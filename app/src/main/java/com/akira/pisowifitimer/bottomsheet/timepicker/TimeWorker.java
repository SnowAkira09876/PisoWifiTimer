package com.akira.pisowifitimer.bottomsheet.timepicker;

import android.app.NotificationManager;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ForegroundInfo;
import androidx.work.ListenableWorker;
import androidx.work.ListenableWorker.Result;
import androidx.work.WorkerParameters;
import com.akira.pisowifitimer.R;
import com.akira.pisowifitimer.StartApplication;
import com.akira.pisowifitimer.pojos.TimeEvent;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.concurrent.TimeUnit;
import org.greenrobot.eventbus.EventBus;

public class TimeWorker extends ListenableWorker {
  private NotificationManager notificationManager;
  private NotificationCompat.Builder builder;
  private final TimeEvent event = new TimeEvent();
  private String wifi;
  private static final int RUNNING_NOTIFID = 0, FINISHED_NOTIFID = 1;
  private int hours, minutes, seconds;
  private Disposable disposable;

  public TimeWorker(@NonNull Context context, @NonNull WorkerParameters params) {
    super(context, params);
    wifi = getInputData().getString(TimerKeys.WIFI_NAME.get());
    hours = getInputData().getInt(TimerKeys.DURATION_HOURS.get(), 0);
    minutes = getInputData().getInt(TimerKeys.DURATION_MINUTES.get(), 0);
    seconds = getInputData().getInt(TimerKeys.DURATION_SECONDS.get(), 0);

    notificationManager =
        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    builder = getNotificationBuilder(context);
  }

  @NonNull
  @Override
  public ListenableFuture<Result> startWork() {
    startForeground();

    Single<String> single =
        Single.create(
            emitter -> {
              long countdownMillis =
                  hours * TimeUnit.HOURS.toMillis(1)
                      + minutes * TimeUnit.MINUTES.toMillis(1)
                      + seconds * TimeUnit.SECONDS.toMillis(1);

              long startTimeMillis = System.currentTimeMillis();

              Disposable disposable =
                  Observable.interval(1, TimeUnit.SECONDS)
                      .map(
                          elapsedTicks ->
                              countdownMillis - (System.currentTimeMillis() - startTimeMillis))
                      .takeWhile(remainingMillis -> remainingMillis > 0)
                      .map(
                          remainingMillis -> {
                            long remainingHours = TimeUnit.MILLISECONDS.toHours(remainingMillis);
                            long remainingMinutes =
                                TimeUnit.MILLISECONDS.toMinutes(remainingMillis) % 60;
                            long remainingSeconds =
                                TimeUnit.MILLISECONDS.toSeconds(remainingMillis) % 60;

                            return String.format(
                                "%d hours, %d minutes, %d seconds",
                                remainingHours, remainingMinutes, remainingSeconds);
                          })
                      .doOnNext(
                          countdownText -> {
                            builder.setContentText(countdownText);
                            updateNotification(RUNNING_NOTIFID, builder);

                            event.setTime(countdownText);
                            event.setStatus(TimerKeys.RUNNING);
                            EventBus.getDefault().post(event);
                          })
                      .doOnComplete(
                          () -> {
                            builder.setContentText(TimerKeys.FINISHED.get());
                            builder.setOngoing(false);
                            updateNotification(FINISHED_NOTIFID, builder);

                            event.setStatus(TimerKeys.FINISHED);
                            EventBus.getDefault().post(event);

                            emitter.onSuccess(TimerKeys.FINISHED.get());
                          })
                      .subscribe();

              emitter.setCancellable(() -> disposable.dispose());
            });

    SettableFuture<Result> future = SettableFuture.create();

    disposable =
        single
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                result -> {
                  future.set(Result.success());
                },
                error -> {
                  future.set(Result.failure());
                });

    return future;
  }

  @Override
  public void onStopped() {
    disposable.dispose();

    builder.setContentText(TimerKeys.STOPPED.get());
    builder.setOngoing(false);
    updateNotification(FINISHED_NOTIFID, builder);

    event.setStatus(TimerKeys.STOPPED);
    EventBus.getDefault().post(event);

    super.onStopped();
  }

  private void startForeground() {
    ForegroundInfo foregroundInfo = new ForegroundInfo(RUNNING_NOTIFID, builder.build());
    setForegroundAsync(foregroundInfo);
  }

  private void updateNotification(int id, NotificationCompat.Builder b) {
    notificationManager.notify(id, b.build());
  }

  private NotificationCompat.Builder getNotificationBuilder(Context context) {
    return new NotificationCompat.Builder(getApplicationContext(), StartApplication.CHANNEL_ID)
        .setContentTitle("Connected to " + wifi)
        .setSmallIcon(R.drawable.ic_alarm)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setOngoing(true)
        .setUsesChronometer(true);
  }
}

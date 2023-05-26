package com.akira.pisowifitimer.bottomsheet.timepicker;

import android.app.NotificationManager;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ForegroundInfo;
import androidx.work.ListenableWorker.Result;
import androidx.work.WorkerParameters;
import androidx.work.rxjava3.RxWorker;
import com.akira.pisowifitimer.R;
import com.akira.pisowifitimer.StartApplication;
import com.akira.pisowifitimer.data.room.AkiraRoom;
import com.akira.pisowifitimer.hilt.AppEntryPoint;
import com.akira.pisowifitimer.pojos.TimeEvent;
import com.akira.pisowifitimer.pojos.TimeHistoryModel;
import dagger.hilt.android.EntryPointAccessors;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.concurrent.TimeUnit;
import org.greenrobot.eventbus.EventBus;

public class TimeWorker extends RxWorker {
  private NotificationManager notificationManager;
  private NotificationCompat.Builder builder;
  private final TimeEvent event = new TimeEvent();
  private static final int NOTIFID = 0;
  private int hours, minutes, seconds;
  private String time, date, wifi;
  private AkiraRoom room;

  public TimeWorker(@NonNull Context context, @NonNull WorkerParameters params) {
    super(context, params);
    time = getInputData().getString(TimerKeys.TIME.get());
    date = getInputData().getString(TimerKeys.DATE.get());
    wifi = getInputData().getString(TimerKeys.WIFI.get());

    hours = getInputData().getInt(TimerKeys.HOUR.get(), 0);
    minutes = getInputData().getInt(TimerKeys.MINUTE.get(), 0);
    seconds = getInputData().getInt(TimerKeys.SECOND.get(), 0);

    notificationManager =
        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    builder = getNotificationBuilder(context);

    AppEntryPoint entryPoint =
        EntryPointAccessors.fromApplication(getApplicationContext(), AppEntryPoint.class);
    room = entryPoint.getAkiraRoom();
  }

  @NonNull
  @Override
  public Single<Result> createWork() {
    startForeground();

    return Single.create(
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
                        updateNotification(builder);

                        event.setTime(countdownText);
                        event.setStatus(TimerKeys.RUNNING);
                        EventBus.getDefault().post(event);
                      })
                  .doOnComplete(
                      () -> {
                        builder.setContentText(TimerKeys.FINISHED.get());
                        builder.setOngoing(false);
                        updateNotification(builder);

                        event.setStatus(TimerKeys.FINISHED);
                        EventBus.getDefault().postSticky(event);
                        EventBus.getDefault().post(event);

                        emitter.onSuccess(Result.success());

                        TimeHistoryModel model = new TimeHistoryModel();
                        model.setDate(date);
                        model.setTime(time);
                        model.setWifi(wifi);

                        room.getTimeHistoryDAO().insert(model);
                      })
                  .doOnError(e -> emitter.onError(e))
                  .subscribeOn(AndroidSchedulers.mainThread())
                  .subscribe();

          emitter.setCancellable(() -> disposable.dispose());
        });
  }

  @Override
  public void onStopped() {
    builder.setContentText(TimerKeys.STOPPED.get());
    builder.setOngoing(false);
    updateNotification(builder);

    event.setStatus(TimerKeys.STOPPED);
    EventBus.getDefault().post(event);

    super.onStopped();
  }

  private void startForeground() {
    ForegroundInfo foregroundInfo = new ForegroundInfo(NOTIFID, builder.build());
    setForegroundAsync(foregroundInfo);
  }

  private void updateNotification(NotificationCompat.Builder b) {
    notificationManager.notify(NOTIFID, b.build());
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

package com.akira.pisowifitimer.bottomsheet.timepicker;

import android.app.NotificationManager;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ForegroundInfo;
import androidx.work.ListenableWorker.Result;
import androidx.work.WorkerParameters;
import androidx.work.rxjava3.RxWorker;
import com.akira.pisowifitimer.StartApplication;
import com.akira.pisowifitimer.data.room.AkiraRoom;
import com.akira.pisowifitimer.hilt.AppEntryPoint;
import com.akira.pisowifitimer.pojos.TimeEvent;
import com.akira.pisowifitimer.pojos.HistoryModel;
import dagger.hilt.android.EntryPointAccessors;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.greenrobot.eventbus.EventBus;
import com.akira.pisowifitimer.R;

public class TimeWorker extends RxWorker {
  private NotificationCompat.Builder timer, alarm;
  private NotificationManager notificationManager;
  private final TimeEvent event = new TimeEvent();
  private static final int TIMER_ID = 0, ALARM_ID = 1;
  private int hours, minutes, seconds, amount;
  private String time, date;
  private AkiraRoom room;
  public static final String HOUR = "hour",
      MINUTE = "minute",
      SECOND = "second",
      AMOUNT = "amount",
      TIME = "time",
      DATE = "date";

  public TimeWorker(@NonNull Context context, @NonNull WorkerParameters params) {
    super(context, params);
    time = getInputData().getString(TIME);
    date = getInputData().getString(DATE);

    amount = getInputData().getInt(AMOUNT, 0);
    hours = getInputData().getInt(HOUR, 0);
    minutes = getInputData().getInt(MINUTE, 0);
    seconds = getInputData().getInt(SECOND, 0);

    AppEntryPoint entryPoint =
        EntryPointAccessors.fromApplication(getApplicationContext(), AppEntryPoint.class);
    room = entryPoint.getAkiraRoom();

    notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    timer = getNotificationBuilder(context, "Timer", StartApplication.TIMER);
    alarm = getNotificationBuilder(context, "Alarm", StartApplication.ALARM);
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
                            Locale.US,
                            "%d hours, %d minutes, %d seconds",
                            remainingHours,
                            remainingMinutes,
                            remainingSeconds);
                      })
                  .doOnNext(
                      countdownText -> {
                        timer.setContentText(countdownText);
                        notificationManager.notify(TIMER_ID, timer.build());

                        event.setTime(countdownText);
                        event.setStatus(TimeStatus.RUNNING);
                        EventBus.getDefault().post(event);
                      })
                  .doOnComplete(
                      () -> {
                        alarm.setContentText("Timer finished!");
                        alarm.setOngoing(false);
                        notificationManager.notify(ALARM_ID, alarm.build());
                        notificationManager.cancel(TIMER_ID);

                        event.setStatus(TimeStatus.FINISHED);
                        EventBus.getDefault().postSticky(event);
                        EventBus.getDefault().post(event);

                        HistoryModel model = new HistoryModel();
                        model.setDate(date);
                        model.setTime(time);
                        model.setAmount(amount);

                        room.getHistoryDAO().insert(model);
                        emitter.onSuccess(Result.success());
                      })
                  .doOnError(e -> emitter.onError(e))
                  .subscribeOn(AndroidSchedulers.mainThread())
                  .subscribe();

          emitter.setCancellable(() -> disposable.dispose());
        });
  }

  @Override
  public void onStopped() {
    super.onStopped();
    timer.setContentText("Timer stopped!");
    timer.setOngoing(false);
    notificationManager.notify(TIMER_ID, timer.build());

    event.setStatus(TimeStatus.STOPPED);
    EventBus.getDefault().post(event);
  }

  private void startForeground() {
    ForegroundInfo foregroundInfo = new ForegroundInfo(TIMER_ID, timer.build());
    setForegroundAsync(foregroundInfo);
  }

  private NotificationCompat.Builder getNotificationBuilder(
      Context context, String title, String channel) {
    return new NotificationCompat.Builder(context, channel)
        .setContentTitle(title)
        .setSmallIcon(R.drawable.ic_alarm)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setOngoing(true)
        .setShowWhen(true);
  }
}

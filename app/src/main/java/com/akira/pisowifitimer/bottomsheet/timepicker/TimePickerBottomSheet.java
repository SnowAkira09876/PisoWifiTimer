package com.akira.pisowifitimer.bottomsheet.timepicker;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.akira.pisowifitimer.StartApplication;
import com.akira.pisowifitimer.activities.history.HistoryActivity;
import com.akira.pisowifitimer.data.room.AkiraRoom;
import com.akira.pisowifitimer.databinding.BottomSheetSetTimeBinding;
import com.akira.pisowifitimer.pojos.TimeEvent;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.slider.Slider;
import dagger.hilt.android.AndroidEntryPoint;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@AndroidEntryPoint
public class TimePickerBottomSheet extends BottomSheetDialogFragment {
  private BottomSheetSetTimeBinding binding;
  private Slider slider_hour, slider_minute, slider_second, slider_amount;
  private Button btn_set, btn_cancel, btn_stop, btn_history, btn_alarm;
  private TextView tv_time;
  private WorkManager workManager;
  private OneTimeWorkRequest request;
  private static final String TIMER_WORK_TAG = "timer_work";

  @Inject AkiraRoom room;
  @Inject ExecutorService executor;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    workManager = WorkManager.getInstance(requireActivity());
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
    binding = BottomSheetSetTimeBinding.inflate(inflater, parent, false);

    onsetViewBinding();
    onsetViews();
    return binding.getRoot();
  }

  @Override
  public void onStart() {
    super.onStart();
    EventBus.getDefault().register(this);
  }

  @Override
  public void onStop() {
    super.onStop();
    EventBus.getDefault().unregister(this);
  }

  @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
  public void onTimeEvent(TimeEvent event) {
    switch (event.getStatus()) {
      case RUNNING:
        btn_stop.setEnabled(true);
        tv_time.setText(event.getTime());
        break;

      case FINISHED:
        btn_stop.setEnabled(false);
        tv_time.setText("Timer finished!");
        TimeEvent stickyEvent = EventBus.getDefault().getStickyEvent(TimeEvent.class);
        if (stickyEvent != null) EventBus.getDefault().removeStickyEvent(stickyEvent);
        break;

      case STOPPED:
        btn_stop.setEnabled(false);
        tv_time.setText("Timer stopped!");
        break;
    }
  }

  private void onsetViewBinding() {
    slider_hour = binding.sliderHour;
    slider_minute = binding.sliderMinute;
    slider_second = binding.sliderSecond;
    slider_amount = binding.sliderAmount;

    btn_set = binding.btnSet;
    btn_cancel = binding.btnCancel;
    btn_stop = binding.btnStop;
    btn_history = binding.btnHistory;
    btn_alarm = binding.btnAlarm;
    
    tv_time = binding.tvTime;
  }

  private void onsetViews() {
    slider_hour.setLabelFormatter(value -> String.format("%sh", String.valueOf((int) value)));
    slider_minute.setLabelFormatter(value -> String.format("%sm", String.valueOf((int) value)));
    slider_second.setLabelFormatter(value -> String.format("%ss", String.valueOf((int) value)));
    slider_amount.setLabelFormatter(value -> String.format("₱%s", String.valueOf((int) value)));

    btn_set.setOnClickListener(
        v -> {
          String hour = String.valueOf((int) slider_hour.getValue());
          String minute = String.valueOf((int) slider_minute.getValue());
          String second = String.valueOf((int) slider_second.getValue());
          String amount = String.valueOf((int) slider_amount.getValue());

          SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy", Locale.getDefault());
          String currentDate = dateFormat.format(new Date());
          String formattedTime =
              String.format("%s hours, %s minutes, %s seconds", hour, minute, second);

          Constraints constraints =
              new Constraints.Builder()
                  // .setRequiredNetworkType(NetworkType.UNMETERED)
                  .build();

          Data data =
              new Data.Builder()
                  .putInt(TimeWorker.HOUR, Integer.parseInt(hour))
                  .putInt(TimeWorker.MINUTE, Integer.parseInt(minute))
                  .putInt(TimeWorker.SECOND, Integer.parseInt(second))
                  .putInt(TimeWorker.AMOUNT, Integer.parseInt(amount))
                  .putString(TimeWorker.TIME, formattedTime)
                  .putString(TimeWorker.DATE, currentDate)
                  .build();

          request =
              new OneTimeWorkRequest.Builder(TimeWorker.class)
                  .setInputData(data)
                  .setConstraints(constraints)
                  .build();

          workManager.enqueueUniqueWork(TIMER_WORK_TAG, ExistingWorkPolicy.REPLACE, request);
          dismiss();
        });

    btn_history.setOnClickListener(
        v -> startActivity(new Intent(requireActivity(), HistoryActivity.class)));

    btn_stop.setOnClickListener(v -> workManager.cancelUniqueWork(TIMER_WORK_TAG));

    btn_cancel.setOnClickListener(v -> dismiss());

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      btn_alarm.setOnClickListener(
          v -> {
            Intent channel = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
            channel.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().getPackageName());
            channel.putExtra(Settings.EXTRA_CHANNEL_ID, StartApplication.ALARM);
            startActivity(channel);
          });
    } else btn_alarm.setVisibility(View.GONE);
  }
}

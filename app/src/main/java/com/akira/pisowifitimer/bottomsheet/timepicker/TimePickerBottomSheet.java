package com.akira.pisowifitimer.bottomsheet.timepicker;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.akira.pisowifitimer.activities.stats.StatsActivity;
import com.akira.pisowifitimer.databinding.BottomSheetSetTimeBinding;
import com.akira.pisowifitimer.pojos.TimeEvent;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Arrays;
import org.greenrobot.eventbus.EventBus;
import androidx.work.Constraints;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class TimePickerBottomSheet extends BottomSheetDialogFragment {
  private BottomSheetSetTimeBinding binding;
  private TextInputLayout tl_wifi, tl_hour, tl_minute, tl_second;
  private TextInputEditText te_wifi, te_hour, te_minute, te_second;
  private Button btn_set, btn_cancel, btn_stop, btn_history;
  private String details = "", unique_work_name = "timer_work";
  private WorkManager workManager;
  private OneTimeWorkRequest request;

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
    EventBus.getDefault().unregister(this);
    super.onStop();
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onTimeEvent(TimeEvent event) {
    switch (event.getStatus()) {
      case RUNNING:
        btn_stop.setEnabled(true);
        tl_wifi.setHelperText(event.getTime());
        break;
      case FINISHED:
      case STOPPED:
        btn_stop.setEnabled(false);
        tl_wifi.setHelperText(event.getStatus().get());
        break;
    }
  }

  private void onsetViewBinding() {
    tl_wifi = binding.tlWifi;
    tl_hour = binding.tlHour;
    tl_minute = binding.tlMinute;
    tl_second = binding.tlSecond;

    te_wifi = binding.teWifi;
    te_hour = binding.teHour;
    te_minute = binding.teMinute;
    te_second = binding.teSecond;

    btn_set = binding.btnSet;
    btn_cancel = binding.btnCancel;
    btn_stop = binding.btnStop;
    btn_history = binding.btnHistory;
  }

  private void onsetViews() {
    te_minute.setFilters(new InputFilter[] {new InputFilterTime(1, 60)});
    te_second.setFilters(new InputFilter[] {new InputFilterTime(1, 60)});

    btn_set.setOnClickListener(
        v -> {
          String wifi = te_wifi.getText().toString().trim();
          String time = te_hour.getText().toString();
          String minute = te_minute.getText().toString();
          String second = te_second.getText().toString();

          if (wifi.isEmpty()) tl_wifi.setError("Wi-Fi is empty");
          if (time.isEmpty()) tl_hour.setError("Hour is empty");
          if (minute.isEmpty()) tl_minute.setError("Minute is empty");
          if (second.isEmpty()) tl_second.setError("Second is empty");

          boolean allTimeFilled =
              Arrays.stream(new String[] {wifi, time, minute, second}).allMatch(s -> !s.isEmpty());

          if (allTimeFilled) {
            tl_wifi.setErrorEnabled(false);
            tl_hour.setErrorEnabled(false);
            tl_minute.setErrorEnabled(false);
            tl_second.setErrorEnabled(false);

            Constraints constraints =
                new Constraints.Builder()
                    // .setRequiredNetworkType(NetworkType.UNMETERED)
                    .build();

            Data data =
                new Data.Builder()
                    .putString(TimerKeys.WIFI_NAME.get(), wifi)
                    .putInt(TimerKeys.DURATION_HOURS.get(), Integer.parseInt(time))
                    .putInt(TimerKeys.DURATION_MINUTES.get(), Integer.parseInt(minute))
                    .putInt(TimerKeys.DURATION_SECONDS.get(), Integer.parseInt(second))
                    .build();

            request =
                new OneTimeWorkRequest.Builder(TimeWorker.class)
                    .setInputData(data)
                    .setConstraints(constraints)
                    .build();

            workManager.enqueueUniqueWork(unique_work_name, ExistingWorkPolicy.REPLACE, request);
            dismiss();
          }
        });

    btn_history.setOnClickListener(
        v -> startActivity(new Intent(requireActivity(), StatsActivity.class)));

    btn_stop.setOnClickListener(v -> workManager.cancelAllWork());

    btn_cancel.setOnClickListener(v -> dismiss());
  }
}

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
import com.akira.pisowifitimer.data.room.AkiraRoom;
import com.akira.pisowifitimer.databinding.BottomSheetSetTimeBinding;
import com.akira.pisowifitimer.pojos.TimeEvent;
import com.akira.pisowifitimer.pojos.TimeHistoryModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import dagger.hilt.android.AndroidEntryPoint;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import androidx.work.Constraints;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@AndroidEntryPoint
public class TimePickerBottomSheet extends BottomSheetDialogFragment {
  private BottomSheetSetTimeBinding binding;
  private TextInputLayout tl_wifi, tl_hour, tl_minute, tl_second;
  private TextInputEditText te_wifi, te_hour, te_minute, te_second;
  private Button btn_set, btn_cancel, btn_stop, btn_history;
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
    EventBus.getDefault().unregister(this);
    super.onStop();
  }

  @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
  public void onTimeEvent(TimeEvent event) {
    switch (event.getStatus()) {
      case RUNNING:
        btn_stop.setEnabled(true);
        tl_wifi.setHelperText(event.getTime());
        break;

      case FINISHED:
        btn_stop.setEnabled(false);
        tl_wifi.setHelperText(event.getStatus().get());
        TimeEvent stickyEvent = EventBus.getDefault().getStickyEvent(TimeEvent.class);
        if (stickyEvent != null) EventBus.getDefault().removeStickyEvent(stickyEvent);
        break;

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
          String hour = te_hour.getText().toString();
          String minute = te_minute.getText().toString();
          String second = te_second.getText().toString();

          if (wifi.isEmpty()) tl_wifi.setError("Wi-Fi is empty");
          if (hour.isEmpty()) tl_hour.setError("Hour is empty");
          if (minute.isEmpty()) tl_minute.setError("Minute is empty");
          if (second.isEmpty()) tl_second.setError("Second is empty");

          boolean allTimeFilled =
              Arrays.stream(new String[] {wifi, hour, minute, second}).allMatch(s -> !s.isEmpty());

          if (allTimeFilled) {
            tl_wifi.setErrorEnabled(false);
            tl_hour.setErrorEnabled(false);
            tl_minute.setErrorEnabled(false);
            tl_second.setErrorEnabled(false);

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            String currentDate = String.format("%04d-%02d-%02d", year, month, day);
            String currentTime =
                String.format("%s hours, %s minutes, %s seconds", hour, minute, second);

            Constraints constraints =
                new Constraints.Builder()
                    // .setRequiredNetworkType(NetworkType.UNMETERED)
                    .build();

            Data data =
                new Data.Builder()
                    .putString(TimerKeys.WIFI.get(), wifi)
                    .putInt(TimerKeys.HOUR.get(), Integer.parseInt(hour))
                    .putInt(TimerKeys.MINUTE.get(), Integer.parseInt(minute))
                    .putInt(TimerKeys.SECOND.get(), Integer.parseInt(second))
                    .putString(TimerKeys.TIME.get(), currentTime)
                    .putString(TimerKeys.DATE.get(), currentDate)
                    .build();

            request =
                new OneTimeWorkRequest.Builder(TimeWorker.class)
                    .setInputData(data)
                    .setConstraints(constraints)
                    .build();

            workManager.enqueueUniqueWork(TIMER_WORK_TAG, ExistingWorkPolicy.REPLACE, request);
            dismiss();
          }
        });

    btn_history.setOnClickListener(
        v -> startActivity(new Intent(requireActivity(), StatsActivity.class)));

    btn_stop.setOnClickListener(v -> workManager.cancelUniqueWork(TIMER_WORK_TAG));

    btn_cancel.setOnClickListener(v -> dismiss());
  }
}

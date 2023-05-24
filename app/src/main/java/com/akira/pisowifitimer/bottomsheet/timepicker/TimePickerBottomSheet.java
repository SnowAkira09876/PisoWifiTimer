package com.akira.pisowifitimer.bottomsheet.timepicker;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Data;
import com.akira.pisowifitimer.activities.main.MainActivityViewModel;
import com.akira.pisowifitimer.activities.stats.StatsActivity;
import com.akira.pisowifitimer.databinding.BottomSheetSetTimeBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Arrays;

public class TimePickerBottomSheet extends BottomSheetDialogFragment {
  private BottomSheetSetTimeBinding binding;
  private TextInputLayout tl_hour, tl_minute, tl_second;
  private TextInputEditText te_hour, te_minute, te_second;
  private Button btn_set, btn_cancel, btn_alarm, btn_history;
  private MainActivityViewModel mainActivityViewModel;
  private String details = "";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mainActivityViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
    binding = BottomSheetSetTimeBinding.inflate(inflater, parent, false);

    onsetViewBinding();
    onsetViews();
    return binding.getRoot();
  }

  private void onsetViewBinding() {
    tl_hour = binding.tlHour;
    tl_minute = binding.tlMinute;
    tl_second = binding.tlSecond;

    te_hour = binding.teHour;
    te_minute = binding.teMinute;
    te_second = binding.teSecond;

    btn_set = binding.btnSet;
    btn_cancel = binding.btnCancel;
    btn_alarm = binding.btnAlarm;
    btn_history = binding.btnHistory;
  }

  private void onsetViews() {
    te_minute.setFilters(new InputFilter[] {new InputFilterTime(1, 99)});
    te_minute.setFilters(new InputFilter[] {new InputFilterTime(1, 60)});
    te_second.setFilters(new InputFilter[] {new InputFilterTime(1, 60)});

    btn_set.setOnClickListener(
        v -> {
          String time = te_hour.getText().toString();
          String minute = te_minute.getText().toString();
          String second = te_second.getText().toString();

          boolean allTimeFilled =
              Arrays.stream(new String[] {time, minute, second}).allMatch(s -> !s.isEmpty());

          if (allTimeFilled) {
            Data data =
                new Data.Builder()
                    .putInt(TimeWorker.KEY_DURATION_HOURS, Integer.parseInt(time))
                    .putInt(TimeWorker.KEY_DURATION_MINUTES, Integer.parseInt(minute))
                    .putInt(TimeWorker.KEY_DURATION_SECONDS, Integer.parseInt(second))
                    .build();

            mainActivityViewModel.setData(data);
            dismiss();
          }
        });

    btn_history.setOnClickListener(
        v -> startActivity(new Intent(requireActivity(), StatsActivity.class)));

    btn_cancel.setOnClickListener(v -> dismiss());
  }
}

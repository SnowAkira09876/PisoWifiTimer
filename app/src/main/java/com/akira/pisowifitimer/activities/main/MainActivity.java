package com.akira.pisowifitimer.activities.main;

import android.os.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.WorkManager;
import androidx.work.Constraints;
import androidx.work.OneTimeWorkRequest;
import com.akira.pisowifitimer.bottomsheet.timepicker.TimeWorker;
import com.akira.pisowifitimer.bottomsheet.timepicker.TimePickerBottomSheet;
import com.akira.pisowifitimer.databinding.ActivityMainBinding;
import com.akira.pisowifitimer.pojos.TimeEvent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import dagger.hilt.android.AndroidEntryPoint;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
  private ActivityMainBinding binding;
  private FloatingActionButton fab;
  private TimePickerBottomSheet timePickerBottomSheet;
  private MainActivityViewModel viewModel;
  private WorkManager workManager;
  private OneTimeWorkRequest request;
  private boolean isTimerRunning = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    timePickerBottomSheet = new TimePickerBottomSheet();
    viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
    workManager = WorkManager.getInstance(this);

    onsetViewBinding();
    onsetViews();
    onsetWorker();
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

  @Subscribe(threadMode = ThreadMode.BACKGROUND)
  public void onTimeEvent(TimeEvent event) {
    if (event.getIsBusy()) isTimerRunning = true;
    else {
      isTimerRunning = false;
      workManager.cancelWorkById(request.getId());
    }
  }

  private void onsetViewBinding() {
    fab = binding.fab;
  }

  private void onsetViews() {
    setContentView(binding.getRoot());

    fab.setOnClickListener(
        v -> {
          if (isTimerRunning)
            Snackbar.make(binding.getRoot(), "Timer is already running!", Snackbar.LENGTH_SHORT)
                .show();
          else timePickerBottomSheet.show(getSupportFragmentManager(), null);
        });
  }

  private void onsetWorker() {
    Constraints constraints =
        new Constraints.Builder()
            // .setRequiredNetworkType(NetworkType.UNMETERED)
            .build();

    viewModel
        .getData()
        .observe(
            this,
            data -> {
              request =
                  new OneTimeWorkRequest.Builder(TimeWorker.class)
                      .setInputData(data)
                      .setConstraints(constraints)
                      .build();

              workManager.enqueue(request);
            });
  }
}

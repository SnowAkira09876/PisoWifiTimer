package com.akira.pisowifitimer.activities.stats;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.akira.pisowifitimer.databinding.ActivityStatsBinding;
import com.akira.pisowifitimer.widgets.recyclerview.AkiraRecyclerView;

public class StatsActivity extends AppCompatActivity {
  private ActivityStatsBinding binding;
  private AkiraRecyclerView rv;

  @Override
  protected void onCreate(Bundle saveInstanceState) {
    super.onCreate(saveInstanceState);
    binding = ActivityStatsBinding.inflate(getLayoutInflater());
    onsetViewBinding();
    onsetViews();
  }

  private void onsetViewBinding() {
    // TODO: Implement this method

  }

  private void onsetViews() {
    rv = binding.rv;
  }
}

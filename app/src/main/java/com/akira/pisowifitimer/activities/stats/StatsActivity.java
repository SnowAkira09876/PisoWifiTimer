package com.akira.pisowifitimer.activities.stats;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.akira.pisowifitimer.data.room.AkiraRoom;
import com.akira.pisowifitimer.databinding.ActivityStatsBinding;
import com.akira.pisowifitimer.recyclerviews.AdapterFactory;
import com.akira.pisowifitimer.recyclerviews.adapters.TimeHistoryAdapter;
import com.akira.pisowifitimer.widgets.recyclerview.AkiraRecyclerView;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;

@AndroidEntryPoint
public class StatsActivity extends AppCompatActivity {
  private ActivityStatsBinding binding;
  private AkiraRecyclerView rv;
  private TimeHistoryAdapter adapter;
  private StatsViewModel viewModel;
  private StatsViewModelFactory factory;
  private Disposable disposable;
  private TextView emptyView;

  @Inject AkiraRoom room;

  @Override
  protected void onCreate(Bundle saveInstanceState) {
    super.onCreate(saveInstanceState);
    binding = ActivityStatsBinding.inflate(getLayoutInflater());
    factory = new StatsViewModelFactory(room);
    viewModel = new ViewModelProvider(this, factory).get(StatsViewModel.class);

    adapter = AdapterFactory.getTimeHistoryAdapter();
    onsetViewBinding();
    onsetViews();
    onsetObservers();
  }

  @Override
  protected void onDestroy() {
    disposable.dispose();
    super.onDestroy();
  }

  private void onsetViewBinding() {
    rv = binding.rv;
    emptyView = binding.emptyView;
  }

  private void onsetViews() {
    setContentView(binding.getRoot());
    rv.setAdapter(adapter);
    rv.setEmptyView(emptyView);
  }

  private void onsetObservers() {
    disposable =
        viewModel
            .getTimeHistory()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(list -> adapter.submitList(list));
  }
}

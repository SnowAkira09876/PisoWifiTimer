package com.akira.pisowifitimer.activities.history;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;
import com.akira.pisowifitimer.data.room.AkiraRoom;
import com.akira.pisowifitimer.databinding.ActivityHistoryBinding;
import com.akira.pisowifitimer.recyclerviews.AdapterFactory;
import com.akira.pisowifitimer.recyclerviews.adapters.HistoryAdapter;
import com.akira.pisowifitimer.widgets.recyclerview.AkiraRecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;
import javax.inject.Inject;

@AndroidEntryPoint
public class HistoryActivity extends AppCompatActivity {
  private ActivityHistoryBinding binding;
  private AkiraRecyclerView rv;
  private HistoryAdapter adapter;
  private HistoryViewModel viewModel;
  private HistoryFactory factory;
  private Disposable history, history_between, amount, amount_between;
  private TextView emptyView;
  private MaterialToolbar toolbar;
  private final CompositeDisposable disposables = new CompositeDisposable();

  @Inject AkiraRoom room;

  @Override
  protected void onCreate(Bundle saveInstanceState) {
    super.onCreate(saveInstanceState);
    binding = ActivityHistoryBinding.inflate(getLayoutInflater());
    factory = new HistoryFactory(room);
    viewModel = new ViewModelProvider(this, factory).get(HistoryViewModel.class);

    adapter = AdapterFactory.getHistoryAdapter();

    onsetViewBinding();
    onsetViews();
    onsetObservers();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    disposables.clear();
  }

  private void onsetViewBinding() {
    rv = binding.rv;
    emptyView = binding.emptyView;
    toolbar = binding.toolbar;
  }

  private void onsetViews() {
    setSupportActionBar(binding.toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    setContentView(binding.getRoot());
    rv.setAdapter(adapter);
    rv.setEmptyView(emptyView);

    binding.fab.setOnClickListener(v -> showDatePicker());
  }

  private void onsetObservers() {
    history =
        viewModel
            .getHistory()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                list -> {
                  adapter.submitList(list);
                });

    amount =
        viewModel
            .getTotalAmount()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                a -> toolbar.setSubtitle(String.format(Locale.US, "%s%d %s", "₱", a, "pesos spent")),
                System.out::println);

    disposables.add(amount);

    disposables.add(history);
  }

  private void showDatePicker() {
    CalendarConstraints constraints =
        new CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.now()).build();

    MaterialDatePicker<Pair<Long, Long>> datePicker =
        MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Pick a date range")
            .setCalendarConstraints(constraints)
            .build();

    datePicker.addOnPositiveButtonClickListener(
        selection -> {
          long startDate = selection.first;
          long endDate = selection.second;

          SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
          String formattedStartDate = dateFormat.format(new Date(startDate));
          String formattedEndDate = dateFormat.format(new Date(endDate));

          history_between =
              viewModel
                  .getHistoryBetween(formattedStartDate, formattedEndDate)
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(
                      list -> {
                        adapter.submitList(list);
                      },
                      System.out::println);

          amount_between =
              viewModel
                  .getTotalAmountBetween(formattedStartDate, formattedEndDate)
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(
                      a -> toolbar.setSubtitle(String.format(Locale.US,"%s%d %s", "₱", a, "pesos spent")),
                      System.out::println);

          disposables.add(amount_between);
          disposables.add(history_between);
        });

    datePicker.show(getSupportFragmentManager(), "date_picker");
  }
}

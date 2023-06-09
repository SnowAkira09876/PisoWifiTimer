package com.akira.pisowifitimer.activities.history;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;
import com.akira.pisowifitimer.data.room.AkiraRoom;
import com.akira.pisowifitimer.databinding.ActivityHistoryBinding;
import com.akira.pisowifitimer.recyclerviews.adapters.HistoryAdapter;
import com.akira.pisowifitimer.recyclerviews.diffs.HistoryDiff;
import com.akira.pisowifitimer.widgets.recyclerview.AkiraRecyclerView;
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
    private Disposable history,
            history_between,
            amount,
            amount_between,
            start_date,
            end_date,
            total_amount;
    private TextView emptyView, tv_amount, tv_startDate, tv_endDate;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject AkiraRoom room;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        factory = new HistoryFactory(room);
        viewModel = new ViewModelProvider(this, factory).get(HistoryViewModel.class);

        adapter = new HistoryAdapter(new HistoryDiff());

        onsetViewBinding();
        onsetViews();
        onsetObservers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }

    private void onsetViewBinding() {
        rv = binding.rv;
        emptyView = binding.emptyView;
        tv_amount = binding.tvAmount;
        tv_startDate = binding.tvStartDate;
        tv_endDate = binding.tvEndDate;
    }

    private void onsetViews() {

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
                                a -> tv_amount.setText(String.format(Locale.US, "%s%d", "₱", a)),
                                System.out::println);

        start_date =
                viewModel
                        .getStartDate()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(date -> tv_startDate.setText(date), System.out::println);

        end_date =
                viewModel
                        .getEndDate()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(date -> tv_endDate.setText(date), System.out::println);

        disposables.add(amount);

        disposables.add(history);

        disposables.add(start_date);

        disposables.add(end_date);
    }

    private void showDatePicker() {
        CalendarConstraints constraints =
                new CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointBackward.now())
                        .build();

        MaterialDatePicker<Pair<Long, Long>> datePicker =
                MaterialDatePicker.Builder.dateRangePicker()
                        .setTitleText("Pick a date range")
                        .setCalendarConstraints(constraints)
                        .build();

        datePicker.addOnPositiveButtonClickListener(
                selection -> {
                    long startDate = selection.first;
                    long endDate = selection.second;

                    SimpleDateFormat dateFormat =
                            new SimpleDateFormat("MMM dd yyyy", Locale.getDefault());
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
                                            a ->
                                                    tv_amount.setText(
                                                            String.format(
                                                                    Locale.US, "%s%d", "₱", a)),
                                            System.out::println);

                    disposables.add(amount_between);
                    disposables.add(history_between);

                    tv_startDate.setText(formattedStartDate);
                    tv_endDate.setText(formattedEndDate);
                });

        datePicker.show(getSupportFragmentManager(), "date_picker");
    }
}

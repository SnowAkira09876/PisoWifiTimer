package com.akira.pisowifitimer.recyclerviews.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.ListAdapter;
import com.akira.pisowifitimer.databinding.ItemHistoryBinding;
import com.akira.pisowifitimer.pojos.HistoryModel;
import com.akira.pisowifitimer.recyclerviews.diffs.HistoryDiff;
import com.akira.pisowifitimer.recyclerviews.holders.HistoryViewHolder;

public class HistoryAdapter extends ListAdapter<HistoryModel, HistoryViewHolder> {

  public HistoryAdapter(HistoryDiff diff) {
    super(diff);
  }

  @Override
  public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new HistoryViewHolder(
        ItemHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
  }

  @Override
  public void onBindViewHolder(HistoryViewHolder holder, int position) {
    holder.bind(getItem(position));
  }
}

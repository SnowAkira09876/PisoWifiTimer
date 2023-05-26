package com.akira.pisowifitimer.recyclerviews.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.DiffUtil;
import com.akira.pisowifitimer.databinding.ItemTimeBinding;
import com.akira.pisowifitimer.pojos.TimeHistoryModel;
import com.akira.pisowifitimer.recyclerviews.BaseListAdapter;
import com.akira.pisowifitimer.recyclerviews.diffs.TimeHistoryDiff;
import com.akira.pisowifitimer.recyclerviews.holders.TimeHistoryViewHolder;

public class TimeHistoryAdapter extends BaseListAdapter<TimeHistoryModel, TimeHistoryViewHolder> {

  public TimeHistoryAdapter(TimeHistoryDiff diff) {
    super(diff);
  }

  @Override
  public TimeHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new TimeHistoryViewHolder(
        ItemTimeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
  }

  @Override
  public void onBindViewHolder(TimeHistoryViewHolder holder, int position) {
    holder.bind(getItem(position));
  }
}

package com.akira.pisowifitimer.recyclerviews.diffs;

import androidx.recyclerview.widget.DiffUtil;
import com.akira.pisowifitimer.pojos.TimeHistoryModel;

public class TimeHistoryDiff extends DiffUtil.ItemCallback<TimeHistoryModel> {

  @Override
  public boolean areItemsTheSame(TimeHistoryModel oldItem, TimeHistoryModel newItem) {
    return oldItem.getId() == newItem.getId();
  }

  @Override
  public boolean areContentsTheSame(TimeHistoryModel oldItem, TimeHistoryModel newItem) {
    return oldItem.equals(newItem);
  }
}

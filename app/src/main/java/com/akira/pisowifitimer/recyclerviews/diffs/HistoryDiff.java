package com.akira.pisowifitimer.recyclerviews.diffs;

import androidx.recyclerview.widget.DiffUtil;
import com.akira.pisowifitimer.pojos.HistoryModel;

public class HistoryDiff extends DiffUtil.ItemCallback<HistoryModel> {

  @Override
  public boolean areItemsTheSame(HistoryModel oldItem, HistoryModel newItem) {
    return oldItem.getId() == newItem.getId();
  }

  @Override
  public boolean areContentsTheSame(HistoryModel oldItem, HistoryModel newItem) {
    return oldItem.equals(newItem);
  }
}

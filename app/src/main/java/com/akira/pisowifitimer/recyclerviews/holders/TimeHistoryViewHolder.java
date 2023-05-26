package com.akira.pisowifitimer.recyclerviews.holders;

import androidx.recyclerview.widget.RecyclerView;
import com.akira.pisowifitimer.databinding.ItemTimeBinding;
import com.akira.pisowifitimer.pojos.TimeHistoryModel;

public class TimeHistoryViewHolder extends RecyclerView.ViewHolder {
  private ItemTimeBinding binding;

  public TimeHistoryViewHolder(ItemTimeBinding binding) {
    super(binding.getRoot());
    this.binding = binding;
  }

  public void bind(TimeHistoryModel model) {
    binding.setModel(model);
  }
}

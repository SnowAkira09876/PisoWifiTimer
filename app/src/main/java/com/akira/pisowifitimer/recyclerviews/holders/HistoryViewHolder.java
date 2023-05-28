package com.akira.pisowifitimer.recyclerviews.holders;

import androidx.recyclerview.widget.RecyclerView;
import com.akira.pisowifitimer.databinding.ItemHistoryBinding;
import com.akira.pisowifitimer.pojos.HistoryModel;

public class HistoryViewHolder extends RecyclerView.ViewHolder {
  private ItemHistoryBinding binding;

  public HistoryViewHolder(ItemHistoryBinding binding) {
    super(binding.getRoot());
    this.binding = binding;
  }

  public void bind(HistoryModel model) {
    binding.setModel(model);
  }
}

package com.akira.pisowifitimer.widgets.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public class AkiraRecyclerView extends RecyclerView {
  private View emptyView;
  private boolean emptyViewVisible;

  private final AdapterDataObserver observer =
      new AdapterDataObserver() {
        @Override
        public void onChanged() {
          checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
          checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
          checkIfEmpty();
        }
      };

  public AkiraRecyclerView(Context context) {
    super(context);
  }

  public AkiraRecyclerView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public AkiraRecyclerView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  public void setAdapter(Adapter adapter) {
    final Adapter oldAdapter = getAdapter();
    if (oldAdapter != null) oldAdapter.unregisterAdapterDataObserver(observer);

    super.setAdapter(adapter);

    if (adapter != null) adapter.registerAdapterDataObserver(observer);
  }

  public void setEmptyView(View emptyView) {
    this.emptyView = emptyView;
  }

  private void checkIfEmpty() {
    if (emptyView != null && getAdapter() != null) {
      emptyViewVisible = getAdapter().getItemCount() == 0;
      emptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
      setVisibility(emptyViewVisible ? GONE : VISIBLE);
    }
  }

  public boolean getEmptyViewVisible() {
    return this.emptyViewVisible;
  }

  public void setEmptyViewVisible(boolean emptyViewVisible) {
    this.emptyViewVisible = emptyViewVisible;
  }
}

package com.akira.pisowifitimer.recyclerviews;

import com.akira.pisowifitimer.recyclerviews.adapters.HistoryAdapter;
import com.akira.pisowifitimer.recyclerviews.diffs.HistoryDiff;

public class AdapterFactory {

  public static HistoryAdapter getHistoryAdapter() {
    return new HistoryAdapter(new HistoryDiff());
  }
}

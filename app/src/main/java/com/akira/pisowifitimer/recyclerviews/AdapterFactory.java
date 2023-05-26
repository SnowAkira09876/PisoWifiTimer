package com.akira.pisowifitimer.recyclerviews;

import com.akira.pisowifitimer.recyclerviews.adapters.TimeHistoryAdapter;
import com.akira.pisowifitimer.recyclerviews.diffs.TimeHistoryDiff;

public class AdapterFactory {

  public static TimeHistoryAdapter getTimeHistoryAdapter() {
    return new TimeHistoryAdapter(new TimeHistoryDiff());
  }
}

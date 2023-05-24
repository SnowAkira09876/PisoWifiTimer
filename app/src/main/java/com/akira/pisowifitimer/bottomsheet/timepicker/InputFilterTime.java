package com.akira.pisowifitimer.bottomsheet.timepicker;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterTime implements InputFilter {
  private final int minValue;
  private final int maxValue;

  public InputFilterTime(int minValue, int maxValue) {
    this.minValue = minValue;
    this.maxValue = maxValue;
  }

  @Override
  public CharSequence filter(
      CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
    try {
      String input = dest.toString() + source.toString();
      int value = Integer.parseInt(input);

      if (value >= maxValue || input.length() > 2) return "";

    } catch (NumberFormatException ignored) {
    }
    return null;
  }
}

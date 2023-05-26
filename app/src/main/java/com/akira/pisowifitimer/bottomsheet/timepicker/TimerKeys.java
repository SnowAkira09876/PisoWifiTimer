package com.akira.pisowifitimer.bottomsheet.timepicker;

public enum TimerKeys {
  WIFI("wifi name"),
  HOUR("hour"),
  MINUTE("minute"),
  SECOND("second"),
  RUNNING("Timer running!"),
  STOPPED("Timer stopped!"),
  FINISHED("Time's up!"),
  TIME("time"),
  DATE("date");

  private final String value;

  TimerKeys(String value) {
    this.value = value;
  }

  public String get() {
    return value;
  }
}

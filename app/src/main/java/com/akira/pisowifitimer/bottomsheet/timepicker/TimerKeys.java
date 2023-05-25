package com.akira.pisowifitimer.bottomsheet.timepicker;

public enum TimerKeys {
  WIFI_NAME("wifi_name"),
  DURATION_HOURS("duration_hours"),
  DURATION_MINUTES("duration_minutes"),
  DURATION_SECONDS("duration_seconds"),
  RUNNING("Timer running!"),
  STOPPED("Timer stopped!"),
  FINISHED("Time's up!");
  
  private final String value;

  TimerKeys(String value) {
    this.value = value;
  }

  public String get() {
    return value;
  }
}

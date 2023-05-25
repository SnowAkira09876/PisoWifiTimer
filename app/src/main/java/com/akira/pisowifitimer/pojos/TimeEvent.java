package com.akira.pisowifitimer.pojos;

import com.akira.pisowifitimer.bottomsheet.timepicker.TimerKeys;

public class TimeEvent {
  private String time;
  private TimerKeys status;

  public TimeEvent() {}

  public String getTime() {
    return this.time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public TimerKeys getStatus() {
    return this.status;
  }

  public void setStatus(TimerKeys status) {
    this.status = status;
  }
}

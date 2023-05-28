package com.akira.pisowifitimer.pojos;

import com.akira.pisowifitimer.bottomsheet.timepicker.TimeStatus;

public class TimeEvent {
  private String time;
  private TimeStatus status;

  public TimeEvent() {}

  public String getTime() {
    return this.time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public TimeStatus getStatus() {
    return this.status;
  }

  public void setStatus(TimeStatus status) {
    this.status = status;
  }
}

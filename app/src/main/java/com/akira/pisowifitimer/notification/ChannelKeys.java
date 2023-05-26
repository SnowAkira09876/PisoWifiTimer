package com.akira.pisowifitimer.notification;

public enum ChannelKeys {
  TIMER("timer_key"),
  ALARM("alarm_key");

  private final String value;

  ChannelKeys(String value) {
    this.value = value;
  }

  public String valueOf() {
    return this.value;
  }
}

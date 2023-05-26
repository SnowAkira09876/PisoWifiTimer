package com.akira.pisowifitimer.pojos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "TimeHistoryTable")
public class TimeHistoryModel {
  @PrimaryKey(autoGenerate = true)
  private int id;

  private String time, wifi, date;

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTime() {
    return this.time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getWifi() {
    return this.wifi;
  }

  public void setWifi(String wifi) {
    this.wifi = wifi;
  }

  public String getDate() {
    return this.date;
  }

  public void setDate(String date) {
    this.date = date;
  }
}

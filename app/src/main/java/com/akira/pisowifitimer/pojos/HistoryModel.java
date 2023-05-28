package com.akira.pisowifitimer.pojos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "HistoryTable")
public class HistoryModel {
  @PrimaryKey(autoGenerate = true)
  private int id;

  private String time, date;
  private int amount;

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

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

  public String getDate() {
    return this.date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public int getAmount() {
    return this.amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }
}

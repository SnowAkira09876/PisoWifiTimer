package com.akira.pisowifitimer.activities.main;

import androidx.lifecycle.MutableLiveData;
import androidx.work.Data;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {
  private final MutableLiveData<Data> data = new MutableLiveData<>();

  public LiveData<Data> getData() {
    return this.data;
  }

  public void setData(Data data) {
    this.data.setValue(data);
  }
}

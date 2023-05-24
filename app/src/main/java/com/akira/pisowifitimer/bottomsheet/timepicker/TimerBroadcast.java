package com.akira.pisowifitimer.bottomsheet.timepicker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.akira.pisowifitimer.pojos.TimeEvent;
import org.greenrobot.eventbus.EventBus;

public class TimerBroadcast extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    String action = intent.getAction();
    if (action != null && action.equals("CANCEL_WORKER")) {
      TimeEvent event = new TimeEvent();
      event.setIsBusy(false);
      
      EventBus.getDefault().post(event);
    }
  }
}

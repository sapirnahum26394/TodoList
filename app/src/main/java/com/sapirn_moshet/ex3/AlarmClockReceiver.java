package com.sapirn_moshet.ex3;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AlarmClockReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String alarmType = intent.getStringExtra("alarmType");
        int alarmId = intent.getIntExtra("alarmId", -1);
        Calendar now = Calendar.getInstance();  // get time now from cmputer
        String timeNow = new SimpleDateFormat("HH:mm:ss").format(now.getTime());

        String msg = ">>> AlarmClockReceiver.onReceive() alarmType=" + alarmType + ", alarmId=" +alarmId + " (" +timeNow +")";
        Log.d("mylog",msg);


        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}

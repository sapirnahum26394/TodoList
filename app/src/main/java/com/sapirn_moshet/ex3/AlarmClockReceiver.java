package com.sapirn_moshet.ex3;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmClockReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "channel_main";
    private static final CharSequence CHANNEL_NAME = "Main Channel";
    private NotificationManager notificationManager;
    private  Date date = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        notificationsSetup(context);

        String username = intent.getStringExtra("username");
        String title = intent.getStringExtra("title");
        String datetime = intent.getStringExtra("datetime");

        int alarmId = intent.getIntExtra("alarmId", -1);

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmm");
        try {
            date = sdf.parse(datetime);
        }catch (Exception e) {
            Log.d("Error: ", String.valueOf(e));
        }
        if (date!=null) {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String newdate=df.format(date);
            SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
            String newtime=tf.format(date);
            String msg = "Date: "+newdate+" Time: " +newtime;
            Log.d("mylog", msg);
            showNotification(context, username, title, msg, alarmId);
        }
    }

    private void showNotification(Context context,String user,String title, String msg,int alarmId) {
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notify_bell)
                .setContentTitle("Hello " +user+", Todo: "+ title)
                .setContentText(msg)
                .build();

        notificationManager.notify(alarmId, notification);
    }

    private void notificationsSetup(Context context) {
        notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID, 	// Constant for Channel ID
                    CHANNEL_NAME, 	// Constant for Channel NAME
                    NotificationManager.IMPORTANCE_HIGH);  // for popup use: IMPORTANCE_HIGH
            notificationManager.createNotificationChannel(notificationChannel);
        }

    }
}

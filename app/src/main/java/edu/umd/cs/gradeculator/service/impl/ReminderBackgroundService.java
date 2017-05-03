package edu.umd.cs.gradeculator.service.impl;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import edu.umd.cs.gradeculator.MainActivity;
import edu.umd.cs.gradeculator.R;

/**
 * Created by weng2 on 4/28/2017.
 */

public class ReminderBackgroundService extends IntentService {


    private static  int MY_NOTIFICATION_ID = 1;
    private static int REMINDER_REQUEST = 2;
    private static int MAIN_REQUEST = 3;
    private static AlarmManager alarmManager;
    private static PendingIntent alarmPendingIntent;

    public ReminderBackgroundService(){
        super("reminder");

    }
    public static Intent newIntent(Context context){
        return new Intent(context, ReminderBackgroundService.class);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {



        Intent restartMainIntent = MainActivity.newIntent(this);

        PendingIntent mContentIntent = PendingIntent.getActivity(getApplicationContext(), MAIN_REQUEST,restartMainIntent,PendingIntent.FLAG_UPDATE_CURRENT);



        Notification.Builder notificationBuilder = new Notification.Builder(getApplicationContext())
                .setTicker(getResources().getString(R.string.reminder_notification))
                .setSmallIcon(android.R.drawable.ic_menu_compass)
                .setContentTitle(getResources().getString(R.string.reminder_notification))
                .setContentText("TO DO, for grade")
                .setContentIntent(mContentIntent)
                .setAutoCancel(true);

        Notification notification = notificationBuilder.build();


        NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(MY_NOTIFICATION_ID,notification);
    }

    public static void setReminderAlarm(Context context, int intervalInMinutes){

        long interval =  (intervalInMinutes*60000) / 2;

        Intent reminderIntent = ReminderBackgroundService.newIntent(context);
        alarmPendingIntent = PendingIntent.getService(context,REMINDER_REQUEST,reminderIntent,PendingIntent.FLAG_UPDATE_CURRENT);



        alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + interval,
                interval,alarmPendingIntent);

    }
    public static void cancelReminderAlarm(Context context){
        if (alarmManager!= null) {
            alarmManager.cancel(alarmPendingIntent);
        }
    }














}

package edu.umd.cs.gradeculator.service.impl;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import edu.umd.cs.gradeculator.MainActivity;
import edu.umd.cs.gradeculator.R;

/**
 * Created by weng2 on 5/3/2017.
 */

public class NotificationService extends Service {

    private NotificationManager notificationManager;
    private static int MAIN_REQUEST = 3;
    private static  int MY_NOTIFICATION_ID = 4;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        notificationManager = (NotificationManager) this.getApplicationContext()
                .getSystemService(
                        this.getApplicationContext().NOTIFICATION_SERVICE);

        Intent restartMainIntent = MainActivity.newIntent(this);

        PendingIntent mContentIntent = PendingIntent.getActivity(getApplicationContext(), MAIN_REQUEST,restartMainIntent,PendingIntent.FLAG_UPDATE_CURRENT);


        Notification.Builder notificationBuilder = new Notification.Builder(getApplicationContext())
                .setTicker(getResources().getString(R.string.reminder_notification))
                .setSmallIcon(android.R.drawable.ic_menu_compass)
                .setContentTitle(getResources().getString(R.string.reminder_notification))
                .setContentText("Todo for due")
                .setContentIntent(mContentIntent)
                .setAutoCancel(true);

        Notification notification = notificationBuilder.build();


        NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(MY_NOTIFICATION_ID,notification);







    }
}

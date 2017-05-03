package edu.umd.cs.gradeculator.service.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by weng2 on 5/3/2017.
 */

public class AlarmRecever extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {
         if(intent !=null){
             Bundle bundle = intent.getExtras();
             int id = bundle.getInt("id");

             Intent myIntent = new Intent(context,NotificationService.class);
             myIntent.putExtra("id",id);
             context.startService(myIntent);



         }
    }
}

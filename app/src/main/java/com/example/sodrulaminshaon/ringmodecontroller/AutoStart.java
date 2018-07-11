package com.example.sodrulaminshaon.ringmodecontroller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * Created by Sodrul Amin Shaon on 22-Jun-18.
 */

public class AutoStart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.PREFERENCE_NAME,context.MODE_PRIVATE);
        boolean auto = preferences.getBoolean(Constants.AUTO_CONTROL_STR,false);
        if(auto){
            Intent serviceIntent =  new Intent(context, MyService.class);
            context.startService(serviceIntent);
            Toast.makeText(context,"Starting service",Toast.LENGTH_SHORT);
        }
    }
}

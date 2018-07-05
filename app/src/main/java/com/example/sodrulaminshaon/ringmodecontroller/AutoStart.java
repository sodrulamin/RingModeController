package com.example.sodrulaminshaon.ringmodecontroller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Sodrul Amin Shaon on 22-Jun-18.
 */

public class AutoStart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(MainActivity.getAutoControl()){
            MainActivity.startService();
        }
    }
}

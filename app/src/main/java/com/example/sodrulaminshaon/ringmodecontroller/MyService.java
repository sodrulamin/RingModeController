package com.example.sodrulaminshaon.ringmodecontroller;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Created by Sodrul Amin Shaon on 22-Jun-18.
 */

public class MyService extends Service {
    WifiManager wifiManager;
    WifiReceiver receiverWifi;
    private static final String LIST_TESTING = "ListTest";
    private boolean startedByService = true;
    MyService service;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        service = this;
        new WifiCheckerThread().start();
        return START_STICKY;
    }
    private class WifiCheckerThread extends Thread{
        @Override
        public void run(){
            try {
                SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
                boolean auto = preferences.getBoolean(Constants.AUTO_CONTROL_STR, false);
                if (auto) {
                    wifiManager = (WifiManager) service.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    if (!wifiManager.isWifiEnabled()) {
                        startedByService = true;
                        wifiManager.setWifiEnabled(true);
                    } else startedByService = false;
                    //Log.i(LIST_TESTING,"Wifi is enabled. Now going to check the available list.");
                    receiverWifi = new WifiReceiver();
                    //if(!isRegistered(receiverWifi))
                    try{
                        registerReceiver(receiverWifi,
                                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                    }catch (Exception e){}
                    wifiManager.startScan();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                Thread.sleep(10000);
            }catch (Exception e){}

            stopSelf();
        }
    }

    private class WifiReceiver extends BroadcastReceiver {

        public void onReceive(Context c, Intent intent) {
            try {
                int mode = getNewMode();
                AudioManager am = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
                am.setRingerMode(mode);
                if(HomeTab.homeTab != null)HomeTab.changeMode(mode);
            }catch (Exception e){
                e.printStackTrace();
            }

            stopSelf();
        }
    }
    private int getNewMode(){
        List<ScanResult> wifiList = wifiManager.getScanResults();
        if(startedByService){
            wifiManager.setWifiEnabled(false);
        }
        HashMap<String,String> availableMap = new HashMap<>();
        String str;
        for(int i = 0; i < wifiList.size(); i++){
            str = wifiList.get(i).SSID;
            availableMap.put(str,str);
        }
        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);
        Set<String> silentList = preferences.getStringSet(Constants.SILENT_LIST, null);
        if (silentList == null) {
            silentList = new HashSet<>();
        }
        for (String wifi : silentList) {
            if (availableMap.containsKey(wifi)) {
                return AudioManager.RINGER_MODE_SILENT;
            }
        }
        Set<String> vibrationList = preferences.getStringSet(Constants.VIBRATION_LIST, null);
        if (vibrationList == null) {
            vibrationList = new HashSet<>();
        }
        for (String wifi : vibrationList) {
            if (availableMap.containsKey(wifi)) {
                return AudioManager.RINGER_MODE_VIBRATE;
            }
        }
        return AudioManager.RINGER_MODE_NORMAL;

    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {
        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);
        boolean auto = preferences.getBoolean(Constants.AUTO_CONTROL_STR,false);
        if(auto)
        {
            if(wifiManager.isWifiEnabled())unregisterReceiver(receiverWifi);
            if(startedByService){
                wifiManager.setWifiEnabled(false);
            }
            AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarm.set(
                    alarm.RTC_WAKEUP,
                    System.currentTimeMillis() + (1000 * 60),
                    PendingIntent.getService(this, 0, new Intent(this, MyService.class), 0)
            );
        }
    }
}

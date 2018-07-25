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
import java.util.Set;

/**
 * Created by Sodrul Amin Shaon on 22-Jun-18.
 */

public class MyService extends Service {
    public static HashMap<String,String> availableMap = new HashMap<>();
    public WifiManager wifiManager;
    WifiReceiver receiverWifi;
    public static final String LIST_TESTING = "ListTest";
    //public boolean wiifEnabledByService = true;
    MyService service;
    private long serviceRunningTime = 10000;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        service = this;
        new WifiCheckerThread().start();
        Log.i(LIST_TESTING,"MyService onStartCommand has been called");
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
                    Log.i(LIST_TESTING,"Wifi is enabled now service is going to check the available wifi list.");
                    if (!wifiManager.isWifiEnabled()) {
                        WifiReceiver.disableWifi = true;
                        wifiManager.setWifiEnabled(true);
                    } //else wiifEnabledByService = false;
                    //Log.i(LIST_TESTING,"Wifi is enabled. Now going to check the available list.");
                    receiverWifi = new WifiReceiver();
                    receiverWifi.service = service;
                    //if(!isRegistered(receiverWifi))
                    Log.i(LIST_TESTING,"Going to register scan receiver");
                    try{
                        service.registerReceiver(receiverWifi,
                                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                    }catch (Exception e){
                        Log.i(LIST_TESTING,e.toString());
                        e.printStackTrace();
                    }
                    Log.i(LIST_TESTING,"Going to start the scan");
                    wifiManager.startScan();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                Thread.sleep(serviceRunningTime);
            }catch (Exception e){}

            try {
                Log.i(LIST_TESTING,"Going to check the mode now");
                int mode = getNewMode();
                Log.i(MyService.LIST_TESTING,"Mode found in broadcast Receiver is "+mode);
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.setRingerMode(mode);
                if(HomeTab.homeTab != null)HomeTab.changeMode(mode);
            }catch (Exception e){
                e.printStackTrace();
            }
            service.stopSelf();
        }
    }

    /*public class WifiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context c, Intent intent) {
            try {
                Log.i(LIST_TESTING,"inside broadcast receiver....");
                int mode = getNewMode();
                Log.i(LIST_TESTING,"Mode found in broadcast Receiver is "+mode);
                AudioManager am = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
                am.setRingerMode(mode);
                if(HomeTab.homeTab != null)HomeTab.changeMode(mode);
            }catch (Exception e){
                e.printStackTrace();
            }

            stopSelf();
        }
    }*/
    private int getNewMode(){
        /*List<ScanResult> wifiList = wifiManager.getScanResults();
        if(wiifEnabledByService){
            wifiManager.setWifiEnabled(false);
        }
        HashMap<String,String> availableMap = new HashMap<>();
        String str;
        for(int i = 0; i < wifiList.size(); i++){
            str = wifiList.get(i).SSID;
            availableMap.put(str,str);
            Log.i(LIST_TESTING,(i+1)+". "+str);
        }*/
        //if(WifiReceiver.availableMap == null)return AudioManager.RINGER_MODE_NORMAL;
        //HashMap<String,String> availableMap =WifiReceiver.availableMap;
        Log.i(LIST_TESTING,"Inside getmode method. Avilable map size: "+availableMap.size());
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
            try {
                unregisterReceiver(receiverWifi);
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                if(WifiReceiver.disableWifi){
                    WifiReceiver.disableWifi = false;
                    wifiManager.setWifiEnabled(false);
                }
            }catch (Exception e){}
            AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarm.set(
                    alarm.RTC_WAKEUP,
                    System.currentTimeMillis() + (1000 * 50),
                    PendingIntent.getService(this, 0, new Intent(this, MyService.class), 0)
            );
        }
    }
}

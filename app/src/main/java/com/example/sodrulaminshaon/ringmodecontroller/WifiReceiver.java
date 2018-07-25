package com.example.sodrulaminshaon.ringmodecontroller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Sodrul Amin Shaon on 11-Jul-18.
 */

public class WifiReceiver extends BroadcastReceiver {

    public static boolean disableWifi = false;
    @Override
    public void onReceive(Context c, Intent intent) {
        if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
            List<ScanResult> wifiList = service.wifiManager.getScanResults();
            Log.i(MyService.LIST_TESTING,"Inside wifi scan receiver size = "+wifiList.size());
            MyService.availableMap.clear();
            String str;
            for(int i = 0; i < wifiList.size(); i++){
                str = wifiList.get(i).SSID;
                if(str != null && str.length()>0){
                    MyService.availableMap.put(str,str);
                    Log.i(MyService.LIST_TESTING,(i+1)+". "+str);
                }
            }
            if(disableWifi){
                service.wifiManager.setWifiEnabled(false);
                disableWifi = false;
            }
        }
    }
    /*@Override
    public void onReceive(Context c, Intent intent) {



        List<ScanResult> wifiList = service.wifiManager.getScanResults();
        Log.i(MyService.LIST_TESTING,"Inside wifi scan receiver size = "+wifiList.size());
        MyService.availableMap.clear();
        String str;
        for(int i = 0; i < wifiList.size(); i++){
            str = wifiList.get(i).SSID;
            if(str != null && str.length()>0){
                MyService.availableMap.put(str,str);
                Log.i(MyService.LIST_TESTING,(i+1)+". "+str);
            }
        }
        if(disableWifi){
            service.wifiManager.setWifiEnabled(false);
            disableWifi = false;
        }

        *//*try {
            Log.i(MyService.LIST_TESTING,"inside broadcast receiver....");
            int mode = getNewMode();
            Log.i(MyService.LIST_TESTING,"Mode found in broadcast Receiver is "+mode);
            AudioManager am = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
            am.setRingerMode(mode);
            if(HomeTab.homeTab != null)HomeTab.changeMode(mode);
        }catch (Exception e){
            e.printStackTrace();
        }

        service.stopSelf();*//*
    }*/
    public MyService service;

    /*private int getNewMode(){
        List<ScanResult> wifiList = service.wifiManager.getScanResults();
        if(service.wiifEnabledByService){
            service.wifiManager.setWifiEnabled(false);
        }
        HashMap<String,String> availableMap = new HashMap<>();
        String str;
        for(int i = 0; i < wifiList.size(); i++){
            str = wifiList.get(i).SSID;
            availableMap.put(str,str);
            Log.i(MyService.LIST_TESTING,(i+1)+". "+str);
        }
        SharedPreferences preferences = service.getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);
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

    }*/
}

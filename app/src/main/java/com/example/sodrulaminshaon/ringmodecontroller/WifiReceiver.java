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


    public static String logger = "MyService";
    public boolean done;
    @Override
    public void onReceive(Context c, Intent intent) {
        if (intent != null && intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
            try {

                if(MyService.wifiManager.isWifiEnabled()) {
                    List<ScanResult> wifiList = MyService.wifiManager.getScanResults();
                    //Log.i(logger, "Inside wifi scan receiver size = " + wifiList.size());
                    MyService.availableMap.clear();
                    String str;
                    for (int i = 0; i < wifiList.size(); i++) {
                        str = wifiList.get(i).SSID;
                        if (str != null && str.length() > 0) {
                            MyService.availableMap.put(str, str);
                            Log.i(logger, (i + 1) + ". " + str);
                        }
                    }

                }

            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                while (MyService.disableWifi && MyService.wifiManager.isWifiEnabled()) {
                    MyService.wifiManager.setWifiEnabled(false);
                    Thread.sleep(200);
                    Log.i(logger,"Wifi is disabled now from receiver");
                    MyService.log(logger +" Wifi is disabled now from receiver");
                }
                MyService.disableWifi = false;
                done = true;
            }catch (Exception e){
                MyService.log(logger +" Exception while disabling wifi..",e);
            }
        }
    }
    public MyService service;

}

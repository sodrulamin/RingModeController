package com.example.sodrulaminshaon.ringmodecontroller;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Sodrul Amin Shaon on 22-Jun-18.
 */

public class MyService extends Service {
    public static HashMap<String,String> availableMap = new HashMap<>();
    public static WifiManager wifiManager;
    //public LinkedBlockingDeque<WifiManager> registeredWifiManagerQueue;
    WifiReceiver receiverWifi;
    public static final String logger = "MyService",logFileName = "events.log";
    //public boolean wiifEnabledByService = true;
    MyService service;
    private long serviceRunningTime = 5000;
    public Context context;
    public static boolean logEvents = false;
    public static boolean disableWifi = false;
    private static String folder_main = "Ring Mode Controller";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //registeredWifiManagerQueue = new LinkedBlockingDeque<>();
        service = this;
        context = getApplicationContext();
        new WifiCheckerThread().start();
        if(logEvents){
            //writeFileOnInternalStorage(context,logFileName,logger+"MyService onStartCommand has been called");
            log(logger+" MyService onStartCommand has been called");
        }
            //Log.i(logger,"MyService onStartCommand has been called");
        return START_STICKY;
    }
    private class WifiCheckerThread extends Thread{
        @Override
        public void run(){
            boolean auto;
            try {
                SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
                auto = preferences.getBoolean(Constants.AUTO_CONTROL_STR, false);
                Log.i(logger,"Auto: "+auto);
                if (auto) {
                    try{
                        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
                        alarm.set(alarm.RTC_WAKEUP,
                                System.currentTimeMillis() + (1000 * 60 * getIntervalTime()),
                                PendingIntent.getService(service, 0, new Intent(service, MyService.class), 0)
                        );
                    }catch (Exception e){}

                    wifiManager = (WifiManager) service.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    //Log.i(logger,"Wifi is enabled now service is going to check the available wifi list.");
                    if (!wifiManager.isWifiEnabled()) {
                        disableWifi = true;
                        wifiManager.setWifiEnabled(true);
                        if(logEvents){
                            //writeFileOnInternalStorage(context,logFileName,logger+"Wifi is enabled now service is going to check the available wifi list.");
                            log(logger+" Wifi is enabled now service is going to check the available wifi list.");
                        }
                        Log.i(logger,"Wifi is enabled now service is going to check the available wifi list.");
                    } //else wiifEnabledByService = false;
                    else{
                        log(logger+" Wifi was enabled by default.");
                    }
                    //Log.i(logger,"Wifi is enabled. Now going to check the available list.");
                    receiverWifi = new WifiReceiver();
                    receiverWifi.service = service;
                    receiverWifi.done = false;
                    //if(!isRegistered(receiverWifi))
                    //Log.i(logger,"Going to register scan receiver");
                    try{
                        service.registerReceiver(receiverWifi,
                                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                    }catch (Exception e){
                        Log.i(logger,e.toString());
                        e.printStackTrace();
                    }
                    //Log.i(logger,"Going to start the scan");
                    wifiManager.startScan();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                long statTime = System.currentTimeMillis();
                while(System.currentTimeMillis() < statTime+serviceRunningTime && !receiverWifi.done){
                    Thread.sleep(100);
                }
            }catch (Exception e){}

            if(wifiManager != null && wifiManager.isWifiEnabled())
            {
                try {
                    if(!receiverWifi.done) {
                        List<ScanResult> wifiList = service.wifiManager.getScanResults();
                        //Log.i(MyService.logger, "Inside MyService receiver size = " + wifiList.size());
                        MyService.availableMap.clear();
                        String str;
                        for (int i = 0; i < wifiList.size(); i++) {
                            str = wifiList.get(i).SSID;
                            if (str != null && str.length() > 0) {
                                MyService.availableMap.put(str, str);
                                Log.i(MyService.logger, (i + 1) + ". " + str);
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    //writeFileOnInternalStorage(context,logFileName," Available map is cleared but could not refill with new wifi. "+e.toString());
                    log(" Available map is cleared but could not refill with new wifi. ",e);
                }

                try{
                    if(disableWifi){
                        while(wifiManager.isWifiEnabled()) {
                            wifiManager.setWifiEnabled(false);
                            Thread.sleep(200);
                        }
                        disableWifi = false;
                        Log.i(logger,"Wifi is disabled now");
                        if(logEvents){
                            //writeFileOnInternalStorage(context,logFileName,logger+"Wifi is disabled now");
                            log(logger+" Wifi is disabled now");
                        }
                    }
                }catch (Exception e){
                    Log.i(logger,"Error!! Could not disable wifi"+e.toString());
                    if(logEvents){
                        //writeFileOnInternalStorage(context,logFileName,logger+"Error!! Could not disable wifi"+e.toString());
                        log(logger+" Error!! Could not disable wifi",e);
                    }
                }

            }


            try {
                int mode = getNewMode();
                Log.i(MyService.logger," Mode found in broadcast Receiver is "+mode);
                log(logger+" Mode found in broadcast Receiver is "+mode);
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.setRingerMode(mode);
                if(HomeTab.homeTab != null)HomeTab.changeMode(mode);
            }catch (Exception e){
                e.printStackTrace();
                //writeFileOnInternalStorage(context,logFileName,"Could not change mode successfully, got errot at change mode "+e.toString());
                log(logger+" Could not change mode successfully, got error at change mode ",e);
            }
            service.stopSelf();
        }
    }
    private int getNewMode(){
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
    public static void log(String str){
        File folder = new File(Environment.getExternalStorageDirectory(),folder_main);
        if(!folder.exists())folder.mkdirs();
        File file = new File(folder,logFileName);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream fileinput = new FileOutputStream(file,true);
            PrintStream printstream = new PrintStream(fileinput);
            long time = System.currentTimeMillis();
            Date date = new Date(time);
            @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss,SSS");
            formatter.setTimeZone(TimeZone.getTimeZone("Asia/Dhaka"));
            String dateFormatted = formatter.format(date);
            str = dateFormatted + " "+str+"\n";
            printstream.print(str);
            fileinput.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static void log(String str,Exception exception){
        File folder = new File(Environment.getExternalStorageDirectory(),folder_main);
        if(!folder.exists())folder.mkdirs();
        File file = new File(folder,logFileName);
        if(!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {}
        try {
            FileOutputStream fileinput = new FileOutputStream(file,true);
            PrintStream printstream = new PrintStream(fileinput);
            long time = System.currentTimeMillis();
            Date date = new Date(time);
            @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss,SSS");
            formatter.setTimeZone(TimeZone.getTimeZone("Asia/Dhaka"));
            String dateFormatted = formatter.format(date);
            str = dateFormatted + " "+str+" "+exception.toString()+getStackTraceString(exception.getStackTrace())+"\n";
            printstream.print(str);
            fileinput.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static String getStackTraceString(StackTraceElement [] s){
        if(s == null)return "";
        String result = "";
        for(int i=1;i<s.length;i++){
            result = result +"\n\tat "+ s[i].getClassName()+"."+s[i].
                    getMethodName()+"("+s[i].getFileName()+":"+s[i].
                    getLineNumber()+")";
        }
        return result;
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
                if(disableWifi){
                    while(wifiManager.isWifiEnabled()) {
                        wifiManager.setWifiEnabled(false);
                        Thread.sleep(200);
                    }
                    disableWifi = false;
                }
            }catch (Exception e){
                e.printStackTrace();
                //writeFileOnInternalStorage(context,logFileName,"Exception while disabling wifi at on destroy method."+e.toString());
                log("Exception while disabling wifi at on destroy method.",e);
            }
            /*AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarm.set(alarm.RTC_WAKEUP,
                    System.currentTimeMillis() + (1000 * 60 * getIntervalTime()),
                    PendingIntent.getService(this, 0, new Intent(this, MyService.class), 0)
            );*/
        }
    }
    public int getIntervalTime(){
        SharedPreferences mPrefs = getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);
        int interval = mPrefs.getInt(Constants.INTERVAL,1);
        return interval;
    }
}

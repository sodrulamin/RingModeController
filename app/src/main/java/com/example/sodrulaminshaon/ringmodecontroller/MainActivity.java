package com.example.sodrulaminshaon.ringmodecontroller;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    public static TabLayout tabLayout;
    public static AudioManager am;
    public static MainActivity activity;
    private static Intent serviceIntent;
    private final static int LOCATION_PERMISSION_REQUEST_CODE = 120;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        getPermission();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        /*tabLayout.getTabAt(0).setIcon(R.drawable.image_name_1);
        tabLayout.getTabAt(1).setIcon(R.drawable.image_name_2);  ////// this is for icon beside tab name
        tabLayout.getTabAt(2).setIcon(R.drawable.image_name_3);*/

        tabLayout.getTabAt(0).setText("Manual");
        tabLayout.getTabAt(1).setText("Silent");
        tabLayout.getTabAt(2).setText("Vibration");
        //System.out.println("Test");
        am= (AudioManager) activity.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);


        serviceIntent = new Intent(activity, MyService.class);
        startService();
    }
    public static void startService(){
        activity.getApplicationContext().startService(serviceIntent);
    }
    public static void stopService(){
        activity.getApplicationContext().stopService(serviceIntent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(activity,ActionSettingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.getApplicationContext().startActivity(intent);
            return true;
        }else if(id == R.id.action_about){
            Intent intent = new Intent(activity,ActionAboutActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.getApplicationContext().startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    HomeTab tab1 = new HomeTab();
                    return tab1;
                case 1:
                    SilentTab tab2 = new SilentTab();
                    return tab2;
                case 2:
                    VibrationTab tab3 = new VibrationTab();
                    return tab3;
            }
            return null;
        }
        @Override
        public CharSequence getPageTitle(int position){
            switch (position){
                case 0:
                    return "Manual";
                case 1:
                    return "Silent";
                case 2:
                    return "Vibrate";
            }
            return null;
        }
        @Override
        public int getCount() {
            return 3;
        }
    }
    public static boolean removeWifiFromList(String windowName, String removableString){
        SharedPreferences mPrefs = activity.getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Set<String> tempwifiList = mPrefs.getStringSet(windowName,null);
        if(tempwifiList == null){
            return false;
        }
        Set<String> wifiList = new HashSet<>(tempwifiList);
        boolean result = wifiList.remove(removableString);
        prefsEditor.putStringSet(windowName,wifiList);

        prefsEditor.commit();
        prefsEditor.apply();
        return result;
    }
    public static boolean addWifiToTheList(String windowName, String wifiName){
        SharedPreferences mPrefs = activity.getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Set<String> tempwifiList = mPrefs.getStringSet(windowName,null);
        Set<String> wifiList = new HashSet<>();
        if(tempwifiList != null){
            wifiList = new HashSet<>(tempwifiList);
        }
        boolean result = wifiList.add(wifiName);
        prefsEditor.putStringSet(windowName,wifiList);

        prefsEditor.commit();
        prefsEditor.apply();
        return result;
    }
    public static boolean addWifiToTheList(String windowName, ArrayList<String> wifiListInput){
        SharedPreferences mPrefs = activity.getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Set<String> wifiList = new HashSet<>();
        if(wifiListInput != null){
            wifiList = new HashSet<>(wifiListInput);
        }
        prefsEditor.putStringSet(windowName,wifiList);

        prefsEditor.commit();
        prefsEditor.apply();
        return true;
    }
    public static HashSet<String> getWifiList(String windowName){
        SharedPreferences mPrefs = activity.getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);
        Set<String> wifiList = mPrefs.getStringSet(windowName,null);
        if(wifiList == null){
            wifiList = new HashSet<>();
        }
        return (HashSet)wifiList;
    }
    public static boolean getAutoControl(){
        SharedPreferences mPrefs = activity.getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);
        boolean autoControl = mPrefs.getBoolean(Constants.AUTO_CONTROL_STR,false);
        return autoControl;
    }
    public static void setAutoControl(Boolean flag){
        SharedPreferences mPrefs = activity.getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putBoolean(Constants.AUTO_CONTROL_STR,flag);
        prefsEditor.commit();
        HomeTab.changeButtonStatus(!flag);
        if(flag) startService();
        else stopService();
    }
    public static int getIntervalTime(){
        SharedPreferences mPrefs = activity.getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);
        int interval = mPrefs.getInt(Constants.INTERVAL,1);
        return interval;
    }
    public static void setIntervalTime(int minute){
        SharedPreferences mPrefs = activity.getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putInt(Constants.INTERVAL,minute);
        prefsEditor.commit();
    }

    private void getPermission(){
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            askForLocationPermissions();
        }
    }
    private void askForLocationPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            new android.support.v7.app.AlertDialog.Builder(this)
                    .setTitle("Location permessions needed")
                    .setMessage("you need to allow this permission!")
                    .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    LOCATION_PERMISSION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {}
                    })
                    .show();


        } else {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
                } else {
                    Toast.makeText(this, "Can not proceed! i need permission" , Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    public static boolean isPermissionGranted(@NonNull String[] grantPermissions, @NonNull int[] grantResults,
                                              @NonNull String permission) {
        for (int i = 0; i < grantPermissions.length; i++) {
            if (permission.equals(grantPermissions[i])) {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED;
            }
        }
        return false;
    }
}

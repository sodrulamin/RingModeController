package com.example.sodrulaminshaon.ringmodecontroller;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Sodrul Amin Shaon on 11-Jun-18.
 */

public class HomeTab extends Fragment {
    private static Button normal,vibrate,silent;
    public static ConstraintLayout backGround;
    private static final int col = 100;
    public static CheckBox autoControl;
    public static HomeTab homeTab;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_tab, container, false);

        normal = (Button) rootView.findViewById(R.id.normal);
        vibrate = (Button) rootView.findViewById(R.id.vibrate);
        silent = (Button) rootView.findViewById(R.id.silent);
        autoControl = (CheckBox) rootView.findViewById(R.id.checkbox);



        homeTab = this;
        backGround =  rootView.findViewById(R.id.homelayout);
        changeMode(MainActivity.am.getRingerMode());
        backGround.setBackgroundColor(Color.CYAN);
        backGround.setBackground(getResources().getDrawable(R.drawable.background2));

        /*final SharedPreferences mPrefs = getActivity().getPreferences(MODE_PRIVATE);
        final boolean autoControlMode = mPrefs.getBoolean(Constants.AUTO_CONTROL_STR,false);*/
        boolean autoControlMode = MainActivity.getAutoControl();
        autoControl.setChecked(autoControlMode);

        //final SharedPreferences.Editor prefsEditor = mPrefs.edit();


        vibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeMode(AudioManager.RINGER_MODE_VIBRATE);
            }
        });
        silent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeMode(AudioManager.RINGER_MODE_SILENT);
            }
        });
        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeMode(AudioManager.RINGER_MODE_NORMAL);
            }
        });
        autoControl.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                /*prefsEditor.putBoolean(Constants.AUTO_CONTROL_STR,autoControl.isChecked());
                prefsEditor.commit();
                changeButtonStatus(!autoControl.isChecked());*/
                setAutoControl(autoControl.isChecked());
                MainActivity.setAutoControl(autoControl.isChecked());
            }
        });
        changeButtonStatus(!autoControlMode);
        return rootView;
    }
    public static void changeButtonStatus(boolean isClickable){
        normal.setClickable(isClickable);
        vibrate.setClickable(isClickable);
        silent.setClickable(isClickable);
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void changeMode(int index){
        NotificationManager notificationManager =
                (NotificationManager) homeTab.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);


            homeTab.startActivity(intent);
        }

        MainActivity.am.setRingerMode(index);
        switch (index){
            case AudioManager.RINGER_MODE_NORMAL:
                normal.setBackgroundColor(Color.rgb(col,col,col));
                silent.setBackgroundColor(Color.WHITE);
                vibrate.setBackgroundColor(Color.WHITE);
                MainActivity.tabLayout.setBackgroundColor(Color.MAGENTA);
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                vibrate.setBackgroundColor(Color.rgb(col,col,col));
                normal.setBackgroundColor(Color.WHITE);
                silent.setBackgroundColor(Color.WHITE);
                MainActivity.tabLayout.setBackgroundColor(Color.BLUE);
                break;
            case AudioManager.RINGER_MODE_SILENT:
                silent.setBackgroundColor(Color.rgb(col,col,col));
                normal.setBackgroundColor(Color.WHITE);
                vibrate.setBackgroundColor(Color.WHITE);
                MainActivity.tabLayout.setBackgroundColor(Color.BLACK);
                break;
        }
    }
    public void setAutoControl(Boolean flag){
        SharedPreferences preferences = getActivity().getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.putBoolean(Constants.AUTO_CONTROL_STR,flag);
        prefEditor.commit();
    }


}

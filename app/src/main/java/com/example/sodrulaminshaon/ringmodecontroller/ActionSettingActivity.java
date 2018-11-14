package com.example.sodrulaminshaon.ringmodecontroller;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

public class ActionSettingActivity extends AppCompatActivity {
    Spinner intervalTime;
    public static ConstraintLayout backGround;
    CheckBox logEventBox;
    int REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_setting_page);
        backGround =  findViewById(R.id.setting_page);
        backGround.setBackground(getResources().getDrawable(R.drawable.background2));
        logEventBox = findViewById(R.id.checkBox);

        intervalTime = findViewById(R.id.spinner);
        int aa = MainActivity.getIntervalTime();
        intervalTime.setSelection(getSpinnerPosition(aa));
        logEventBox.setChecked(MainActivity.getLogEvent());

        Button save = findViewById(R.id.button2);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int interval = Integer.parseInt(intervalTime.getSelectedItem().toString());
                MainActivity.setIntervalTime(interval);
                MainActivity.setLogEvent(logEventBox.isChecked());
                MyService.logEvents = logEventBox.isChecked();
                if(logEventBox.isChecked()){
                    checkPermission();
                }
                finish();
            }
        });

        Button cancel = findViewById(R.id.button3);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    private boolean checkPermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(MyService.logger,"Permission is granted");
                return true;
            } else {

                Log.v(MyService.logger,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(MyService.logger,"Permission is granted");
            return true;
        }

    }
    private int getSpinnerPosition(int value){
        int n = intervalTime.getCount();
        for(int i=0;i<n;i++){
            if(intervalTime.getItemAtPosition(i).toString().equals(""+value))return i;
        }
        return 0;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(MyService.logger,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }
}

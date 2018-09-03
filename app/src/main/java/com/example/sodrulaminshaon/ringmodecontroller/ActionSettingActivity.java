package com.example.sodrulaminshaon.ringmodecontroller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class ActionSettingActivity extends AppCompatActivity {
    Spinner intervalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_setting_page);

        intervalTime = findViewById(R.id.spinner);
        int aa = MainActivity.getIntervalTime();
        intervalTime.setSelection(getSpinnerPosition(aa));

        Button save = findViewById(R.id.button2);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int interval = Integer.parseInt(intervalTime.getSelectedItem().toString());
                MainActivity.setIntervalTime(interval);
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

    private int getSpinnerPosition(int value){
        int n = intervalTime.getCount();
        for(int i=0;i<n;i++){
            if(intervalTime.getItemAtPosition(i).toString().equals(""+value))return i;
        }
        return 0;
    }
}

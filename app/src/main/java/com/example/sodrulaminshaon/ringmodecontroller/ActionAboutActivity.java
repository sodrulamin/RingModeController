package com.example.sodrulaminshaon.ringmodecontroller;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActionAboutActivity extends AppCompatActivity {
    public static ConstraintLayout backGround;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_about_page);

        backGround =  findViewById(R.id.about_page);
        backGround.setBackground(getResources().getDrawable(R.drawable.background2));

        TextView versionView = findViewById(R.id.version_view);

        versionView.setText("version: "+BuildConfig.VERSION_NAME);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

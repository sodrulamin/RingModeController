package com.example.sodrulaminshaon.ringmodecontroller;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by Sodrul Amin Shaon on 21-Jul-18.
 */

public class AvailableWifiListView extends AppCompatActivity {
    private String windowName;
    private ArrayList<String> selectedList = new ArrayList<String>();
    private Button addButton,cancelButton;
    AvailableWifiViewAdapter listAddapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.available_wifi_list);
        addButton = (Button)findViewById(R.id.add);
        cancelButton = (Button)findViewById(R.id.cancel);
        listAddapter = new AvailableWifiViewAdapter(this);
        windowName = getIntent().getStringExtra(Constants.WINDOW_NAME);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }




    public class AvailableWifiViewAdapter extends BaseAdapter{
        Context c;
        ArrayList<String> listOfWifi;
        LayoutInflater inflater;
        Context context;

        public AvailableWifiViewAdapter(Context c){
            context = c;
        }

        @Override
        public int getCount() {
            return listOfWifi.size();
        }

        @Override
        public Object getItem(int i) {
            return listOfWifi.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            if(inflater==null)
            {
                inflater= (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            if(convertView==null)
            {
                convertView=inflater.inflate(R.layout.wifi_list_item,parent,false);
            }

            return null;
        }
    }
}

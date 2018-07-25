package com.example.sodrulaminshaon.ringmodecontroller;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Sodrul Amin Shaon on 21-Jul-18.
 */

public class AvailableWifiListView extends AppCompatActivity {
    private String windowName;
    private ArrayList<String> selectedList = new ArrayList<String>();
    private Button addButton,cancelButton;
    AvailableWifiViewAdapter listAddapter;
    private ListView listView;
    ConstraintLayout backGround;
    private EditText selectedWifi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.available_wifi_list);
        backGround = findViewById(R.id.background);
        backGround.setBackground(getResources().getDrawable(R.drawable.background2));
        selectedWifi = (EditText)findViewById(R.id.editText);
        listView = (ListView)findViewById(R.id.wifiListView);
        addButton = (Button)findViewById(R.id.add);
        cancelButton = (Button)findViewById(R.id.cancel);
        listAddapter = new AvailableWifiViewAdapter(this);
        listView.setAdapter(listAddapter);
        windowName = getIntent().getStringExtra(Constants.WINDOW_NAME);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = selectedWifi.getText().toString();
                if(str != null && str.length()>0){
                    MainActivity.addWifiToTheList(windowName,str);
                    if(windowName.equals(Constants.VIBRATION_LIST)){
                        VibrationTab.adapter.refreshAdapter();
                    }else if(windowName.equals(Constants.SILENT_LIST)){
                        SilentTab.adapter.refreshAdapter();
                    }
                }
                finish();
            }
        });
        selectedWifi.requestFocus();
        selectedWifi.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s != null && s.length() > 0)
                {
                    listAddapter.listOfWifi.clear();
                    for(String str: listAddapter.baseList){
                        if(str.toLowerCase().contains(s.toString().toLowerCase())){
                            listAddapter.listOfWifi.add(str);
                        }
                    }
                    Log.i(MyService.LIST_TESTING,"Current text = "+s);
                }else{
                    listAddapter.listOfWifi.clear();
                    for(String str: listAddapter.baseList){
                        //if(str.toLowerCase().contains(s.toString().toLowerCase()))
                        {
                            listAddapter.listOfWifi.add(str);
                        }
                    }

                }
                listAddapter.refreshAdapter();
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
        //Context c;
        public ArrayList<String> listOfWifi,baseList;
        LayoutInflater inflater;
        Context context;
        public AvailableWifiViewAdapter(Context c){
            context = c;
            listOfWifi = new ArrayList<>();
            baseList = new ArrayList<>();
            try {
                Set<String> keySet = MyService.availableMap.keySet();
                for (String str : keySet){
                    listOfWifi.add(str);
                    baseList.add(str);
                }

            }catch (Exception e){}
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
                inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            if(convertView==null)
            {
                convertView=inflater.inflate(R.layout.wifi_list_item,parent,false);
            }

            TextView nameTxt= (TextView) convertView.findViewById(R.id.name);
            final String name= listOfWifi.get(i);
            nameTxt.setText(name);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedWifi.setText(name);
                }
            });

            return convertView;
        }
        public void refreshAdapter(){
            notifyDataSetChanged();
        }
    }
}

package com.example.sodrulaminshaon.ringmodecontroller;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Sodrul Amin Shaon on 11-Jun-18.
 */

public class SilentTab extends Fragment {
    public static RelativeLayout backGround;
    //public static ArrayList<String> wifiList;
    public static SearchView searchView;
    public static ListView lv;
    public static ImageButton addWifi;
    //public static String popupInput;
    public static CustomAdapter  adapter;

    /*static{
        wifiList = new ArrayList<>();
    }*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.wifi_list_view, container, false);
        backGround =  rootView.findViewById(R.id.commonListLayout);
        searchView = rootView.findViewById(R.id.searchView);
        addWifi = rootView.findViewById(R.id.add);


        backGround.setBackgroundColor(Color.CYAN);
        backGround.setBackground(getResources().getDrawable(R.drawable.background2));
        /*wifiList.clear();
        wifiList.add("a2");
        wifiList.add("test1");
        wifiList.add("This_is_long_text_to_see_if_it_gets_multiline_in_textview._So_that_the_wifi_name_len_can_be_long");
        wifiList.add("SilentOn");
        wifiList.add("ClassRoom");
        wifiList.add("This is long text to see if it gets multiline in textview. So that the wifi name len can be long");
        String[] array = new String[wifiList.size()];
        for(int i=0;i<wifiList.size();i++){
            array[i]=wifiList.get(i);
        }*/
        //HashSet<String> wifiList = MainActivity.getWifiList(Constants.SILENT_LIST);
        lv= (ListView) rootView.findViewById(R.id.wifiListView);
        //addap = new ArrayAdapter<String>(getContext(), R.layout.wifi_list_item, R.id.name, array);
        //addap = new ArrayAdapter<String>(getContext(), R.layout.simple_list_item, R.id.name, array);
        adapter = new CustomAdapter(getContext(),Constants.SILENT_LIST);
        //lv.setAdapter(addap);
        lv.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        addWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AvailableWifiListView.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(Constants.WINDOW_NAME,Constants.SILENT_LIST);
                getActivity().startActivity(intent);
                //popupInput = null;
                /*AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add New Wifi");
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String inputString = input.getText().toString();
                        if(inputString != null && inputString.length()>0) {
                            MainActivity.addWifiToTheList(Constants.SILENT_LIST, inputString);
                            adapter.refreshAdapter();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();*/
            }
        });

        return rootView;
    }

}

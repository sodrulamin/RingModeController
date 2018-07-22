package com.example.sodrulaminshaon.ringmodecontroller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Sodrul Amin Shaon on 11-Jun-18.
 */

public class VibrationTab extends Fragment {
    public static RelativeLayout backGround;
    public static SearchView searchView;
    public static ListView lv;
    public static ImageButton addWifi;
    CustomAdapter  adapter;
    /*public static ArrayList<String> wifiList;
    static{
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
        //Set<String> wifiList = MainActivity.getWifiList(Constants.VIBRATION_LIST);

        /*wifiList.clear();
        for(int i=0;i<20;i++)
            wifiList.add("ReveSystems_"+i);*/

        lv= (ListView) rootView.findViewById(R.id.wifiListView);
        adapter = new CustomAdapter(getContext(),Constants.VIBRATION_LIST);
        lv.setAdapter(adapter);

        /*final View parent = (View) searchView.getParent();  // button: the view you want to enlarge hit area
        parent.post( new Runnable() {
            public void run() {
                final Rect rect = new Rect();
                searchView.getHitRect(rect);
                rect.left -= 10;   // increase left hit area
                rect.right += 100;  // increase right hit area
                parent.setTouchDelegate( new TouchDelegate( rect , searchView));
            }
        });*/
        searchView.setQueryHint("Search");
        searchView.setVisibility(View.VISIBLE);
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
                intent.putExtra(Constants.WINDOW_NAME,Constants.VIBRATION_LIST);
                getActivity().startActivity(intent);

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
                            MainActivity.addWifiToTheList(Constants.VIBRATION_LIST, inputString);
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

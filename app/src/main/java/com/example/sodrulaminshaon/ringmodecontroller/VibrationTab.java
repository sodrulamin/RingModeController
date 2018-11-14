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
    public static CustomAdapter  adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.wifi_list_view, container, false);
        backGround =  rootView.findViewById(R.id.commonListLayout);
        searchView = rootView.findViewById(R.id.searchView);
        addWifi = rootView.findViewById(R.id.add);

        backGround.setBackground(getResources().getDrawable(R.drawable.background2));

        lv= (ListView) rootView.findViewById(R.id.wifiListView);
        adapter = new CustomAdapter(getContext(),Constants.VIBRATION_LIST);
        lv.setAdapter(adapter);
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

            }
        });

        return rootView;
    }


}

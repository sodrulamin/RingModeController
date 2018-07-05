package com.example.sodrulaminshaon.ringmodecontroller;

/**
 * Created by Sodrul Amin Shaon on 25-Jun-18.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashSet;

public class CustomAdapter extends BaseAdapter implements Filterable {

    Context c;
    ArrayList<String> listOfWifi,filteredData;
    LayoutInflater inflater;
    private ItemFilter mFilter = new ItemFilter();
    private String windowName;


    public CustomAdapter(Context c, String windowName) {
        this.c = c;
        HashSet<String> tvShows = MainActivity.getWifiList(windowName);
        this.listOfWifi = new ArrayList<String>();
        for(String str: tvShows)
            this.listOfWifi.add(str);
        this.filteredData = this.listOfWifi;
        this.windowName = windowName;
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if(inflater==null)
        {
            inflater= (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.simple_list_item,parent,false);
        }

        TextView nameTxt= (TextView) convertView.findViewById(R.id.name);
        final String name= filteredData.get(position);
        ImageButton button = (ImageButton) convertView.findViewById(R.id.delete);
        button.setBackgroundColor(Color.RED);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                builder.setTitle("Delete Operation");
                final TextView message = new TextView(parent.getContext());
                String str = "  Want to delete \""+name+"\" from the list?";
                message.setText(str);
                builder.setView(message);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ///do whatever need to do to delete a wifi from the list
                        MainActivity.removeWifiFromList(windowName,name);
                        //refreshAdapter();
                        filteredData.remove(name);
                        listOfWifi.remove(name);
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        nameTxt.setText(name);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(c);
                builder.setTitle("Add New Wifi");
                final EditText input = new EditText(c);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText(name);
                input.selectAll();
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String inputString = input.getText().toString();
                        if(inputString != null && inputString.length()>0) {
                            for(int i=0;i<listOfWifi.size();i++){
                                if(name.equals(listOfWifi.get(i))){
                                    listOfWifi.remove(i);
                                    listOfWifi.add(i,inputString);
                                    break;
                                }
                            }
                            filteredData.remove(position);
                            filteredData.add(position,inputString);
                            MainActivity.addWifiToTheList(windowName,listOfWifi);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        return convertView;
    }
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();
            filteredData = new ArrayList<>();
            for(String str: listOfWifi){
                if(str.toLowerCase().contains(filterString)){
                    filteredData.add(str);
                }
            }

            results.values = filteredData;
            results.count = filteredData.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notifyDataSetChanged();
        }

    }
    public void refreshAdapter(){
        HashSet<String> tvShows = MainActivity.getWifiList(windowName);
        this.listOfWifi = new ArrayList<String>();
        for(String str: tvShows)
            this.listOfWifi.add(str);
        this.filteredData = this.listOfWifi;
        notifyDataSetChanged();
    }
    public Filter getFilter() {
        return mFilter;
    }


}

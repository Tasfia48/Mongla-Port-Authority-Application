package com.example.nur.monglaport.Class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nur.monglaport.R;
import com.example.nur.monglaport.UI.Containers;

import java.util.ArrayList;

/**
 * Created by nur on 11/18/16.
 */

public class TideArrayAdapter extends ArrayAdapter<Tide> {

    public TideArrayAdapter(Context context, ArrayList<Tide> tides)
    {
        super(context, 0, tides);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tide tide = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_5, parent, false);
        }

        final TextView  date= (TextView) convertView.findViewById(R.id.date);
        final TextView  river= (TextView) convertView.findViewById(R.id.river);
        final TextView  time= (TextView) convertView.findViewById(R.id.time);
        final TextView  height= (TextView) convertView.findViewById(R.id.height);


        date.setText(tide.getDate());
        river.setText(tide.getRiver());
        time.setText(tide.getTime());
        height.setText(tide.getHeight());

        return convertView;
    }
}

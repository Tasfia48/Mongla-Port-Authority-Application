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

public class ContainerArrayAdapter extends ArrayAdapter<container> {

    public ContainerArrayAdapter(Context context, ArrayList<container> containers)
    {
        super(context, 0, containers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        container container = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_3, parent, false);
        }

        final TextView  slno= (TextView) convertView.findViewById(R.id.text1);
        final TextView a_n = (TextView) convertView.findViewById(R.id.text2);
        final TextView phn = (TextView) convertView.findViewById(R.id.text3);
        final TextView em = (TextView) convertView.findViewById(R.id.text4);
        final TextView twd = (TextView) convertView.findViewById(R.id.text5);
        final TextView twr = (TextView) convertView.findViewById(R.id.text6);
        final TextView ford = (TextView) convertView.findViewById(R.id.text7);
        final TextView forr = (TextView) convertView.findViewById(R.id.text8);
        final TextView td = (TextView) convertView.findViewById(R.id.text9);
        final TextView tr = (TextView) convertView.findViewById(R.id.text10);


        slno.setText(container.getSlno());
        a_n.setText(container.getA_name());
        phn.setText(container.getPhn());
        em.setText(container.getEm());
        twd.setText(container.getTwd());
        twr.setText(container.getTwr());
        ford.setText(container.getFord());
        forr.setText(container.getForr());
        td.setText(container.getTd());
        tr.setText(container.getTr());


        return convertView;
    }
}

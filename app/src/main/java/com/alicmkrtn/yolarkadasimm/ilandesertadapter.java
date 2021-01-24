package com.alicmkrtn.yolarkadasimm;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



import java.util.ArrayList;

public class ilandesertadapter extends ArrayAdapter<ilandesert> {

    public ilandesertadapter(Activity context, ArrayList<ilandesert> desserts) {
        super(context, 0, desserts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.denemelistadapter, parent, false);
        }*/

        ilandesert currentDesert = getItem(position);

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.denemelistadapter, parent, false);

        TextView nameTextView = (TextView) convertView.findViewById(R.id.txtkalkisismi);
        nameTextView.setText(currentDesert.getad());

        return convertView;
    }
}

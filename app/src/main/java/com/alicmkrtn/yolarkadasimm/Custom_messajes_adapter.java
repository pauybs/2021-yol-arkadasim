package com.alicmkrtn.yolarkadasimm;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Custom_messajes_adapter extends ArrayAdapter<Custom_messajes> {
    private static final String LOG_TAG = Custom_messajes_adapter.class.getSimpleName();
    public Custom_messajes_adapter(Activity context, ArrayList<Custom_messajes> custom_messajes) {
        super(context, 0, custom_messajes);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.custom_messages, parent, false);
        }
        Custom_messajes currentDesert = getItem(position);
        final ImageView ımageView2 = listItemView.findViewById(R.id.imgprofilresmi);
        Picasso.get().load(currentDesert.getkalkis()).into(ımageView2);
        final TextView txtad = listItemView.findViewById(R.id.txtadsoyad);
        txtad.setText(currentDesert.getad());
        final TextView txtid = listItemView.findViewById(R.id.id);
        txtid.setText(currentDesert.getid());
        return listItemView;
    }
}
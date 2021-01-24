package com.alicmkrtn.yolarkadasimm;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



import java.util.ArrayList;

public class messagesAdapter extends ArrayAdapter<messages> {
    private String kullanici;
    public messagesAdapter(Activity context, ArrayList<messages> messages, String kullanici) {
        super(context, 0, messages);
        this.kullanici = kullanici;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        messages message = getItem(position);
        if (kullanici.equalsIgnoreCase(message.getgonderici())){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.right_item_layout, parent, false);
            //TextView txtUser = (TextView) convertView.findViewById(R.id.txtUserRight);
            TextView txtMessage = (TextView) convertView.findViewById(R.id.txtMessageRight);
            TextView txtTime = (TextView) convertView.findViewById(R.id.txtTimeRight);
            //txtUser.setText(message.getgonderici());
            txtMessage.setText(message.getmesaj());
            txtTime.setText(message.gettarih());
        }else{
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.left_item_layout, parent, false);
            //TextView txtUser = (TextView) convertView.findViewById(R.id.txtUserLeft);
            TextView txtMessage = (TextView) convertView.findViewById(R.id.txtMessageLeft);
            TextView txtTime = (TextView) convertView.findViewById(R.id.txtTimeLeft);
            //txtUser.setText(message.getgonderici());
            txtMessage.setText(message.getmesaj());
            txtTime.setText(message.gettarih());
        }
        return convertView;
    }
}
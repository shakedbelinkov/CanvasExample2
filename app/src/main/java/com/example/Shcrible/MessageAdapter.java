package com.example.Shcrible;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Massage> {
    private Context context;
    private List<Massage> massages;
    public MessageAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Massage> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context=context;
        this.massages=objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.layout_chat,parent,false);

        TextView tvName = (TextView)view.findViewById(R.id.playerName);
        TextView tvMessage = (TextView)view.findViewById(R.id.massage);
        Massage temp = massages.get(position);

        tvName.setText(temp.getName());
        tvMessage.setText(temp.getMassage());

        return view;
    }

}

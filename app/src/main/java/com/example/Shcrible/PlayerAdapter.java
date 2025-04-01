package com.example.Shcrible;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PlayerAdapter extends ArrayAdapter<Player> {
    Context context;
    List<Player> objects;
    public PlayerAdapter(Context context, int resource, int textViewResourceId, List<Player> objects) {
        super(context, resource, textViewResourceId, objects);

        this.context=context;
        this.objects=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.layout_leaderboard,parent,false);

        TextView tvName = (TextView)view.findViewById(R.id.playerName);
        TextView tvPoints = (TextView)view.findViewById(R.id.points);
        Player temp = objects.get(position);

        tvName.setText(String.valueOf(temp.getName()));
        tvPoints.setText(temp.getPoints());

        return view;
    }
}

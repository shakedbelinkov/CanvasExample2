package com.example.Shcrible;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PlayerAdapter extends ArrayAdapter<Player> {
    Context context;
    ArrayList<Player> objects;
    public PlayerAdapter(Context context, int resource, int textViewResourceId, ArrayList<Player> objects) {
        super(context, resource, textViewResourceId, objects);

        this.context=context;
        this.objects=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.layout_leaderboard,parent,false);
        //change the data and the design according to the list of Player
        TextView tvNames = (TextView)view.findViewById(R.id.playerNameLB);
        TextView tvPoints = (TextView)view.findViewById(R.id.points);
        TextView tvPlace=(TextView)view.findViewById(R.id.placeLB);
        ImageView imageView=(ImageView)view.findViewById(R.id.colorLB);
        Player temp = objects.get(position);
        int place=temp.getPlace();
        tvNames.setText(String.valueOf(temp.getName()));
        tvPoints.setText(String.valueOf(temp.getPoints()));
        tvPlace.setText(String.valueOf(temp.getPlace()));
        if (place==1)
            imageView.setImageResource(R.drawable.color3);
        else if (place==2) {
            imageView.setImageResource(R.drawable.color2);
        }
        else if(place==3)
            imageView.setImageResource(R.drawable.color4);

        return view;
    }
}

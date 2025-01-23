package com.example.Shcrible;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;
import java.util.List;


public class MessageAdapter extends FirestoreRecyclerAdapter<Massage,MessageAdapter.ViewHolder> {
    public MessageAdapter(@NonNull FirestoreRecyclerOptions<Massage> options) {
        super(options);
    }
    /*private Context context;
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
    }*/
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView nameTV;
        private TextView massageTV;
        public ViewHolder(View view)
        {
            super(view);
            nameTV=view.findViewById(R.id.playerName);
            massageTV=view.findViewById(R.id.massage);
        }
    }
    protected void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position, @NonNull Massage model) {
        // Get element from your dataset at this position and replace the

        holder.massageTV.setText(model.getMassage());
        holder.nameTV.setText(model.getName());
    }
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.layout_chat, parent, false);

        // Return a new holder instance
        //  AllPostsAdapter.ViewHolder viewHolder = new FirebaseUIPostAdapter.ViewHolder(contactView);
        return new ViewHolder(contactView);
    }

}

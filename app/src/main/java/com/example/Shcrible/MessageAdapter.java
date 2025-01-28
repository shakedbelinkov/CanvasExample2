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


public class MessageAdapter extends FirestoreRecyclerAdapter<Message,MessageAdapter.ViewHolder> {
    public MessageAdapter(@NonNull FirestoreRecyclerOptions<Message> options) {
        super(options);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView nameTV;
        private TextView messageTV;
        public ViewHolder(View view)
        {
            super(view);
            nameTV=view.findViewById(R.id.playerName);
            messageTV=view.findViewById(R.id.message);
        }
    }

    protected void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position, @NonNull Message model) {
        // Get element from your dataset at this position and replace the

        holder.messageTV.setText(model.getMessage());
        if (model.isRight())
            holder.messageTV.setTextColor(Integer.valueOf("#39b037"));
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

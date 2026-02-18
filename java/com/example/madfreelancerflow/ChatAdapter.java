package com.example.madfreelancerflow;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private ArrayList<MessageModel> messageList;
    private String currentUserId;

    public ChatAdapter(ArrayList<MessageModel> messageList) {
        this.messageList = messageList;

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_message, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MessageModel message = messageList.get(position);

        holder.name.setText(message.getSenderName());
        holder.message.setText(message.getMessage());

        // Format time
        String formattedTime = new SimpleDateFormat("hh:mm a",
                Locale.getDefault())
                .format(new Date(message.getTimestamp()));

        holder.time.setText(formattedTime);

        // Highlight current user message
        if (message.getSenderId() != null &&
                message.getSenderId().equals(currentUserId)) {

            holder.itemView.setBackgroundColor(Color.parseColor("#1E3A5F"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#12243C"));
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, message, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_id);
            message = itemView.findViewById(R.id.msg_id);
            time = itemView.findViewById(R.id.time_id);
        }
    }
}

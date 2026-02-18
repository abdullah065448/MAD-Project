package com.example.madfreelancerflow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AIChatAdapter extends RecyclerView.Adapter<AIChatAdapter.ChatViewHolder> {

    private Context context;
    private List<AIChatMessage> messages;

    public AIChatAdapter(Context context, List<AIChatMessage> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).isUser() ? 0 : 1;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) { // user
            view = LayoutInflater.from(context).inflate(R.layout.item_user_message, parent, false);
        } else { // bot
            view = LayoutInflater.from(context).inflate(R.layout.item_ai_message, parent, false);
        }
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.txtMessage.setText(messages.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txtMessage);
        }
    }
}

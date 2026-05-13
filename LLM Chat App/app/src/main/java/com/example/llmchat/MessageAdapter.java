package com.example.llmchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_USER = 0;
    private static final int VIEW_TYPE_GEMINI = 1;

    private List<Message> messages;
    private Context context;

    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.getSpeaker().equals("user")) {
            return VIEW_TYPE_USER;
        } else {
            return VIEW_TYPE_GEMINI;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_USER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_user, parent, false);
            return new UserViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_gemini, parent, false);
            return new GeminiViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (holder instanceof UserViewHolder) {
            UserViewHolder userHolder = (UserViewHolder) holder;
            userHolder.userText.setText(message.getMessage());
            userHolder.userTimestamp.setText(message.getTimestamp());
        } else if (holder instanceof GeminiViewHolder) {
            GeminiViewHolder geminiHolder = (GeminiViewHolder) holder;
            geminiHolder.geminiText.setText(message.getMessage());
            geminiHolder.geminiTimestamp.setText(message.getTimestamp());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userText;
        TextView userTimestamp;
        public UserViewHolder(View itemView) {
            super(itemView);
            userText = itemView.findViewById(R.id.userText);
            userTimestamp = itemView.findViewById(R.id.userTimestamp);
        }
    }

    static class GeminiViewHolder extends RecyclerView.ViewHolder {
        TextView geminiText;
        TextView geminiTimestamp;
        public GeminiViewHolder(View itemView) {
            super(itemView);
            geminiText = itemView.findViewById(R.id.geminiText);
            geminiTimestamp = itemView.findViewById(R.id.geminiTimestamp);
        }
    }
}

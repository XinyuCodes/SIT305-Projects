package com.example.llm_project;

import android.adservices.topics.Topic;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private List<String> topics;
    private List<String> selectedTopics = new ArrayList<>();

    public TopicAdapter(List<String> topics) {
        this.topics = topics;
    }

    // ViewHolder represents a single card in the RecyclerView
    public static class TopicViewHolder extends RecyclerView.ViewHolder {
        TextView interestTextView;
        public TopicViewHolder(View itemView) {
            super(itemView);
             interestTextView = itemView.findViewById(R.id.interestTextView);
        }
    }

    @Override
    public TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TopicViewHolder holder, int position) {
        String topic = topics.get(position);

        holder.interestTextView.setText(topic);
        //changing background and text color
        if (selectedTopics.contains(topic)) {
            holder.itemView.setBackgroundColor(Color.parseColor("#673AB7"));
            holder.interestTextView.setTextColor(Color.WHITE);
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#E0E0E0"));
            holder.interestTextView.setTextColor(Color.BLACK);
        }

        //handle click to select/deselect a topic
        holder.itemView.setOnClickListener(v -> {
            if (selectedTopics.contains(topic)) {
                selectedTopics.remove(topic);
            } else {
                selectedTopics.add(topic);
            }
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        // TODO: return the size of the topics list
        return topics.size();
    }

    // call this from your Activity to get the selected topics
    public List<String> getSelectedTopics() {
        return selectedTopics;
    }
}
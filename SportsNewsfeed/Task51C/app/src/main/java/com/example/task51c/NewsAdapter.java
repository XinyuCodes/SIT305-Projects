package com.example.task51c;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsItem> newsList;
    private OnItemClickListener listener;

    //creating the constructor
    public NewsAdapter(List<NewsItem> newsList, OnItemClickListener listener){
        this.newsList = newsList;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    //creating OnItemClickListener
    public interface OnItemClickListener{
        void onItemClick(NewsItem item);
    }
    //add new newsviewholder class
    public static class NewsViewHolder extends RecyclerView.ViewHolder{
        ImageView ivNewImage;
        TextView tvNewsTitle;
        TextView tvNewsCategory;
        TextView tvNewsTimeStamp;

        public NewsViewHolder(@NonNull View itemView){
            super(itemView);
            ivNewImage = itemView.findViewById(R.id.ivNewsImage);
            tvNewsTitle = itemView.findViewById(R.id.tvNewsTitle);
            tvNewsCategory = itemView.findViewById(R.id.tvNewsCategory);
            tvNewsTimeStamp = itemView.findViewById(R.id.tvNewsTimestamp);
        }
    }

    //create new newviewsholder
    @NonNull
    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_card, parent, false);
        return new NewsViewHolder(view);
    }
    //making the magic happen
    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.NewsViewHolder holder, int position) {
        NewsItem item = newsList.get(position);
        holder.tvNewsTitle.setText(item.getTitle());
        holder.tvNewsCategory.setText(item.getCategory());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
        holder.ivNewImage.setImageResource(item.getImageResId());

    }
    public void updateList(List<NewsItem> newList) {
        this.newsList = newList;
        notifyDataSetChanged();
    }

}
package com.example.lostandfound;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdvertAdapter extends RecyclerView.Adapter<AdvertAdapter.AdvertViewHolder> {

    // Interface for handling item clicks
    public interface OnItemClickListener {
        void onItemClick(Advert advert);
    }

    // Data and context
    private List<Advert> advertList;
    private Context context;
    private OnItemClickListener listener;

    public static class AdvertViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, category, timestamp, postType;
        ImageView imageView;

        public AdvertViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.lostItem1);
            category = itemView.findViewById(R.id.category);
            timestamp = itemView.findViewById(R.id.date);
            postType = itemView.findViewById(R.id.lostorfoundInd);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    // Constructor
    public AdvertAdapter(Context context, List<Advert> advertList, OnItemClickListener listener) {
        this.context = context;
        this.advertList = advertList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdvertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_advert, parent, false);
        return new AdvertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdvertViewHolder holder, int position) {
        Advert advert = advertList.get(position);
        holder.itemName.setText(advert.getName());
        holder.category.setText(advert.getCategory());
        holder.timestamp.setText(advert.getTimestamp());
        holder.postType.setText(advert.getPostType());
        
        if (advert.getImagePath() != null && !advert.getImagePath().isEmpty()) {
            try {
                holder.imageView.setImageURI(Uri.parse(advert.getImagePath()));
            } catch (Exception e) {
                holder.imageView.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } else {
            holder.imageView.setImageResource(android.R.drawable.ic_menu_gallery);
        }
        
        holder.itemView.setOnClickListener(v -> listener.onItemClick(advert));
    }

    @Override
    public int getItemCount() {
        return advertList.size();
    }

    public void updateList(List<Advert> newList) {
        advertList = newList;
        notifyDataSetChanged();
    }
}
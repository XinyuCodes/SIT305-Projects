package com.example.task51c;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class BookmarksFragment extends Fragment {
    private RecyclerView rvBookmarks;
    private TextView tvEmptyState;
    private NewsAdapter bookmarksAdapter;

    public BookmarksFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookmarks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvBookmarks = view.findViewById(R.id.rvBookmarkedStories);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);
        rvBookmarks.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initiate and load from Room database
        BookmarkDatabase db = BookmarkDatabase.getInstance(getContext());
        db.bookmarkDao().getAllBookmarks().observe(getViewLifecycleOwner(), bookmarks -> {

            // Convert BookmarkEntity list to NewsItem list
            List<NewsItem> newsItems = new ArrayList<>();
            for (BookmarkEntity entity : bookmarks) {
                newsItems.add(new NewsItem(
                        entity.id,
                        entity.title,
                        entity.description,
                        entity.imageResId,
                        entity.category,
                        true,
                        false
                ));
            }


            // Show empty state or list
            if (newsItems.isEmpty()) {
                tvEmptyState.setVisibility(View.VISIBLE);
                rvBookmarks.setVisibility(View.GONE);
            } else {
                tvEmptyState.setVisibility(View.GONE);
                rvBookmarks.setVisibility(View.VISIBLE);

                bookmarksAdapter = new NewsAdapter(newsItems, item -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", item.getId());
                    bundle.putInt("imageResId", item.getImageResId());
                    bundle.putString("title", item.getTitle());
                    bundle.putString("description", item.getDescription());
                    bundle.putString("category", item.getCategory());
                    Navigation.findNavController(view)
                            .navigate(R.id.action_bookmarksFragment_to_detailFragment, bundle);
                });
                rvBookmarks.setAdapter(bookmarksAdapter);
            }
        });
    }
}
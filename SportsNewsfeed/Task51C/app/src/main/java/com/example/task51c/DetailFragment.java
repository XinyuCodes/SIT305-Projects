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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {

    private TextView tvDetailTitle;
    private TextView tvDetailDescription;
    private RecyclerView rvRelatedStories;
    private ImageView ivDetailImage;
    private Button btnBookmark;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find views
        tvDetailTitle = view.findViewById(R.id.tvDetailTitle);
        tvDetailDescription = view.findViewById(R.id.tvDetailDescription);
        rvRelatedStories = view.findViewById(R.id.rvRelatedStories);
        btnBookmark = view.findViewById(R.id.btnBookmark);
        ivDetailImage = view.findViewById(R.id.ivDetailImage);

        // Get bundle
        Bundle args = getArguments();
        if (args != null) {
            int id = args.getInt("id");
            String title = args.getString("title");
            String description = args.getString("description");
            String category = args.getString("category");
            int imageResId = args.getInt("imageResId");
            if(imageResId != 0){
                ivDetailImage.setImageResource(imageResId);
            }

            // Populate views
            tvDetailTitle.setText(title);
            tvDetailDescription.setText(description);

            // Get database instance
            BookmarkDatabase db = BookmarkDatabase.getInstance(getContext());

            // Check if already bookmarked and update button text
            new Thread(() -> {
                BookmarkEntity existing = db.bookmarkDao().getBookmarkById(id);
                requireActivity().runOnUiThread(() -> {
                    if (existing != null) {
                        btnBookmark.setText("Bookmarked");
                    } else {
                        btnBookmark.setText("Bookmark");
                    }
                });
            }).start();

            // Bookmark button click
            btnBookmark.setOnClickListener(v -> {
                new Thread(() -> {
                    BookmarkEntity existing = db.bookmarkDao().getBookmarkById(id);
                    if (existing != null) {
                        // already bookmarked so remove it
                        db.bookmarkDao().delete(existing);
                        requireActivity().runOnUiThread(() ->
                                btnBookmark.setText("Bookmark"));
                    } else {
                        // not bookmarked so save it
                        BookmarkEntity bookmark = new BookmarkEntity(
                                id, title, description, imageResId, category);
                        db.bookmarkDao().insert(bookmark);
                        requireActivity().runOnUiThread(() ->
                                btnBookmark.setText("Bookmarked"));
                    }
                }).start();
            });

            // Related stories
            List<NewsItem> relatedStories = getRelatedStories(category);
            NewsAdapter relatedAdapter = new NewsAdapter(relatedStories, item -> {
                Bundle bundle = new Bundle();
                bundle.putInt("id", item.getId());
                bundle.putString("title", item.getTitle());
                bundle.putString("description", item.getDescription());
                bundle.putString("category", item.getCategory());
                Navigation.findNavController(view)
                        .navigate(R.id.action_detailFragment_to_detailFragment, bundle);
            });
            rvRelatedStories.setLayoutManager(new LinearLayoutManager(getContext()));
            rvRelatedStories.setAdapter(relatedAdapter);
        }

        android.util.Log.d("DetailFragment", "args = " + args);
    }

    private List<NewsItem> getRelatedStories(String category) {
        List<NewsItem> allNews = getDummyData();
        List<NewsItem> related = new ArrayList<>();
        for (NewsItem item : allNews) {
            if (item.getCategory().equals(category)) {
                related.add(item);
            }
        }
        return related;
    }

    private List<NewsItem> getDummyData() {
        List<NewsItem> list = new ArrayList<>();
        list.add(new NewsItem(1, "Ronaldo scores hat-trick", "An amazing performance last night.", R.drawable.ronaldo, "Football", false, true));
        list.add(new NewsItem(2, "LeBron breaks scoring record", "History was made last night.", R.drawable.lebron, "Basketball", false, false));
        list.add(new NewsItem(3, "India wins Test series", "A dominant performance by India.", R.drawable.india, "Cricket", false, true));
        list.add(new NewsItem(4, "Man City win the league", "City clinch the title on final day.", R.drawable.mancity, "Football", false, false));
        return list;
    }
}
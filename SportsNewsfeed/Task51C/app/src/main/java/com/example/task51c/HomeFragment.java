package com.example.task51c;

import android.os.Bundle;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment {

    private RecyclerView rvFeaturedMatches;
    private RecyclerView rvLatestNews;
    private NewsAdapter featuredAdapter;
    private NewsAdapter latestNewsAdapter;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);

        rvFeaturedMatches = view.findViewById(R.id.rvFeaturedMatches);
        rvLatestNews = view.findViewById(R.id.rvLatestNews);

        rvFeaturedMatches.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvLatestNews.setLayoutManager(
                new LinearLayoutManager(getContext()));

        // Stop NestedScrollView intercepting horizontal swipes
        rvFeaturedMatches.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                int action = e.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    rv.getParent().requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });

        rvFeaturedMatches.setNestedScrollingEnabled(true);

        List<NewsItem> allNews = getDummyData();
        List<NewsItem> featuredNews = allNews.stream().filter(NewsItem::getIsFeatureStory).collect(Collectors.toList());

        featuredAdapter = new NewsAdapter(featuredNews, item -> {
            Bundle bundle = new Bundle();
            bundle.putInt("id", item.getId());
            bundle.putString("title", item.getTitle());
            bundle.putString("description", item.getDescription());
            bundle.putString("category", item.getCategory());
            bundle.putInt("imageResId", item.getImageResId());
            navController.navigate(R.id.action_homeFragment_to_detailFragment, bundle);
        });
        rvLatestNews.setNestedScrollingEnabled(true);

        latestNewsAdapter = new NewsAdapter(allNews, item -> {
            Bundle bundle = new Bundle();
            bundle.putInt("id", item.getId());
            bundle.putString("title", item.getTitle());
            bundle.putString("description", item.getDescription());
            bundle.putString("category", item.getCategory());
            bundle.putInt("imageResId", item.getImageResId());
            navController.navigate(R.id.action_homeFragment_to_detailFragment, bundle);
        });

        rvFeaturedMatches.setAdapter(featuredAdapter);
        rvLatestNews.setAdapter(latestNewsAdapter);

        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterNews(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterNews(newText);
                return true;
            }
        });
    }

    private void setFixedWidth(View view, int widthDp) {
        int widthPx = (int) (widthDp * getResources().getDisplayMetrics().density);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = widthPx;
        view.setLayoutParams(params);
    }

    /// providing the actual dummy data - just seeding some basic data here
    private List<NewsItem> getDummyData() {
        List<NewsItem> list = new ArrayList<>();
        list.add(new NewsItem(1, "Ronaldo scores hat-trick", "An amazing performance last night from Ronaldo!", R.drawable.ronaldo, "Football", false, true));
        list.add(new NewsItem(2, "LeBron breaks scoring record", "History was made last night.", R.drawable.lebron, "Basketball", false, false));
        list.add(new NewsItem(3, "India wins Test series", "A dominant performance by India - crowd describes it as India's 'Peak Performance'. No shortage of excitement on the ground.", R.drawable.india, "Cricket", false, true));
        list.add(new NewsItem(4, "Man City win the league", "City clinch the title on final day, what a nailbiter of a match it was!", R.drawable.mancity, "Football", false, false));
        return list;
    }

    private void filterNews(String query) {
        List<NewsItem> allNews = getDummyData();
        if (query.isEmpty()) {
            latestNewsAdapter.updateList(allNews);
            return;
        }
        List<NewsItem> filtered = new ArrayList<>();
        for (NewsItem item : allNews) {
            if (item.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    item.getCategory().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(item);
            }
        }
        latestNewsAdapter.updateList(filtered);
    }
}
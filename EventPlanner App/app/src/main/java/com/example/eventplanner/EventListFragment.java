package com.example.eventplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EventListFragment extends Fragment {

    private EventViewModel viewModel;
    private EventAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setting up the recycler view
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EventAdapter();
        recyclerView.setAdapter(adapter);

        // Set up ViewModel and observe events
        viewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        viewModel.allEvents.observe(getViewLifecycleOwner(), events -> {
            adapter.setEvents(events);
        });

        // Set up click listener — navigate to add/edit screen
        adapter.setListener(event -> {
            Bundle bundle = new Bundle();
            bundle.putInt("eventId", event.id);
            bundle.putString("eventTitle", event.title);
            bundle.putString("eventCategory", event.category);
            bundle.putString("eventDateTime", event.dateTime);
            bundle.putString("eventLocation", event.location);
            Navigation.findNavController(view)
                    .navigate(R.id.action_list_to_addedit, bundle);
        });
    }
}
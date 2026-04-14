package com.example.eventplanner;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import android.widget.Toast;

public class AddEditEventFragment extends Fragment {

    private EventViewModel viewModel;
    private EditText editTitle, editCategory, editDateTime, editLocation;
    private Button buttonSave, buttonDelete;


    private int eventId = -1;
    private boolean isEditMode = false;

    private boolean isValidDateTime(String input){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        format.setLenient(false);
        try{
            format.parse(input);
            return true;
        }
        catch(ParseException e){
            return false;
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_edit_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get views
        editTitle = view.findViewById(R.id.edit_title);
        editCategory = view.findViewById(R.id.edit_category);
        editDateTime = view.findViewById(R.id.edit_datetime);
        editLocation = view.findViewById(R.id.edit_location);
        buttonSave = view.findViewById(R.id.button_save);
        buttonDelete = view.findViewById(R.id.button_delete);

        viewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

        // Check if we were passed an existing event (edit mode)
        Bundle args = getArguments();
        if (args != null && args.containsKey("eventId")) {
            isEditMode = true;
            eventId = args.getInt("eventId");

            // Pre-fill the form with existing data
            editTitle.setText(args.getString("eventTitle"));
            editCategory.setText(args.getString("eventCategory"));
            editDateTime.setText(args.getString("eventDateTime"));
            editLocation.setText(args.getString("eventLocation"));

            // Show delete button in edit mode
            buttonDelete.setVisibility(View.VISIBLE);
        }

        // Save button — creates or updates depending on mode
        buttonSave.setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();
            String category = editCategory.getText().toString().trim();
            String dateTime = editDateTime.getText().toString().trim();
            String location = editLocation.getText().toString().trim();
            Boolean canUpdate = true;

            if (title.isEmpty()) {
                editTitle.setError("Title is required");
                canUpdate = false;
                return;
            }

            if(category.isEmpty()){
                editCategory.setError("Category is required");
                canUpdate = false;
                return;
            }

            if(dateTime.isEmpty()){
                editDateTime.setError("Date Time cannot be empty!");
                canUpdate = false;
                return;
            }
            if (!isValidDateTime(dateTime)) {
                editDateTime.setError("Format your time like YYYY-MM-DD HH:MM");
                canUpdate = false;
                return;
            }

            if (isEditMode) {
                if(canUpdate == true) {
                    // UPDATE existing event
                    Event updated = new Event(title, category, dateTime, location);
                    updated.id = eventId;
                    viewModel.update(updated);
                    Toast.makeText(requireContext(), "Event successfully updated!", Toast.LENGTH_SHORT).show();
                }
            } else {
                if(canUpdate == true) {
                    // CREATE new event
                    Event newEvent = new Event(title, category, dateTime, location);
                    viewModel.insert(newEvent);
                    Toast.makeText(requireContext(), "Event successfully added!", Toast.LENGTH_SHORT).show();
                }
            }
            // Navigate back to the list
            Navigation.findNavController(view).navigateUp();
        });

        // Delete button — only visible in edit mode
        buttonDelete.setOnClickListener(v -> {
            Event toDelete = new Event(
                    editTitle.getText().toString(),
                    editCategory.getText().toString(),
                    editDateTime.getText().toString(),
                    editLocation.getText().toString()
            );
            toDelete.id = eventId;
            viewModel.delete(toDelete);
            Toast.makeText(requireContext(), "Event successfully deleted!", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).navigateUp();
        });
    }

}
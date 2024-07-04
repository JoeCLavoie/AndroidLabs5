package com.example.androidlabs5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DetailsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        // Get references to the TextViews
        TextView name = view.findViewById(R.id.name);
        TextView height = view.findViewById(R.id.height);
        TextView mass = view.findViewById(R.id.mass);

        // Get the arguments passed to this fragment
        Bundle args = getArguments();
        if (args != null) {
            // Set the text for each TextView with the character details
            name.setText("Name: " + args.getString("name"));
            height.setText("Height: " + args.getString("height"));
            mass.setText("Mass: " + args.getString("mass"));
        }

        return view;
    }
}

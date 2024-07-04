package com.example.androidlabs5;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        // Get extras passed to this activity
        Bundle extras = getIntent().getExtras();
        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(extras);

        // Replace the FrameLayout with DetailsFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
}

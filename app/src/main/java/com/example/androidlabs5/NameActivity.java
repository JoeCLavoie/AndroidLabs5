package com.example.androidlabs5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.name_activity);

        TextView textViewWelcome = findViewById(R.id.textViewWelcome);
        Button btnThankYou = findViewById(R.id.btnThankYou);
        Button btnDontCallMeThat = findViewById(R.id.btnDontCallMeThat);

        String name = getIntent().getStringExtra("name");
        textViewWelcome.setText(getString(R.string.welcome_blank, name));

        btnThankYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });

        btnDontCallMeThat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(0);
                finish();
            }
        });
    }
}

package com.example.androidlabs5;

import android.os.Bundle; // Save & restore activity state
import android.view.View; // Base class for UI components
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout; // Used to dynamically load views
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private TextView textView;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Btns to switch layouts
        Button buttonLinear = findViewById(R.id.button_linear);
        Button buttonGrid = findViewById(R.id.button_grid);
        Button buttonConstraint = findViewById(R.id.button_constraint);

        buttonLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayout(R.layout.activity_main_linear);
            }
        });

        buttonGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayout(R.layout.activity_main_grid);
            }
        });

        buttonConstraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLayout(R.layout.activity_main_constraint);
            }
        });

        // Choose and load a default layout - Linear in this case is chosen
        loadLayout(R.layout.activity_main_linear);
    }

    private void loadLayout(int layoutResID) {
        FrameLayout container = findViewById(R.id.container);
        container.removeAllViews(); // Clear previous views
        View.inflate(this, layoutResID, container);

        // After switching to new layout, rebind views and set listeners
        rebindViewsAndSetListeners();
    }

    private void rebindViewsAndSetListeners() {
        editText = findViewById(R.id.edit_text);
        textView = findViewById(R.id.text_view);
        checkBox = findViewById(R.id.check_box);

        // Setting onClickListener for the Press Me button
        Button pressMeButton = findViewById(R.id.press_me_btn);
        if (pressMeButton != null) {
            pressMeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = editText.getText().toString();
                    textView.setText(text);
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.toast_message), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Set onCheckedChangeListener for the CheckBox
        if (checkBox != null) {
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                String message = getString(R.string.checkbox_snackbar_message, isChecked ? "on" : "off");
                Snackbar snackbar = Snackbar.make(buttonView, message, Snackbar.LENGTH_LONG);
                snackbar.setAction(R.string.undo, v -> checkBox.setChecked(!isChecked));
                snackbar.show();
            });
        }
    }
}

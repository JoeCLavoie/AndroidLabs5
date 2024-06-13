package com.example.androidlabs5;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<TodoItem> todoList = new ArrayList<>(); // Lis to store ToDo items
    private TodoAdapter adapter; // Adapter to handle ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.todo_list);
        EditText editText = findViewById(R.id.todo_edit_text);
        Switch urgentSwitch = findViewById(R.id.urgent_switch);
        Button addButton = findViewById(R.id.add_button);

        // Initialize adapter with context and list of ToDo items
        adapter = new TodoAdapter(this, todoList);
        listView.setAdapter(adapter);

        // Click listener for add button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoText = editText.getText().toString(); // Get text from EditText
                boolean isUrgent = urgentSwitch.isChecked(); // Get urgency status from switch
                if (!todoText.isEmpty()) { // Text not empty
                    TodoItem todoItem = new TodoItem(todoText, isUrgent); // Create new ToDo item
                    todoList.add(todoItem); // Add item to list
                    adapter.notifyDataSetChanged(); // Notify adapter to refresh ListView
                    editText.setText(""); //Clear EditText
                }
            }
        });

        // Long click listener for ListView items
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Show an AlertDialog to confirm deletion
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(getString(R.string.confirm_delete))
                        .setMessage(getString(R.string.selected_row) + position)
                        .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                            todoList.remove(position);
                            adapter.notifyDataSetChanged();
                        })
                        .setNegativeButton(getString(R.string.no), null)
                        .show();
                return true;
            }
        });
    }
}

package com.example.androidlabs5;

import android.content.ContentValues; // New import for ContentValues
import android.database.Cursor; // New import for Cursor
import android.database.sqlite.SQLiteDatabase; // New import for SQLiteDatabase
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

    private List<TodoItem> todoList = new ArrayList<>(); // List to store ToDo items
    private TodoAdapter adapter; // Adapter to handle ListView
    private DatabaseHelper dbHelper; // New: Database helper instance
    private SQLiteDatabase db; // New: SQLite database instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.todo_list);
        EditText editText = findViewById(R.id.todo_edit_text);
        Switch urgentSwitch = findViewById(R.id.urgent_switch);
        Button addButton = findViewById(R.id.add_button);

        // New: Initialize the database helper and get a writable database
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        // New: Load existing ToDo items from the database
        loadTodosFromDatabase();

        // Initialize adapter with context and list of ToDo items
        adapter = new TodoAdapter(this, todoList);
        listView.setAdapter(adapter);

        // onClick listener for add button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoText = editText.getText().toString(); // Get text from EditText
                boolean isUrgent = urgentSwitch.isChecked(); // Get urgency status from switch
                if (!todoText.isEmpty()) { // Text not empty
                    TodoItem todoItem = new TodoItem(todoText, isUrgent); // Create new ToDo item
                    addTodoToDatabase(todoItem); // New: Add item to database
                    todoList.add(todoItem); // Add item to list
                    adapter.notifyDataSetChanged(); // Notify adapter to refresh ListView
                    editText.setText(""); // Clear EditText
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
                            deleteTodoFromDatabase(todoList.get(position)); // New: Delete item from database
                            todoList.remove(position); // Remove item from list
                            adapter.notifyDataSetChanged(); // Notify adapter to refresh ListView
                        })
                        .setNegativeButton(getString(R.string.no), null)
                        .show();
                return true;
            }
        });
    }

    // New: Method to load existing ToDo items from the database
    private void loadTodosFromDatabase() {
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String text = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TEXT));
            boolean isUrgent = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_URGENT)) == 1;
            TodoItem todoItem = new TodoItem(text, isUrgent);
            todoList.add(todoItem);
        }
        cursor.close();
    }

    // New: Method to add a new ToDo item to the database
    private void addTodoToDatabase(TodoItem todoItem) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TEXT, todoItem.getText());
        values.put(DatabaseHelper.COLUMN_URGENT, todoItem.isUrgent() ? 1 : 0);
        db.insert(DatabaseHelper.TABLE_NAME, null, values);
    }

    // New: Method to delete a ToDo item from the database
    private void deleteTodoFromDatabase(TodoItem todoItem) {
        String selection = DatabaseHelper.COLUMN_TEXT + " = ? AND " + DatabaseHelper.COLUMN_URGENT + " = ?";
        String[] selectionArgs = { todoItem.getText(), todoItem.isUrgent() ? "1" : "0" };
        db.delete(DatabaseHelper.TABLE_NAME, selection, selectionArgs);
    }
}


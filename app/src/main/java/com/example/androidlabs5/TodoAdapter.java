package com.example.androidlabs5;

import android.content.Context; // Import for context to access app-specific resources
import android.graphics.Color; // Import for use of colour
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

// Adapter class to handle ListView for ToDo items
public class TodoAdapter extends BaseAdapter {
    private Context context;
    private List<TodoItem> todoList;

    // Initialize adapter with context and list of ToDo items
    public TodoAdapter(Context context, List<TodoItem> todoList) {
        this.context = context;
        this.todoList = todoList;
    }

    @Override
    public int getCount() { // Returns number of items in list
        return todoList.size();
    }

    @Override
    public Object getItem(int position) { // Returns item at specified position
        return todoList.get(position);
    }

    @Override
    public long getItemId(int position) { // Returns position as ID
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.todo_item, parent, false);
        }
        // Gets current ToDo item, finds TextView and sets text
        TodoItem todoItem = (TodoItem) getItem(position);
        TextView textView = convertView.findViewById(R.id.todo_text);
        textView.setText(todoItem.getText());

        // Sets background colour of item based on urgency
        if (todoItem.isUrgent()) {
            convertView.setBackgroundColor(Color.RED);
            textView.setTextColor(Color.WHITE);
        } else {
            convertView.setBackgroundColor(Color.WHITE);
            textView.setTextColor(Color.BLACK);
        }

        return convertView;
    }
}

package com.example.androidlabs5;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// SQLiteOpenHelper class to manage database creation and version management
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "todo.db"; // Database name
    public static final int DATABASE_VERSION = 1; // Database version
    public static final String TABLE_NAME = "todo_items"; // Table name
    public static final String COLUMN_ID = "_id"; // Column ID
    public static final String COLUMN_TEXT = "text"; // Column for ToDo text
    public static final String COLUMN_URGENT = "urgent"; // Column for urgency

    // SQL query to create the table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TEXT + " TEXT, " +
                    COLUMN_URGENT + " INTEGER);";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the table
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Debugging method to print cursor information
    public static void printCursor(Cursor c) {
        Log.d("DatabaseHelper", "Database Version: " + c.getColumnCount());
        Log.d("DatabaseHelper", "Number of Columns: " + c.getColumnCount());
        Log.d("DatabaseHelper", "Column Names: ");
        for (String name : c.getColumnNames()) {
            Log.d("DatabaseHelper", "Column Name: " + name);
        }
        Log.d("DatabaseHelper", "Number of Results: " + c.getCount());
        while (c.moveToNext()) {
            String row = "";
            for (int i = 0; i < c.getColumnCount(); i++) {
                row += c.getString(i) + " ";
            }
            Log.d("DatabaseHelper", "Row: " + row);
        }
    }
}

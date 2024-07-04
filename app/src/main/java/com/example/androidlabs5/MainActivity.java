package com.example.androidlabs5;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView; // ListView to display character names
    private List<JSONObject> characterList = new ArrayList<>(); // List to hold character JSON objects
    private List<String> characterNames = new ArrayList<>(); // List to hold character names

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize ListView
        listView = findViewById(R.id.listView);

        // Execute AsyncTask to fetch data from API
        new FetchDataTask().execute();

        // Set item click listener for ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    // Get selected character's JSON object
                    JSONObject character = characterList.get(position);

                    // Create a bundle to pass character details
                    Bundle bundle = new Bundle();
                    bundle.putString("name", character.getString("name"));
                    bundle.putString("height", character.getString("height"));
                    bundle.putString("mass", character.getString("mass"));

                    if (findViewById(R.id.frameLayout) == null) { // If frameLayout is not found, we are on a phone
                        // Start EmptyActivity with character details
                        Intent intent = new Intent(MainActivity.this, EmptyActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else { // Otherwise, on a tablet
                        // Replace the fragment with DetailsFragment
                        DetailsFragment fragment = new DetailsFragment();
                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frameLayout, fragment)
                                .commit();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // AsyncTask to fetch data from the Star Wars API
    private class FetchDataTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                // Connect to API
                URL url = new URL("https://swapi.dev/api/people/?format=json");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            } catch (Exception e) {
                Log.e("FetchDataTask", "Error fetching data", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                // Handle case where the result is null
                Toast.makeText(MainActivity.this, "Failed to fetch data from API", Toast.LENGTH_SHORT).show();
                Log.e("FetchDataTask", "Result is null");
                return;
            }

            try {
                Log.d("FetchDataTask", "API Response: " + result);
                // Parse the JSON response
                JSONObject jsonObject = new JSONObject(result);
                JSONArray characters = jsonObject.getJSONArray("results");
                for (int i = 0; i < characters.length(); i++) {
                    JSONObject character = characters.getJSONObject(i);
                    characterList.add(character);
                    characterNames.add(character.getString("name"));
                }
                // Update ListView with character names
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, characterNames);
                listView.setAdapter(adapter);
            } catch (JSONException e) {
                Log.e("FetchDataTask", "Error parsing JSON data", e);
                Toast.makeText(MainActivity.this, "Failed to parse JSON data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

package com.example.androidlabs5;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    // Tag for logging (had issues with ProgressBar not updating, added for debugging)
    private static final String TAG = "MainActivity";
    // UI elements
    private ImageView imageView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);

        // Start the AsyncTask to load cat images
        new CatImages().execute();
    }

    // AsyncTask class to handle downloading and displaying cat images
    private class CatImages extends AsyncTask<Void, Integer, Bitmap> {
        private Bitmap catImage; // Bitmap to store the downloaded cat image

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Show the ProgressBar before starting the background task
            runOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            try {
                while (true) {
                    // Fetch JSON response from URL
                    URL url = new URL("https://cataas.com/cat?json=true");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    StringBuilder json = new StringBuilder();
                    int byteCharacter;
                    while ((byteCharacter = inputStream.read()) != -1) {
                        json.append((char) byteCharacter);
                    }

                    // Parse JSON response to get img ID
                    JSONObject jsonObject = new JSONObject(json.toString());
                    String id = jsonObject.getString("_id");
                    String imageUrl = "https://cataas.com/cat/" + id;

                    // Check if img exists locally
                    File file = new File(getCacheDir(), id + ".jpg");
                    if (file.exists()) {
                        catImage = BitmapFactory.decodeFile(file.getPath());
                    } else {
                        // Download the img from URL
                        URL imageDownloadUrl = new URL(imageUrl);
                        HttpURLConnection imageConnection = (HttpURLConnection) imageDownloadUrl.openConnection();
                        InputStream imageInputStream = imageConnection.getInputStream();
                        catImage = BitmapFactory.decodeStream(imageInputStream);

                        // Save the downloaded img locally
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        catImage.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                        fileOutputStream.close();
                    }

                    // Publish progress to trigger UI updates
                    publishProgress(0);
                    for (int i = 0; i < 100; i++) {
                        publishProgress(i);
                        Thread.sleep(30); // Pause to simulate a countdown
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error downloading cat image", e);
            }
            return catImage; // Return the last cat image downloaded
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // Log progress value for debugging
            Log.d(TAG, "Progress: " + values[0]);
            // If progress is at the start, update the ImageView with the new image
            if (values[0] == 0) {
                runOnUiThread(() -> {
                    imageView.setImageBitmap(catImage);
                });
            }
            // Update the ProgressBar with current progress
            runOnUiThread(() -> progressBar.setProgress(values[0]));
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // If a bitmap is successfully downloaded, set to ImageView
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                Log.e(TAG, "Failed to download cat image");
            }
            // Hide the ProgressBar when task is complete
            runOnUiThread(() -> progressBar.setVisibility(View.INVISIBLE));
        }
    }
}

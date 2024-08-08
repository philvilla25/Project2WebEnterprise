package com.example.sliderapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText currentTravelTextView, dirChangeCountTextView, maxTravelTextView, movementDirectionTextView, sizeEditText,xTextView, yTextView;
    private ImageView slider;
    private Handler handler;
    private Runnable fetchRunnable;
    private static final int FETCH_INTERVAL = 700; // 200 milliseconds
    private RequestQueue reqQueue;
    private String url = "http://10.0.2.2:8080/Project3/resources/cst8218.stan0304.slider.entity.slider/";
    Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        currentTravelTextView = findViewById(R.id.currentTravelTextView);
        dirChangeCountTextView = findViewById(R.id.dirChangeCountTextView);
        maxTravelTextView = findViewById(R.id.maxTraveTextView);
        movementDirectionTextView = findViewById(R.id.movementDirectionTextView);
        sizeEditText = findViewById(R.id.sizeEditText);
        xTextView = findViewById(R.id.xTextView);
        yTextView = findViewById(R.id.yTextView);
        Button updateButton = findViewById(R.id.update_btn);
        logout = findViewById(R.id.logout_btn);
        slider = findViewById(R.id.slider_image);


        reqQueue = Volley.newRequestQueue(this);

        // Set click listener on the button to make POST request
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a JSONObject with new data from TextView fields
                JSONObject newData = new JSONObject();
                try {
                    newData.put("currentTravel", currentTravelTextView.getText().toString());
                    newData.put("dirChangeCount", dirChangeCountTextView.getText().toString());
                    newData.put("id", 1);
                    newData.put("maxTravel", maxTravelTextView.getText().toString());
                    newData.put("movementDirection", movementDirectionTextView.getText().toString());
                    newData.put("size", sizeEditText.getText().toString());
                    newData.put("x", xTextView.getText().toString());
                    newData.put("y", yTextView.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String urlPost = "http://10.0.2.2:8080/Project3/resources/cst8218.stan0304.slider.entity.slider/1";
                // Make POST request to change the data
                makePostRequest(urlPost, newData);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the Handler and Runnable for periodic fetching
        handler = new Handler(Looper.getMainLooper());
        fetchRunnable = new Runnable() {
            @Override
            public void run() {
                makeGetRequest(url);
                handler.postDelayed(this, FETCH_INTERVAL);
            }
        };

        // Start the periodic task
        handler.post(fetchRunnable);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Logout Account", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void makeGetRequest(String url) {
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Assuming the first element in the array is the required JSON object
                            JSONObject sliderData = response.getJSONObject(0);

                            int idGet = sliderData.getInt("id");
                            int currentTravel = sliderData.getInt("currentTravel");
                            int dirChangeCount = sliderData.getInt("dirChangeCount");
                            int maxTravel = sliderData.getInt("maxTravel");
                            int movementDirection = sliderData.getInt("movementDirection");
                            int size = sliderData.getInt("size");
                            int x = sliderData.getInt("x");
                            int y = sliderData.getInt("y");

                            Log.d(TAG, "ID: " + idGet);
                            Log.d(TAG, "Current Travel: " + currentTravel);
                            Log.d(TAG, "Direction Change Count: " + dirChangeCount);
                            Log.d(TAG, "Max Travel: " + maxTravel);
                            Log.d(TAG, "Movement Direction: " + movementDirection);
                            Log.d(TAG, "Movement Direction: " + size);
                            Log.d(TAG, "X: " + x);
                            Log.d(TAG, "Y: " + y);

                            // Populate TextView fields with the data
                            currentTravelTextView.setText(String.valueOf(currentTravel));
                            dirChangeCountTextView.setText(String.valueOf(dirChangeCount));
                            maxTravelTextView.setText(String.valueOf(maxTravel));
                            movementDirectionTextView.setText(String.valueOf(movementDirection));
                            sizeEditText.setText(String.valueOf(size));
                            xTextView.setText(String.valueOf(x));
                            yTextView.setText(String.valueOf(y));

                            updateHorizontalBias(x, y, movementDirection, maxTravel);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.getMessage(), error);
                    }
                }
        );
        reqQueue.add(getRequest);
    }

    private void updateHorizontalBias(int x, int y, int movementDirection, int maxTravel) {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) slider.getLayoutParams();
        int MAX_TRAVEL = 1000;
        // Compute the bias based on x, y, movementDirection, and maxTravel
        float normalizedBias;
        if (movementDirection <= 0) { // Horizontal movement
            normalizedBias = Math.max(0, Math.min(1, (float) x / MAX_TRAVEL));
        } else { // Vertical movement
            normalizedBias = Math.max(0, Math.min(1, (float) y / MAX_TRAVEL));
        }

        params.horizontalBias = normalizedBias;
        slider.setLayoutParams(params);
    }


    private void makePostRequest(String url, JSONObject newData) {
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.PUT, url, newData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response after a successful POST request
                        //tv.setText("Data updated: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      //  tv.setText(error.getMessage());
                        Log.e(TAG, "Error: " + error.getMessage(), error);
                    }
                }
        );
        reqQueue.add(postRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the periodic task to prevent memory leaks
        handler.removeCallbacks(fetchRunnable);
    }

}

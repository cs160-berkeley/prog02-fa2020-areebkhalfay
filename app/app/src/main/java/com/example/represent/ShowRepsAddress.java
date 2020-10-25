package com.example.represent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShowRepsAddress extends AppCompatActivity {
    String url = "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyBwVVygpmWGOqADxipiBs7lLmUK9u7B0Ws&address=";
    public static final String EXTRA_MESSAGE = "com.ShowMoreInfo.input";
    public String full_message;
    public String message;
    private RequestQueue mQueue;
    private String randFeature = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_reps_address);

        Intent intent = getIntent();
        full_message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        String[] message_index = full_message.split("\\s+");
        message = message_index[0];
        if (message_index.length > 1) {
            randFeature = message_index[1];
        }
        url += message;
        mQueue = Volley.newRequestQueue(this);

        final TextView senator_1_name_tV = findViewById(R.id.senator_1_name);
        final TextView senator_1_party_tV = findViewById(R.id.senator_1_party);
        final ImageView senator_1_image = findViewById(R.id.senator_1_pic);

        final TextView senator_2_name_tV = findViewById(R.id.senator_2_name);
        final TextView senator_2_party_tV = findViewById(R.id.senator_2_party);
        final ImageView senator_2_image = findViewById(R.id.senator_2_pic);

        final TextView representative_name_tV = findViewById(R.id.representative_name);
        final TextView representative_party_tV = findViewById(R.id.representative_party);
        final ImageView representative_image = findViewById(R.id.representative_pic);

        //Help from: https://codinginflow.com/tutorials/android/volley/part-1-simple-get-request
        //Help from: https://www.youtube.com/watch?v=Tdb_WSEEZbQ&ab_channel=CodinginFlow
        //Help from: https://stackoverflow.com/questions/58430498/error-http-504-when-load-image-form-url-with-picasso-android-library
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("officials");

                    Picasso.get().setLoggingEnabled(true);
                    //First Senator
                    JSONObject senator_1_obj = jsonArray.getJSONObject(2);
                    String senator_1_name = senator_1_obj.getString("name");
                    String senator_1_party = senator_1_obj.getString("party").replace( " Party", "");
                    if (senator_1_obj.has("photoUrl")) {
                        String senator_1_photoUrl = senator_1_obj.getString("photoUrl");
                        Picasso.get().load(senator_1_photoUrl).resize(109, 156).into(senator_1_image);
                    }
                    else {
                        Picasso.get().load(R.drawable.no_image_available).resize(109, 156).into(senator_1_image);
                    }
                    senator_1_name_tV.setText(senator_1_name);
                    senator_1_party_tV.setText(senator_1_party);
                    JSONObject senator_2_obj = jsonArray.getJSONObject(3);
                    String senator_2_name = senator_2_obj.getString("name");
                    String senator_2_party = senator_2_obj.getString("party").replace( " Party", "");
                    if (senator_2_obj.has("photoUrl")) {
                        String senator_2_photoUrl = senator_2_obj.getString("photoUrl");
                        Picasso.get().load(senator_2_photoUrl).resize(109, 156).into(senator_2_image);
                    }
                    else {
                        Picasso.get().load(R.drawable.no_image_available).resize(109, 156).into(senator_2_image);
                    }
                    senator_2_name_tV.setText(senator_2_name);
                    senator_2_party_tV.setText(senator_2_party);
                    JSONObject representative_obj = jsonArray.getJSONObject(4);
                    String representative_name = representative_obj.getString("name");
                    String representative_party = representative_obj.getString("party").replace( " Party", "");
                    if (representative_obj.has("photoUrl")) {
                        String representative_photoUrl= representative_obj.getString("photoUrl");
                        Picasso.get().load(representative_photoUrl).resize(109, 156).into(representative_image);
                    }
                    else {
                        Picasso.get().load(R.drawable.no_image_available).resize(109, 156).into(representative_image);
                    }
                    representative_name_tV.setText(representative_name);
                    representative_party_tV.setText(representative_party);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
        }

    public void sendSenator_1_Name(View view) {
        Intent intent = new Intent(this, ShowMoreInfo.class);
        intent.putExtra(EXTRA_MESSAGE, message + " 2" + " S" + " " + randFeature);
        startActivity(intent);
    }

    public void sendSenator_2_Name(View view) {
        Intent intent = new Intent(this, ShowMoreInfo.class);
        intent.putExtra(EXTRA_MESSAGE, message + " 3" + " S" + " " + randFeature);
        startActivity(intent);
    }

    public void sendRepresentative(View view) {
        Intent intent = new Intent(this, ShowMoreInfo.class);
        intent.putExtra(EXTRA_MESSAGE, message + " 4" + " R" + " " + randFeature);
        startActivity(intent);
    }
}
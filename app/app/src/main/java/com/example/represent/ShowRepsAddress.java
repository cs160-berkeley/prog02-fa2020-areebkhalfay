package com.example.represent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    private RequestQueue mQueue;
    private ArrayList<String> senator_1 = new ArrayList<>();
    private ArrayList<String> senator_2 = new ArrayList<>();
    private ArrayList<String> representative = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_reps_address);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        System.out.println(message);
        url += message;
        System.out.println(url);
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
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("officials");

                    //First Senator
                    JSONObject senator_1_obj = jsonArray.getJSONObject(2);
                    System.out.println(senator_1_obj);
                    String senator_1_name = senator_1_obj.getString("name");
                    System.out.println(senator_1_name);
                    String senator_1_party = senator_1_obj.getString("party").replace( " Party", "");
                    System.out.println(senator_1_party);
                    if (senator_1_obj.has("photoUrl")) {
                        String senator_1_photoUrl = senator_1_obj.getString("photoUrl").replace("http:", "https:");
                        Picasso.get().load(senator_1_photoUrl).resize(109, 156).into(senator_1_image);
                    }
                    senator_1_name_tV.setText(senator_1_name);
                    senator_1_party_tV.setText(senator_1_party);
                    JSONObject senator_2_obj = jsonArray.getJSONObject(3);
                    System.out.println(senator_2_obj);
                    String senator_2_name = senator_2_obj.getString("name");
                    System.out.println(senator_2_name);
                    String senator_2_party = senator_2_obj.getString("party").replace( " Party", "");
                    System.out.println(senator_2_party);
                    if (senator_2_obj.has("photoUrl")) {
                        String senator_2_photoUrl = senator_2_obj.getString("photoUrl").replace("http:", "https:");
                        Picasso.get().load(senator_2_photoUrl).resize(109, 156).into(senator_2_image);
                    }
                    senator_2_name_tV.setText(senator_2_name);
                    senator_2_party_tV.setText(senator_2_party);
                    JSONObject representative_obj = jsonArray.getJSONObject(4);
                    System.out.println(senator_2_obj);
                    String representative_name = representative_obj.getString("name");
                    System.out.println(representative_name);
                    String representative_party = representative_obj.getString("party").replace( " Party", "");
                    System.out.println(representative_party);
                    if (representative_obj.has("photoUrl")) {
                        String representative_photoUrl = representative_obj.getString("photoUrl").replace("http:", "https:");
                        Picasso.get().load(representative_photoUrl).resize(109, 156).into(representative_image);
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

}
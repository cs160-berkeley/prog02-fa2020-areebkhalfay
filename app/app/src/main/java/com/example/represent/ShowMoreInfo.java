package com.example.represent;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.util.Random;
public class ShowMoreInfo extends AppCompatActivity {
    private Integer index;
    private String message;
    private String senator_or_representative;
    String url = "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyBwVVygpmWGOqADxipiBs7lLmUK9u7B0Ws&address=";
    private RequestQueue mQueue;
    private Boolean random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_more_info);

        Intent intent = getIntent();
        String full_message = intent.getStringExtra(ShowRepsAddress.EXTRA_MESSAGE);
        String[] message_index = full_message.split("\\s+");
        message = message_index[0];
        index = Integer.parseInt(message_index[1]);
        senator_or_representative = message_index[2];
        if (message_index.length > 3) {
            random = true;
        }
        else {
            random = false;
        }
        url += message;

        //Setting Senator or Representative
        TextView senator_or_rep = findViewById(R.id.rep_title);
        if (senator_or_representative.equals("S")) {
            senator_or_rep.setText(R.string.senator_title);
        }
        else {
            senator_or_rep.setText(R.string.representative_title);
        }

        mQueue = Volley.newRequestQueue(this);
        final TextView senator_1_name_tV = findViewById(R.id.senator_1_name);
        final TextView senator_1_party_tV = findViewById(R.id.senator_1_party);
        final TextView senator_1_phone_tV = findViewById(R.id.phone_number_actual);
        final ImageView senator_1_image = findViewById(R.id.senator_1_pic);
        final TextView senator_1_email_tV = findViewById(R.id.email_address);
        final Button website_button = (Button) findViewById(R.id.button6);
        final Button special_feature_button = (Button) findViewById(R.id.button5);
        if (!random) {
            special_feature_button.setVisibility(View.GONE);
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("officials");

                    Picasso.get().setLoggingEnabled(true);
                    //First Senator
                    JSONObject senator_1_obj = jsonArray.getJSONObject(index);
                    String senator_1_name = senator_1_obj.getString("name");
                    String senator_1_party = senator_1_obj.getString("party").replace( " Party", "");
                    String senator_1_phone = senator_1_obj.getJSONArray("phones").getString(0);
                    final String senator_website = senator_1_obj.getJSONArray("urls").getString(0);
                    if (senator_1_obj.has("emails")) {
                        String senator_1_email = senator_1_obj.getJSONArray("emails").getString(0);
                        senator_1_email_tV.setText(senator_1_email);
                    }
                    else {
                        senator_1_email_tV.setText(R.string.email_not_available);
                    }
                    if (senator_1_obj.has("photoUrl")) {
                        String senator_1_photoUrl = senator_1_obj.getString("photoUrl");
                        Picasso.get().load(senator_1_photoUrl).resize(109, 156).into(senator_1_image);
                    }
                    else {
                        Picasso.get().load(R.drawable.no_image_available).resize(109, 156).into(senator_1_image);
                    }
                    senator_1_name_tV.setText(senator_1_name);
                    senator_1_party_tV.setText(senator_1_party);
                    senator_1_phone_tV.setText(senator_1_phone);
                    //Help from https://stackoverflow.com/questions/4930228/open-a-url-on-click-of-ok-button-in-android
                    website_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent rep_website = new Intent(Intent.ACTION_VIEW);
                            rep_website.setData(Uri.parse(senator_website));
                            startActivity(rep_website);
                        }
                    });
                    if (senator_1_obj.has("channels") && random){
                        JSONArray channels = senator_1_obj.getJSONArray("channels");
                        Random r = new Random();
                        int rand_index = r.ints(0, channels.length()).limit(1).findFirst().getAsInt();
                        JSONObject random_feat = channels.getJSONObject(rand_index);
                        String random_type = random_feat.getString("type");
                        final String random_id = random_feat.getString("id");
                        if (random_type.equals("Facebook")) {
                            special_feature_button.setText(R.string.facebook);
                            special_feature_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent rep_website = new Intent(Intent.ACTION_VIEW);
                                    rep_website.setData(Uri.parse("https://www.facebook.com/" + random_id));
                                    startActivity(rep_website);
                                }
                            });
                        }
                        else if (random_type.equals("Twitter")) {
                            special_feature_button.setText(R.string.twitter);
                            special_feature_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent rep_website = new Intent(Intent.ACTION_VIEW);
                                    rep_website.setData(Uri.parse("https://www.twitter.com/" + random_id));
                                    startActivity(rep_website);
                                }
                            });
                        }
                        else if (random_type.equals("YouTube")) {
                            special_feature_button.setText(R.string.youtube);
                            special_feature_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent rep_website = new Intent(Intent.ACTION_VIEW);
                                    rep_website.setData(Uri.parse("https://www.youtube.com/" + random_id));
                                    startActivity(rep_website);
                                }
                            });
                        }
                    }
                    else {
                        special_feature_button.setVisibility(View.GONE);
                    }
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
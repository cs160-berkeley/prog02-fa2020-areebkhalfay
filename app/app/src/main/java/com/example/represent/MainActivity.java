package com.example.represent;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.Manifest;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    public static final String EXTRA_MESSAGE = "com.ShowRepsAddress.input";
    private RequestQueue mQueue;
    private FusedLocationProviderClient fusedLocationClient;
    private String currentAddress;
    private String randomAddress;
    private Context context = this;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void sendMessageCurrentAddress(View view) {
        //Permission
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.

        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
        }

        //Location Request from https://stackoverflow.com/questions/29441384/fusedlocationapi-getlastlocation-always-null
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        //TODO: UI updates.
                    }
                }
            }
        };
        mQueue = Volley.newRequestQueue(this);
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + location.getLatitude() + "," + location.getLongitude() +  "&key=AIzaSyBwVVygpmWGOqADxipiBs7lLmUK9u7B0Ws";
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");

                            JSONObject jsonObj = jsonArray.getJSONObject(0);
                            String formatted_address = jsonObj.getString("formatted_address");
                            currentAddress = formatted_address.replace(',', ' ').replace(' ','+');
                            Intent intent = new Intent(context, ShowRepsAddress.class);
                            intent.putExtra(EXTRA_MESSAGE, currentAddress);
                            startActivity(intent);
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
        });
    }

    public void sendRandomAddress(View view) {
        double lat_max = 41.8;
        double lat_min = 33.8;
        double long_max = -81.5;
        double long_min = -116.2;

        double random_lat = ThreadLocalRandom.current().nextDouble(lat_min, lat_max);
        double random_long = ThreadLocalRandom.current().nextDouble(long_min, long_max);

        mQueue = Volley.newRequestQueue(this);
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + random_lat + "," + random_long +  "&key=AIzaSyBwVVygpmWGOqADxipiBs7lLmUK9u7B0Ws";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");

                    JSONObject jsonObj = jsonArray.getJSONObject(0);
                    String formatted_address = jsonObj.getString("formatted_address");
                    randomAddress = formatted_address.replace(',', ' ').replace(' ','+');
                    System.out.println(randomAddress);
                    Intent intent = new Intent(context, ShowRepsAddress.class);
                    intent.putExtra(EXTRA_MESSAGE, randomAddress + " random");
                    startActivity(intent);
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

    public void sendMessageRepsAddress(View view) {
        Intent intent = new Intent(this, ShowRepsAddress.class);
        EditText address_1_eT = (EditText) findViewById(R.id.editText2);
        String address_1 = address_1_eT.getText().toString().replace(' ', '+');
        address_1 = address_1 + '+';
        EditText city_eT = (EditText) findViewById(R.id.editText4);
        String city = city_eT.getText().toString().replace(' ', '+') + '+';
        EditText state_eT = (EditText) findViewById(R.id.editText5);
        String state = state_eT.getText().toString() + '+';
        EditText zipcode_address_eT = (EditText) findViewById(R.id.editText6);
        String zipcode = zipcode_address_eT.getText().toString();
        String full_address = address_1 + city + state + zipcode;
        intent.putExtra(EXTRA_MESSAGE, full_address);
        startActivity(intent);
    }

    public void sendMessageRepsAddressZip(View view) {
        Intent intent = new Intent(this, ShowRepsAddress.class);
        EditText address_1_eT = (EditText) findViewById(R.id.editText1);
        String zip_code = address_1_eT.getText().toString().replace(' ', '+');
        intent.putExtra(EXTRA_MESSAGE, zip_code);
        startActivity(intent);
    }

}
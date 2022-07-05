package com.example.m1_frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class currLocation extends AppCompatActivity implements LocationListener {
    protected LocationManager locationManager;
    final static String TAG = "currLocActivity";
    //Use google maps API to turn latitude and longitude values to address
    private final String url = "https://maps.googleapis.com/maps/api/geocode/json";
    private final String apikey = "AIzaSyDZDy1pZBbYHzj8On-4_ZB6sN8pG44Agic";
    TextView currCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curr_location);
        currCity = (TextView) findViewById(R.id.loc_model_view);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Error below can be ignored following Piazza post @29
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        currCity = (TextView) findViewById(R.id.loc_model_view);

        String tempurl = url + "?latlng=" + location.getLatitude() + "," + location.getLongitude() + "&key=" + apikey;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Extract the city name from JSON response
                    JSONObject jsonResp = new JSONObject(response);
                    JSONObject plus_code = jsonResp.getJSONObject("plus_code");
                    String compound_code = plus_code.getString("compound_code");
                    String addr_fragment = compound_code.split(",")[0];
                    String city = addr_fragment.substring(9, addr_fragment.length());
                    Log.d(TAG, "city: " + city);
                    currCity.setText("Current City: " + city + "\n" + "Manufacturer: " + Build.MANUFACTURER + "\n" + "Model: " + Build.MODEL);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
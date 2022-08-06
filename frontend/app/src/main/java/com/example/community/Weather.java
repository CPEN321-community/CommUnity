package com.example.community;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class Weather extends AppCompatActivity implements LocationListener {
    final static String TAG = "Weather_Activity";
    protected LocationManager locationManager;
    //OpenWeatherMap API used for the Surprise weather button functionality
    private final String weather_url = "http://api.openweathermap.org/data/2.5/weather";
    private final String appid = "5603ddab5ca3086399dbee9a2fba312d";

    //Use google maps API to turn latitude and longitude values to address
    private final String maps_url = "https://maps.googleapis.com/maps/api/geocode/json";
    private final String apikey = "AIzaSyDqU7yXjuf0a5tcSboHwIQ_y7Hpw3Z_wZA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Error below can be ignored following Piazza post @29
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        final String[] city = new String[1];

        String tempurl = maps_url + "?latlng=" + location.getLatitude() + "," + location.getLongitude() + "&key=" + apikey;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Extract the city name from JSON response
                    JSONObject jsonResp = new JSONObject(response);
                    JSONObject plus_code = jsonResp.getJSONObject("plus_code");
                    String compound_code = plus_code.getString("compound_code");
                    String addr_fragment = compound_code.split(",")[0];
                    city[0] = addr_fragment.substring(9, addr_fragment.length());
                    Log.d(TAG, "city: " + city[0]);
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

        String weather_tempurl = "";
        weather_tempurl = weather_url + "?q=" + city[0] + "&appid=" + appid;

            StringRequest stringReq = new StringRequest(Request.Method.POST, tempurl, new Response.Listener<String>(){
                @Override
                public void onResponse(String response){
                    String data_output = "";
                    try{
                        JSONObject jsonResp = new JSONObject(response);
                        JSONArray jsonArr = jsonResp.getJSONArray("weather");
                        JSONObject jsonObjWeather = jsonArr.getJSONObject(0);
                        String description = jsonObjWeather.getString("description");
                        Log.d(TAG, "Current weather: " + description);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error){
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });

            RequestQueue weather_requestQueue = Volley.newRequestQueue(getApplicationContext());
            weather_requestQueue.add(stringReq);
    }
}
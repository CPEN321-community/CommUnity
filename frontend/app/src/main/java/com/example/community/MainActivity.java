package com.example.community;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.community.classes.ChatHelper;
import com.example.community.classes.GlobalUtil;
import com.example.community.classes.SearchHelper;
import com.example.community.classes.TagHelper;
import com.example.community.databinding.ActivityMainBinding;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final String TAG = "MAIN_ACTIVITY";
    //protected LocationManager locationManager;
    //OpenWeatherMap External API
    private final String weather_url = "http://api.openweathermap.org/data/2.5/weather";
    private final String appid = "5603ddab5ca3086399dbee9a2fba312d";

    //Use google maps API to turn latitude and longitude values to address
    private final String maps_url = "https://maps.googleapis.com/maps/api/geocode/json";
    private final String apikey = "AIzaSyDxPKklD3bicapK5B-sorORJhnuu8vLXWU";
    public String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Starting Main Activity!");

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        GlobalUtil.setAppContext(getApplicationContext());

        // Pass Notification Token to backend on start app
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            String token = task.getResult();
            CustomFirebaseMessagingService.sendTokenToDatabase(token);
        });

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_leaderboard, R.id.navigation_profile)
                .build();
        hideActionBar();
        SearchHelper.search(this);
        ChatHelper.GetSocket();
        ChatHelper.FetchChats(this);
        TagHelper.getTags();


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Error below can be ignored following Piazza post @29
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        checkLocationPermissions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    private boolean checkLocationPermissions() {
        // Check if permissions have already been granted
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Location permissions have already been granted", Toast.LENGTH_LONG).show();
            return true;
        } else {
            // If denied either of the two permissions then give them some more info (and
            // ask again)
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(TAG, "denied at least 1 permission");
                Toast.makeText(MainActivity.this,
                                "Are you sure? We need these location permissions to personalise this app!", Toast.LENGTH_LONG)
                        .show();
                new AlertDialog.Builder(this)
                        .setTitle("Need Location Permissions")
                        .setMessage("Are you sure? We need these location permissions to personalise this app!")
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(MainActivity.this, "We need these location permissions to run!",
                                        Toast.LENGTH_LONG).show();
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[] { Manifest.permission.ACCESS_COARSE_LOCATION,
                                                Manifest.permission.ACCESS_FINE_LOCATION },
                                        1);
                            }
                        })
                        .create()
                        .show();
            } else {
                // If haven't requested permissions before
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION }, 1);
            }
        }
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {

        String tempurl = maps_url + "?latlng=" + location.getLatitude() + "," + location.getLongitude() + "&key=" + apikey;
        Log.d(TAG, "location url: " + tempurl);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Extract the city name from JSON response
                    JSONObject jsonResp = new JSONObject(response);
                    JSONObject plus_code = jsonResp.getJSONObject("plus_code");
                    String compound_code = plus_code.getString("compound_code");
                    String addr_fragment = compound_code.split(",")[0];
                    city = addr_fragment.substring(9, addr_fragment.length());
                    Log.d(TAG, "city: " + city);
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
        weather_tempurl = weather_url + "?q=" + city + "&appid=" + appid;
        Log.d(TAG, "weather url: " + weather_tempurl);

        StringRequest stringReq = new StringRequest(Request.Method.POST, weather_tempurl, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                String data_output = "";
                try{
                    JSONObject jsonResp = new JSONObject(response);
                    JSONArray jsonArr = jsonResp.getJSONArray("weather");
                    JSONObject jsonObjWeather = jsonArr.getJSONObject(0);
                    String description = jsonObjWeather.getString("description");
                    Log.d(TAG, "Current weather: " + description);

                    String bgColour;
                    ConstraintLayout bgLayout = (ConstraintLayout) findViewById(R.id.sun_moon_bg);

                    switch(description){
                        case "clear sky":
                            bgLayout.setBackgroundResource(R.drawable.sunny);
                            Log.d(TAG, "background sunny");
                            break;
                        case "few clouds":
                        case "overcast clouds":
                        case "broken clouds":
                        case "scattered clouds":
                            bgLayout.setBackgroundResource(R.drawable.cloudy);
                            Log.d(TAG, "background cloudy");
                            break;
                        case "light rain":
                        case "moderate rain":
                            bgLayout.setBackgroundResource(R.drawable.rainy);
                            Log.d(TAG, "background rain");
                            break;
                        default:
                            bgLayout.setBackgroundColor(Color.parseColor("#8AC9D1"));
                            Log.d(TAG, "background default");
                            break;
                    }

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
package com.example.m1_frontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button fav_loc_button;
    private Button curr_loc_button;
    private Button weather_button;
    final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Button 1
        fav_loc_button = findViewById(R.id.maps_button);
        fav_loc_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Trying to open google maps");

                Intent mapsIntent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(mapsIntent);
            }
        });

        //Button 2
        curr_loc_button = findViewById(R.id.curr_location_button);
        curr_loc_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean success = checkLocationPermissions();
                Log.d(TAG, "Is success? " + success);
                //if we have the appropriate location permissions we can start the current location/phone model activity,
                //otherwise we will wait until we have them
                if(checkLocationPermissions()){
                    Log.d(TAG, "Trying to show current location and phone model");
                    Intent currLocIntent = new Intent(MainActivity.this, currLocation.class);
                    startActivity(currLocIntent);
                }else{
                    //Log that button was clicked
                    Log.d(TAG, "Trying to request location permissions");
                    //Display on screen that button was clicked
                    Toast.makeText(MainActivity.this, "Trying to request location permissions", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Button 3
        weather_button = findViewById(R.id.weather_button);
        weather_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Trying to open weather page");

                Intent weatherIntent = new Intent(MainActivity.this, Weather.class);
                startActivity(weatherIntent);
            }
        });
    }
    private boolean checkLocationPermissions(){
        //Check if permissions have already been granted
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "We have these permissions yay!", Toast.LENGTH_LONG).show();
            return true;
        }
        else{
            //If denied either of the two permissions then give them some more info (and ask again)
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                Log.d(TAG, "denied at least 1 permission");
                Toast.makeText(MainActivity.this, "We need these location permissions to run!", Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(this)
                        .setTitle("Need Location Permissions")
                        .setMessage("We need the location permissions to mark your location on a map")
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(MainActivity.this, "We need these location permissions to run!", Toast.LENGTH_LONG).show();
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            }
                        })
                        .create()
                        .show();
            }
            else{
                //If haven't requested permissions before
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
        return false;
    }
}
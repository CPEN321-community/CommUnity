package com.example.community;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.community.classes.GlobalUtil;
import com.example.community.databinding.ActivityMainBinding;
import com.example.community.ui.chat.ChatActivity;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        ActionBar actionBar = getSupportActionBar();
        initCustomActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
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

    private void initCustomActionBar() {
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.ab_custom, null);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            Log.e(TAG, "initCustomActionBar: Failed to get support action bar");
            return;
        }
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        v.findViewById(R.id.chat_button).setOnClickListener((view) -> {
            Intent chatIntent = new Intent(MainActivity.this, ChatActivity.class);
            startActivity(chatIntent);
        });
        v.findViewById(R.id.search_button).setOnClickListener((view) -> {
            Intent chatIntent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(chatIntent);
        });
        actionBar.setCustomView(v);
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
                Toast.makeText(MainActivity.this, "Are you sure? We need these location permissions to personalise this app!", Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(this)
                        .setTitle("Need Location Permissions")
                        .setMessage("Are you sure? We need these location permissions to personalise this app!")
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
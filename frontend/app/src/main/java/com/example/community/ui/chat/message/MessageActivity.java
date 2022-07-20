package com.example.community.ui.chat.message;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.community.R;
import com.example.community.classes.Chat;
import com.example.community.databinding.ActivityMessageListBinding;

public class MessageActivity extends AppCompatActivity {

    private static final String TAG = "MESSAGE_ACTIVITY";
    private AppBarConfiguration appBarConfiguration;
    private ActivityMessageListBinding binding;
    protected Chat chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        this.chat = (Chat) intent.getSerializableExtra("chat");
        binding = ActivityMessageListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_message);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


        binding.toolbar.setTitle(chat.other.firstName);
        Log.d(TAG, "onCreate: " + chat.me.firstName);
        Log.d(TAG, "onCreate: " + chat.other.firstName);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_message);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
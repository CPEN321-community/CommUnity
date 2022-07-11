package com.example.community.request_list;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.community.R;
import com.example.community.classes.Global;
import com.example.community.classes.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class NewRequestForm extends AppCompatActivity {

    private static final String TAG = "NEW_REQUEST_FORM";
    private EditText itemName;
    private EditText itemQuantity;
    private EditText desc;
    private Button createPostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request_form);
        this.itemName = this.findViewById(R.id.request_name_input);
        this.itemQuantity = this.findViewById(R.id.quantity_input);
        this.desc = this.findViewById(R.id.description_input);
        this.createPostButton = this.findViewById(R.id.create_request_button);
        this.createPostButton.setOnClickListener(v -> {
            this.createOfferPost();
        });
    }


    private void createOfferPost() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://10.0.2.2:8081/communitypost/requests";
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("userId", Global.getAccount().getId());
            postBody.put("title", this.itemName.getText().toString());
            postBody.put("description", this.desc.getText().toString());
            postBody.put("status", "ACTIVE");
        } catch (JSONException e) {
            Log.e(TAG, "createPost: " + e);
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url,
                postBody,
                (JSONObject response) -> {
                    Log.d(TAG, "createPost: " + response);
                    Toast toast = Toast.makeText(this, "Successfully created post!", Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    toastView.setBackgroundColor(Color.parseColor("#00ff00"));
                    toast.show();
                    finish();
                },
                error -> {
                    Log.e(TAG, "fetchLeaderboard: " + error);
                });
        queue.add(request);
    }
}
package com.example.community.offer_list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.community.R;
import com.example.community.classes.GlobalUtility;
import com.example.community.classes.Tags;
import com.example.community.classes.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class NewOfferForm extends AppCompatActivity {

    private static final String TAG = "NEW_OFFER_FORM";
    private static final int REQUEST_CODE = 4;
    private EditText itemName;
    private EditText itemQuantity;
    private CalendarView bestBefore;
    private Button uploadPhotoButton;
    private EditText pickup;
    private EditText desc;
    private Button createPostButton;
    private Tags tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_offer_form);
        this.itemName = this.findViewById(R.id.offer_name_input);
        this.itemQuantity = this.findViewById(R.id.quantity_input);
        this.bestBefore = this.findViewById(R.id.best_before_input);
        this.uploadPhotoButton = this.findViewById(R.id.upload_button);
        this.pickup = this.findViewById(R.id.pickup_location_input);
        this.desc = this.findViewById(R.id.description_input);
        this.createPostButton = this.findViewById(R.id.create_offer_button);
        this.createPostButton.setOnClickListener(v -> {
            this.createOfferPost();
        });
        TextView fruit = findViewById(R.id.fruit);
        TextView vegetable = findViewById(R.id.vegetable);
        TextView nut = findViewById(R.id.nut);

        Tags tags = new Tags(fruit, vegetable, nut);
        this.tags = tags;

        this.uploadPhotoButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE);
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case REQUEST_CODE:
                    if (resultCode == Activity.RESULT_OK) {
                        //data gives you the image uri. Try to convert that to bitmap
                        break;
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        Log.e(TAG, "Selecting picture cancelled");
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in onActivityResult : " + e.getMessage());
        }
    }


    private void createOfferPost() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = GlobalUtility.POST_URL + "/communitypost/offers";
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("userId", GlobalUtility.getAccount().getId());
            postBody.put("title", this.itemName.getText().toString());
            postBody.put("description", this.desc.getText().toString());
            postBody.put("quantity", Integer.parseInt(this.itemQuantity.getText().toString()));
            postBody.put("pickUpLocation", this.pickup.getText().toString());
            postBody.put("image", "");
            postBody.put("status", "ACTIVE");
            postBody.put("tagList", this.tags.getJSONArr());
            Date selectedDate = new Date(this.bestBefore.getDate());
            String dateString = Utils.DateToString(selectedDate);
            postBody.put("bestBeforeDate", dateString);

        } catch (JSONException e) {
            Log.e(TAG, "createPost: " + e);
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url,
                postBody,
                (JSONObject response) -> {
                    Log.d(TAG, "createPost: " + response);
                    Toast.makeText(this, "Successfully created post!", Toast.LENGTH_LONG).show();
                    finish();
                },
                error -> {
                    Log.e(TAG, "fetchLeaderboard: " + error);
                });
        queue.add(request);
    }

}
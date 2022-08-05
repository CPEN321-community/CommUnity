package com.example.community.offer_list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.community.R;
import com.example.community.classes.CustomJSONObjectRequest;
import com.example.community.classes.DateImgUtil;
import com.example.community.classes.GlobalUtil;
import com.example.community.classes.TagHelper;
import com.example.community.databinding.ActivityNewOfferFormBinding;
import com.example.community.ui.TagAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class NewOfferForm extends AppCompatActivity {

    private static final String TAG = "NEW_OFFER_FORM";
    private static final int REQUEST_CODE = 4;
    private EditText itemName;
    private EditText itemQuantity;
    private CalendarView bestBefore;
    private EditText pickup;
    private EditText desc;


    @Override
    public void onBackPressed() {
        TagHelper.reset();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        TagHelper.reset();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button uploadPhotoButton;
        Button createPostButton;
        getSupportActionBar().hide();
        ActivityNewOfferFormBinding binding = ActivityNewOfferFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        RecyclerView tagList = binding.includeTags.tagsList;
        tagList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        TagAdapter adapter = new TagAdapter(this, TagHelper.reset());
        tagList.setAdapter(adapter);

        TagHelper.getTagData().observe(this, ts -> {
            adapter.setItems(ts);
            adapter.notifyDataSetChanged();
        });
        this.itemName = this.findViewById(R.id.offer_name_input);
        this.itemQuantity = this.findViewById(R.id.quantity_input);
        this.bestBefore = this.findViewById(R.id.best_before_input);
        uploadPhotoButton = this.findViewById(R.id.upload_button);
        this.pickup = this.findViewById(R.id.pickup_location_input);
        this.desc = this.findViewById(R.id.description_input);
        createPostButton = this.findViewById(R.id.create_offer_button);
        createPostButton.setOnClickListener(v -> {
            this.createOfferPost();
        });

        uploadPhotoButton.setOnClickListener(v -> {
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
                        // data gives you the image uri. Try to convert that to bitmap
                        break;
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        Log.e(TAG, "Selecting picture cancelled");
                    }
                    break;
                default:
                    Log.e(TAG, "Invalid request code");
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in onActivityResult : " + e.getMessage());
        }
    }

    private void createOfferPost() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = GlobalUtil.POST_URL + "/communitypost/offers";
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("userId", GlobalUtil.getId());
            postBody.put("title", this.itemName.getText().toString());
            postBody.put("description", this.desc.getText().toString());
            postBody.put("quantity", Integer.parseInt(this.itemQuantity.getText().toString()));
            postBody.put("pickUpLocation", this.pickup.getText().toString());
            postBody.put("image", "");
            postBody.put("status", "ACTIVE");
            JSONArray arr = TagHelper.getJSONArr();
            postBody.put("tagList", arr);
            Log.d(TAG, "createOfferPost: " + arr);
            Date selectedDate = new Date(this.bestBefore.getDate());
            String dateString = DateImgUtil.DateToString(selectedDate);
            postBody.put("bestBeforeDate", dateString);

        } catch (JSONException e) {
            Log.e(TAG, "createPost: " + e);
            e.printStackTrace();
        }

        CustomJSONObjectRequest request = new CustomJSONObjectRequest(Request.Method.POST,
                url,
                postBody,
                (JSONObject response) -> {
                    Log.d(TAG, "createPost: " + response);
                    Toast.makeText(this, "Successfully created post!", Toast.LENGTH_LONG).show();
                    finish();
                },
                error -> {
                    Toast.makeText(this, "Failed to create post, please try again", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "fetchLeaderboard: " + error);
                });
        queue.add(request);
    }

}
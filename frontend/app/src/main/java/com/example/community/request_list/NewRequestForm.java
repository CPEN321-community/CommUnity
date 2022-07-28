package com.example.community.request_list;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.community.R;
import com.example.community.classes.GlobalUtil;
import com.example.community.classes.Tags;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewRequestForm extends AppCompatActivity {

    private static final String TAG = "NEW_REQUEST_FORM";
    private EditText itemName;
    private EditText desc;
    private Tags tags;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request_form);
        this.itemName = this.findViewById(R.id.request_name_input);
        this.desc = this.findViewById(R.id.description_input);
        Button createPostButton = this.findViewById(R.id.create_request_button);
        createPostButton.setOnClickListener(v -> {
            this.createRequestPost();
        });
        TextView fruit = findViewById(R.id.fruit);
        TextView vegetable = findViewById(R.id.vegetable);
        TextView nut = findViewById(R.id.nut);

        Tags tags = new Tags(fruit, vegetable, nut);
        this.tags = tags;
    }


    private void createRequestPost() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = GlobalUtil.POST_URL + "/communitypost/requests";
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("userId", GlobalUtil.getId());
            postBody.put("title", this.itemName.getText().toString());
            postBody.put("description", this.desc.getText().toString());
            postBody.put("status", "ACTIVE");
            postBody.put("tagList", tags.getJSONArr());
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
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("token", GlobalUtil.getHeaderToken());
                return headers;
            }
        };
        queue.add(request);
    }
}
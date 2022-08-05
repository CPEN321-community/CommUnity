package com.example.community.request_list;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.community.R;
import com.example.community.classes.CustomJSONObjectRequest;
import com.example.community.classes.GlobalUtil;
import com.example.community.classes.SearchManager;
import com.example.community.classes.Tag;
import com.example.community.classes.TagManager;
import com.example.community.databinding.ActivityNewRequestFormBinding;
import com.example.community.ui.TagAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewRequestForm extends AppCompatActivity {

    private static final String TAG = "NEW_REQUEST_FORM";
    private EditText itemName;
    private EditText desc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityNewRequestFormBinding binding = ActivityNewRequestFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        RecyclerView tagList = binding.includeTagsReq.tagsList;
        ArrayList<Tag> tags = TagManager.getTags();
        tagList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        TagAdapter adapter = new TagAdapter(this, TagManager.reset());
        tagList.setAdapter(adapter);
        for (int i = 0; i < tags.size(); i++) {
            Tag t = tags.get(i);

            int finalI = i;
            t.getClickData().observe(this, clicked -> {
                adapter.notifyItemChanged(finalI);
            });
        }
        this.itemName = this.findViewById(R.id.request_name_input);
        this.desc = this.findViewById(R.id.description_input);
        Button createPostButton = this.findViewById(R.id.create_request_button);
        createPostButton.setOnClickListener(v -> {
            this.createRequestPost();
        });
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
            postBody.put("tagList", TagManager.getJSONArr());
        } catch (JSONException e) {
            Log.e(TAG, "createPost: " + e);
            e.printStackTrace();
        }


        CustomJSONObjectRequest request = new CustomJSONObjectRequest(Request.Method.POST,
                url,
                postBody,
                (JSONObject response) -> {
                    Log.d(TAG, "createPost: " + response);
                    Toast toast = Toast.makeText(this, "Successfully created post!", Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    toastView.setBackgroundColor(Color.parseColor("#00ff00"));
                    toast.show();
                    SearchManager.search(this);
                    finish();
                },
                error -> {
                    Log.e(TAG, "fetchLeaderboard: " + error);
                });
        queue.add(request);
    }
}
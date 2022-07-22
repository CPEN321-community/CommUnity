package com.example.community;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.community.classes.GlobalUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class EditRestrictionsActivity extends AppCompatActivity {

    private static final String TAG = "EDIT_RESTRICTIONS_ACTIVITY";
    private EditText textField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restrictions);
        this.textField = findViewById(R.id.restriction_input);
        Button submitButton = findViewById(R.id.submit_restriction_button);
        submitButton.setOnClickListener(v -> {
            CreatePreference();
        });
    }

    private void CreatePreference() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = GlobalUtil.USER_URL + "/user/preference";
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("userId", GlobalUtil.getAccount().getId());
            postBody.put("value", this.textField.getText().toString());
            postBody.put("type", "DIETARY");
        } catch (JSONException e) {
            Log.e(TAG, "createPost: " + e);
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT,
                url,
                postBody,
                (JSONObject response) -> {
                    Log.d(TAG, "createPost: " + response);
                    Toast toast = Toast.makeText(this, "Successfully added dietary restriction!", Toast.LENGTH_LONG);
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
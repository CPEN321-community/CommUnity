package com.example.community;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.HashMap;
import java.util.Map;

public class EditRestrictionsActivity extends AppCompatActivity {

    private static final String TAG = "EDIT_RESTRICTIONS_ACTIVITY";
    private EditText textField;
    private Button submitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_restrictions);
        this.textField = findViewById(R.id.restriction_input);
        submitButton = findViewById(R.id.add_dietary_restriction_button);
        submitButton.setEnabled(false);
        this.textField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                boolean enabled = text.trim().length() > 0;
                submitButton.setEnabled(enabled);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        submitButton.setOnClickListener(v -> {
            submitButton.setEnabled(false);
            CreatePreference();
        });
    }

    private void CreatePreference() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = GlobalUtil.USER_URL + "/user/preference";
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("userId", GlobalUtil.getId());
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
                    textField.setText("");
                    Toast toast = Toast.makeText(this, "Successfully added dietary restriction!", Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    toastView.setBackgroundColor(Color.parseColor("#00ff00"));
                    toast.show();
                    finish();
                },
                error -> {
                    submitButton.setEnabled(true);
                    Toast toast = Toast.makeText(this, "Failed to add restriction", Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    toastView.setBackgroundColor(Color.parseColor("#ff0000"));
                    toast.show();
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
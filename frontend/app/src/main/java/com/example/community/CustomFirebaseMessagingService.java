package com.example.community;

import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.community.classes.GlobalUtil;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CustomFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FIREBASE_MESSAGING_SERVICE";

    /**
     * There are two scenarios when onNewToken is called:
     * 1) When a new token is generated on initial app startup
     * 2) Whenever an existing token is changed
     * Under #2, there are three scenarios when the existing token is changed:
     * A) App is restored to a new device
     * B) User uninstalls/reinstalls the app
     * C) User clears app data
     */
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);
        sendTokenToDatabase(token);
        super.onNewToken(token);
    }

    public static void sendTokenToDatabase(String token) {
        RequestQueue queue = Volley.newRequestQueue(GlobalUtil.getAppContext());
        String url = GlobalUtil.CHAT_URL + "/token";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("userId", GlobalUtil.getAccount().getId());
            jsonBody.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url,
                jsonBody,
                null,
                error -> {
                    Log.e(TAG, "sendTokenToDatabase: " + error);
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
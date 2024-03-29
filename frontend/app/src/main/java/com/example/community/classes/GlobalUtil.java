package com.example.community.classes;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.socket.client.Socket;

public class GlobalUtil {
//     public static final String CHAT_URL = "http://10.0.2.2:3000";
//     public static final String USER_URL = "http://10.0.2.2:8080";
//     public static final String POST_URL = "http://10.0.2.2:8081";
    public static final String CHAT_URL = "http://ec2-3-96-132-62.ca-central-1.compute.amazonaws.com:3000";
    public static final String USER_URL = "http://ec2-3-98-122-163.ca-central-1.compute.amazonaws.com:3000";
    public static final String POST_URL = "http://ec2-3-99-226-175.ca-central-1.compute.amazonaws.com:3000";
    private static final String TAG = "GLOBAL_CLASS";
    public static UserProfile userProfile;
    private static GoogleSignInAccount account;
    private static Socket socket;
    private static Context appContext;
    private static String headerToken;
    private static String id;
    private static String givenName;
    private static String lastName;

    public static void cleanup() {
        account = null;
        id = null;
        givenName = null;
        if (socket != null) {
            socket.close();
            socket = null;
        }
        userProfile = null;
    }

    public static GoogleSignInAccount getAccount() {
        return account;
    }

    public static void setAccount(GoogleSignInAccount account) {
        GlobalUtil.account = account;
        setId(account.getId());
        setGivenName(account.getGivenName());
        setLastName(account.getFamilyName());
    }

    public static Socket getSocket() {
        return socket;
    }

    public static void setSocket(Socket socket) {
        GlobalUtil.socket = socket;
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static void setAppContext(Context appContext) {
        GlobalUtil.appContext = appContext;
    }

    public static void FetchUser() {
        if (userProfile != null) {
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = GlobalUtil.USER_URL + "/user/" + GlobalUtil.getId();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                (JSONObject response) -> {
                    Log.d(TAG, "fetchRestrictions: " + response);
                    JSONObject user;
                    try {
                        user = response.getJSONObject("user");
                        GlobalUtil.userProfile = new UserProfile(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    public static String getHeaderToken() {
        return headerToken;
    }

    public static void setHeaderToken(String headerToken) {
        GlobalUtil.headerToken = headerToken;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        GlobalUtil.id = id;
    }

    public static String getGivenName() {
        return givenName;
    }

    public static void setGivenName(String givenName) {
        GlobalUtil.givenName = givenName;
    }

    public static String getLastName() {
        return lastName;
    }

    public static void setLastName(String lastName) {
        GlobalUtil.lastName = lastName;
    }
}

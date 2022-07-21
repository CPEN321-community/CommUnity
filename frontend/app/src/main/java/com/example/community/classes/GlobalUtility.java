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

import io.socket.client.Socket;

public class GlobalUtility {
    private static final String TAG = "GLOBAL_CLASS";
    private static GoogleSignInAccount account;
    private static Socket socket;
    private static Context appContext;
    public static UserProfile userProfile;

    //    public static final String CHAT_URL = "http://10.0.2.2:3000";
    //    public static final String USER_URL = "http://10.0.2.2:8080";
    //    public static final String POST_URL = "http://10.0.2.2:8081";
    public static final String CHAT_URL = "http://ec2-35-183-28-141.ca-central-1.compute.amazonaws.com:3000";
    public static final String USER_URL = "http://ec2-3-96-168-213.ca-central-1.compute.amazonaws.com:3000";
    public static final String POST_URL = "http://ec2-35-183-145-212.ca-central-1.compute.amazonaws.com:3000";


    public static void cleanup() {
        account = null;
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
        Global.account = account;
    }

    public static Socket getSocket() {
        return socket;
    }

    public static void setSocket(Socket socket) {
        Global.socket = socket;
    }


    public static Context getAppContext() {
        return appContext;
    }

    public static void setAppContext(Context appContext) {
        Global.appContext = appContext;
    }

    public static void FetchUser() {
        if (userProfile != null) {
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = Global.USER_URL + "/user/" + Global.getAccount().getId();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                (JSONObject response) -> {
                    Log.d(TAG, "fetchRestrictions: " + response);
                    JSONObject user;
                    try {
                        user = response.getJSONObject("user");
                        Global.userProfile = new UserProfile(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, "fetchLeaderboard: " + error);
                });
        queue.add(request);

    }
}

package com.example.community.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.community.MainActivity;
import com.example.community.R;
import com.example.community.VolleyCallBack;
import com.example.community.classes.CustomJSONObjectRequest;
import com.example.community.classes.GlobalUtil;
import com.example.community.classes.LoginUtils;
import com.example.community.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "LOGIN_ACTIVITY";
    final String PREFS_NAME = "firstLogin";
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//      Set app context so Volley Utils can reference some context.
        GlobalUtil.setAppContext(getApplicationContext());
        mGoogleSignInClient = LoginUtils.GetGoogleClient(this);
        GoogleSignInAccount account = LoginUtils.GetLastSignedInAccount(this);
        updateUI(account);

        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_login);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    protected void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void finishLogin(GoogleSignInAccount account) {
        GlobalUtil.setAccount(account);
        GlobalUtil.setHeaderToken(account.getIdToken());
        GlobalUtil.FetchUser();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        Intent nextIntent;

        if (settings.getBoolean("firstTime", true)) {
            //TODO take to intro screen
            nextIntent = new Intent(LoginActivity.this, MainActivity.class);
        } else {
            nextIntent = new Intent(LoginActivity.this, MainActivity.class);
        }
        startActivity(nextIntent);
        finish();
    }

    private void handleLoginError() {
        // TODO Pretty-ify toast
        Toast errorToast = Toast.makeText(this, "Failed to create user", Toast.LENGTH_SHORT);
        errorToast.show();
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            GlobalUtil.setAccount(account);
            GlobalUtil.setHeaderToken(account.getIdToken());
            GlobalUtil.FetchUser();
//          If User doesn't exist, create it in the Database
            userDoesExist(account, new LoginCallback() {
                public void onSuccess(boolean exists) {
                    Log.d(TAG, "onSuccess: " + exists);
                    if (!exists) {
                        createUser(account, new LoginCallback() {
                            @Override
                            public void onError(VolleyError error) {
                                handleLoginError();
                            }

                            @Override
                            public void onSuccess() {
                                finishLogin(account);
                            }
                        });
                    } else {
                        finishLogin(account);
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    handleLoginError();
                }
            });

        }
    }

    private void createUser(GoogleSignInAccount account, VolleyCallBack volleyCallBack) {
        Log.d(TAG, "createUser: Start");
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = GlobalUtil.USER_URL + "/user";
        JSONObject data = new JSONObject();
        try {
            data.put("userId", account.getId());
            data.put("firstName", account.getGivenName());
            data.put("lastName", account.getFamilyName());
            data.put("email", account.getEmail());
            data.put("profilePicture", account.getPhotoUrl());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJSONObjectRequest request = new CustomJSONObjectRequest(Request.Method.POST, url, data,
                response -> {
                    Log.d(TAG, "createUser: " + response);
                    volleyCallBack.onSuccess();
                },
                error -> {
                    Log.e(TAG, "createUser: " + error);
                    volleyCallBack.onError();
                }
        );
        queue.add(request);

    }

    private void userDoesExist(GoogleSignInAccount account, VolleyCallBack callback) {
        String uid = account.getId();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = GlobalUtil.USER_URL + "/user" + "/" + uid;
        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.GET, url,
                null,
                response -> {
                    Log.d(TAG, "userDoesExist: successResponse");
                    callback.onSuccess(!response.isNull("user"));
                },
                error -> {
                    if (error.networkResponse.statusCode == 404) {
                        callback.onSuccess(false);
                    }
                    else {
                    Log.e(TAG, "userDoesExist: " + error);
                    callback.onError(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("token", account.getIdToken());
                return headers;
            }
        };
        queue.add(sr);
    }

}



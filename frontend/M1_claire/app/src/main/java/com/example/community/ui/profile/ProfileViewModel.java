package com.example.community.ui.profile;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.community.classes.DietaryRestriction;
import com.example.community.classes.Global;
import com.example.community.classes.Preference;
import com.example.community.classes.Stats;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfileViewModel extends AndroidViewModel {

    private static final String TAG = "PROFILE_VIEW_MODEL";
    private final MutableLiveData<ArrayList<DietaryRestriction>> mRestrictionList;
    private final MutableLiveData<Stats> mStats;
    private final Application application;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        this.mRestrictionList = new MutableLiveData<>(new ArrayList<>());
        this.mStats = new MutableLiveData<>();
        fetchRestrictions();
        fetchStats();
    }

    public LiveData<ArrayList<DietaryRestriction>> getRestrictions() {
        return mRestrictionList;
    }

    private void fetchRestrictions() {
        RequestQueue queue = Volley.newRequestQueue(this.application);
        String url = "http://10.0.2.2:8080/user/" + Global.getAccount().getId();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                (JSONObject response) -> {
                    Log.d(TAG, "fetchRestrictions: " + response);
                    JSONObject user;
                    ArrayList<Preference> preferences = new ArrayList<>();
                    JSONArray prefJSON;

                    try {
                        user = response.getJSONObject("user");
                        prefJSON = user.getJSONArray("preferences");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }

                    for (int i = 0; i < prefJSON.length(); i++) {
                        try {
                            Preference preference = new Preference(prefJSON.getJSONObject(i));
                            preferences.add(preference);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d(TAG, "fetchRestrictions: " + preferences);
                    ArrayList<DietaryRestriction> diet = new ArrayList<>();
                    for (Preference p: preferences) {
                        if (p.isDietary()) {
                            diet.add(new DietaryRestriction(p));
                        }
                    }

                    mRestrictionList.setValue(diet);
                },
                error -> {
                    Log.e(TAG, "fetchLeaderboard: " + error);
                });
        queue.add(request);
    }

    private void fetchStats() {
        RequestQueue queue = Volley.newRequestQueue(this.application);
        String url = "http://10.0.2.2:8080/leaderboard/" + Global.getAccount().getId();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                (JSONObject response) -> {
                    Log.d(TAG, "fetchLeaderboard: " + response);
                    Stats leaderBoardStats = new Stats(response);
                    Log.d(TAG, "fetchLeaderboard: " + leaderBoardStats);
                    mStats.setValue(leaderBoardStats);
                },
                error -> {
                    Log.e(TAG, "fetchLeaderboard: " + error);
                });
        queue.add(request);
    }

    public LiveData<Stats> getStats() {
        return mStats;
    }
}
package com.example.community.ui.profile;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
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
        fetchUser();
    }

    public LiveData<ArrayList<DietaryRestriction>> getRestrictions() {
        return mRestrictionList;
    }

    private void parseRestrictions(JSONArray prefJSON) {
        ArrayList<Preference> preferences = new ArrayList<>();

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
        for (Preference p : preferences) {
            if (p.isDietary()) {
                diet.add(new DietaryRestriction(p));
            }
        }

        mRestrictionList.setValue(diet);
    }

    private void parseStats(JSONObject leaderboardJSON) {
        Log.d(TAG, "fetchLeaderboard: " + leaderboardJSON);
        Stats leaderBoardStats = new Stats(leaderboardJSON);
        Log.d(TAG, "fetchLeaderboard: " + leaderBoardStats);
        mStats.setValue(leaderBoardStats);
    }

    private void fetchUser() {
        RequestQueue queue = Volley.newRequestQueue(this.application);
        String url = Global.USER_URL + "/user/" + Global.getAccount().getId();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                (JSONObject response) -> {
                    Log.d(TAG, "fetchRestrictions: " + response);
                    JSONObject user;
                    ArrayList<Preference> preferences = new ArrayList<>();
                    JSONArray prefJSON;
                    JSONObject leaderboardJSON;

                    try {
                        user = response.getJSONObject("user");
                        leaderboardJSON = user.getJSONObject("leaderboard");
                        prefJSON = user.getJSONArray("preferences");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }

                    parseRestrictions(prefJSON);
                    parseStats(leaderboardJSON);
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
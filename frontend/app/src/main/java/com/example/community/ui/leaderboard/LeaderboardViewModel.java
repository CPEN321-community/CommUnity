package com.example.community.ui.leaderboard;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.community.classes.CustomJSONArrayRequest;
import com.example.community.classes.CustomJSONObjectRequest;
import com.example.community.classes.GlobalUtil;
import com.example.community.classes.UserWithScore;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class LeaderboardViewModel extends AndroidViewModel {

    private static final String TAG = "LEADERBOARD_MODEL";
    private final MutableLiveData<ArrayList<UserWithScore>> mList;
    private final MutableLiveData<Integer> rank = new MutableLiveData<>(0);
    private final Application application;

    public LeaderboardViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        mList = new MutableLiveData<>(new ArrayList<>());
        fetchLeaderboard();
    }

    public LiveData<ArrayList<UserWithScore>> getList() {
        return mList;
    }

    protected void fetchLeaderboard() {
        RequestQueue queue = Volley.newRequestQueue(this.application);
        String url = GlobalUtil.USER_URL + "/rank/top/10";

        CustomJSONArrayRequest request = new CustomJSONArrayRequest(Request.Method.GET,
                url,
                null,
                (JSONArray response) -> {
                    Log.d(TAG, "fetchLeaderboard: " + response);
                    ArrayList<UserWithScore> users = new ArrayList<>();
                    if (response != null) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                UserWithScore currUser = new UserWithScore(response.getJSONObject(i));
                                users.add(currUser);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    mList.setValue(users);
                },
                error -> {
                    Log.e(TAG, "fetchLeaderboard: " + error);
                });
        queue.add(request);
    }

    private void getMyRank() {
        RequestQueue queue = Volley.newRequestQueue(this.application);
        String url = GlobalUtil.USER_URL + "/rank/" + GlobalUtil.getId();

        CustomJSONObjectRequest request = new CustomJSONObjectRequest(Request.Method.GET,
                url,
                null,
                (response) -> {
                    Log.d(TAG, "fetchLeaderboard: " + response);
                    if (response != null) {
                        try {
                            int rank = response.getInt("rank");
                            this.rank.postValue(rank);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> {
                    Log.e(TAG, "fetchLeaderboard: " + error);
                });
        queue.add(request);
    }

    public MutableLiveData<Integer> getRankData() {
        return this.rank;
    }

    public boolean UserIsTopTen() {
        return this.rank.getValue() <= 10;
    }

    public void setData(ArrayList<UserWithScore> users) {
        this.mList.postValue(users);
    }
}
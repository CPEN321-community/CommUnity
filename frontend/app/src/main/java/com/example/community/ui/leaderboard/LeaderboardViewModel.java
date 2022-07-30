package com.example.community.ui.leaderboard;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.community.classes.GlobalUtil;
import com.example.community.classes.UserWithScore;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LeaderboardViewModel extends AndroidViewModel {

    private static final String TAG = "LEADERBOARD_MODEL";
    private final MutableLiveData<ArrayList<UserWithScore>> mList;
    private final Application application;

    public LeaderboardViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        mList = new MutableLiveData<>();
        fetchLeaderboard();
    }

    public LiveData<ArrayList<UserWithScore>> getList() {
        return mList;
    }

    protected void fetchLeaderboard() {
        RequestQueue queue = Volley.newRequestQueue(this.application);
        String url = GlobalUtil.USER_URL + "/rank/top/10";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
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
//                    callback.onError(error);
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

    public void setData(ArrayList<UserWithScore> users) {
        this.mList.postValue(users);
    }
}
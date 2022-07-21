package com.example.community.request_list;

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
import com.example.community.classes.ReqPostObj;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ReqPostViewModel extends AndroidViewModel {
    private static final String TAG = "REQ_POST_MODEL";
    private final MutableLiveData<ArrayList<ReqPostObj>> mList;
    private final Application application;

    public ReqPostViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        mList = new MutableLiveData<>();
        fetchReqPosts();
    }

    public LiveData<ArrayList<ReqPostObj>> getList() {
        return mList;
    }

    protected void fetchReqPosts() {
        RequestQueue queue = Volley.newRequestQueue(this.application);
        String url = GlobalUtil.POST_URL + "/communitypost/requests";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                (JSONArray response) -> {
                    Log.d(TAG, "fetchingRequestPosts: " + response);
                    ArrayList<ReqPostObj> reqPosts = new ArrayList<>();
                    if (response != null) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                ReqPostObj currReq = new ReqPostObj(response.getJSONObject(i));
                                reqPosts.add(currReq);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    mList.setValue(reqPosts);
                },
                error -> {
                    Log.e(TAG, "fetchRequestPostsError: " + error);
//                    callback.onError(error);
                });
        queue.add(request);
    }
}

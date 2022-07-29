package com.example.community.offer_list;

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
import com.example.community.classes.OfferPostObj;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OfferPostViewModel extends AndroidViewModel {
    private static final String TAG = "OFFER_POST_MODEL";
    private final MutableLiveData<ArrayList<OfferPostObj>> mList;
    private final Application application;

    public OfferPostViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        mList = new MutableLiveData<>();
        fetchOfferPosts();
    }

    public LiveData<ArrayList<OfferPostObj>> getList() {
        return mList;
    }

    protected void fetchOfferPosts() {
        RequestQueue queue = Volley.newRequestQueue(this.application);
        String url = GlobalUtil.POST_URL + "/communitypost/offers";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                (JSONArray response) -> {
                    Log.d(TAG, "fetchingOfferPosts: " + response);
                    ArrayList<OfferPostObj> offerPosts = new ArrayList<>();
                    if (response != null) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                OfferPostObj currOffer = new OfferPostObj(response.getJSONObject(i));
                                offerPosts.add(currOffer);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    mList.setValue(offerPosts);
                },
                error -> {
                    Log.e(TAG, "fetchRequestPostsError: " + error);
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

}

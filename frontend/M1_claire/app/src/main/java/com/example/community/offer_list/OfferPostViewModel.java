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
import com.example.community.classes.OfferPostObj;
import com.example.community.classes.ReqPostObj;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

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
        String url = "http://10.0.2.2:8081/communitypost/offers";

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
                });
        queue.add(request);
    }

}

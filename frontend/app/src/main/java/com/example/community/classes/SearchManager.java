package com.example.community.classes;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchManager {
    private static final String TAG = "SEARCH_MANAGER";
    private static final MutableLiveData<ArrayList<ReqPostObj>> requestPosts = new MutableLiveData<>();
    private static final MutableLiveData<ArrayList<OfferPostObj>> offerPosts = new MutableLiveData<>();
    private static final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private static String query = "";

    private static void PerformRequestSearch(Context ctx) {
        String url = GlobalUtil.POST_URL + "/communitypost/requests/search/" + query;
        RequestQueue queue = Volley.newRequestQueue(ctx);
        loading.setValue(true);
        CustomJSONArrayRequest request = new CustomJSONArrayRequest(Request.Method.GET,
                url,
                null,
                (JSONArray response) -> {
                    Log.d(TAG, "requestSearch: " + response);
                    if (response.length() == 0) {
                        loading.setValue(false);
                        requestPosts.setValue(new ArrayList<>());
                        return;
                    }
                    ArrayList<ReqPostObj> searchResults = new ArrayList<>();
                    Log.d(TAG, "PerformRequestSearch: " + response);
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jobj = response.getJSONObject(i);
                            Log.d(TAG, "PerformRequestSearch: jobj" + jobj);
                            ReqPostObj currReqPost = new ReqPostObj(response.getJSONObject(i));
                            searchResults.add(currReqPost);
                            Log.d(TAG, "PerformRequestSearch: " + currReqPost.createdAt);
                        } catch (JSONException e) {
                            Log.e(TAG, "getChats: " + e);
                            e.printStackTrace();
                        }
                    }
                    loading.setValue(false);
                    requestPosts.setValue(searchResults);
                },
                error -> {
                    loading.setValue(false);
                    Log.e(TAG, "getChats: " + error);
                });
        queue.add(request);
    }

    private static void PerformOfferSearch(Context ctx) {
        String url = GlobalUtil.POST_URL + "/communitypost/offers/search/" + query;
        loading.setValue(true);
        RequestQueue queue = Volley.newRequestQueue(ctx);
        CustomJSONArrayRequest request = new CustomJSONArrayRequest(Request.Method.GET,
                url,
                null,
                (JSONArray response) -> {
                    Log.d(TAG, "requestSearch: " + response);
                    if (response.length() == 0) {
                        loading.setValue(false);
                        offerPosts.setValue(new ArrayList<>());
                        return;
                    }
                    ArrayList<OfferPostObj> searchResults = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            OfferPostObj currReqPost = new OfferPostObj(response.getJSONObject(i));
                            searchResults.add(currReqPost);
                        } catch (JSONException e) {
                            Log.e(TAG, "getChats: " + e);
                            e.printStackTrace();
                        }
                    }
                    loading.setValue(false);
                    offerPosts.setValue(searchResults);
                },
                error -> {
                    loading.setValue(false);
                    Log.e(TAG, "getChats: " + error);
                });
        queue.add(request);
    }

    private static void GetAllOfferPosts(Context ctx) {
        String url = GlobalUtil.POST_URL + "/communitypost/offers/";
        loading.setValue(true);
        RequestQueue queue = Volley.newRequestQueue(ctx);
        CustomJSONArrayRequest request = new CustomJSONArrayRequest(Request.Method.GET,
                url,
                null,
                (JSONArray response) -> {
                    Log.d(TAG, "requestSearch: " + response);
                    if (response.length() == 0) {
                        loading.setValue(false);
                        offerPosts.setValue(new ArrayList<>());
                        return;
                    }
                    ArrayList<OfferPostObj> searchResults = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            OfferPostObj currReqPost = new OfferPostObj(response.getJSONObject(i));
                            searchResults.add(currReqPost);
                        } catch (JSONException e) {
                            Log.e(TAG, "getChats: " + e);
                            e.printStackTrace();
                        }
                    }
                    loading.setValue(false);
                    offerPosts.setValue(searchResults);
                },
                error -> {
                    loading.setValue(false);
                    Log.e(TAG, "getChats: " + error);
                });
        queue.add(request);
    }

    private static void GetAllRequestPosts(Context ctx) {
        String url = GlobalUtil.POST_URL + "/communitypost/requests/";
        loading.setValue(true);
        RequestQueue queue = Volley.newRequestQueue(ctx);
        CustomJSONArrayRequest request = new CustomJSONArrayRequest(Request.Method.GET,
                url,
                null,
                (JSONArray response) -> {
                    Log.d(TAG, "requestSearch: " + response);
                    if (response.length() == 0) {
                        loading.setValue(false);
                        requestPosts.setValue(new ArrayList<>());
                        return;
                    }
                    ArrayList<ReqPostObj> searchResults = new ArrayList<>();
                    Log.d(TAG, "PerformRequestSearch: " + response);
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jobj = response.getJSONObject(i);
                            Log.d(TAG, "PerformRequestSearch: jobj" + jobj);
                            ReqPostObj currReqPost = new ReqPostObj(response.getJSONObject(i));
                            searchResults.add(currReqPost);
                            Log.d(TAG, "PerformRequestSearch: " + currReqPost.createdAt);
                        } catch (JSONException e) {
                            Log.e(TAG, "getChats: " + e);
                            e.printStackTrace();
                        }
                    }
                    loading.setValue(false);
                    requestPosts.setValue(searchResults);
                },
                error -> {
                    loading.setValue(false);
                    Log.e(TAG, "getChats: " + error);
                });
        queue.add(request);
    }

    public static void search(Context ctx) {
        ArrayList<Tag> tags = TagManager.getClickedTags();
        if (tags.size() > 0) {
            PerformRequestTagSearch(ctx);
            PerformOfferTagSearch(ctx);
            return;
        }
        if (!"".equals(query)) {
            PerformRequestSearch(ctx);
            PerformOfferSearch(ctx);

        } else {
            GetAllOfferPosts(ctx);
            GetAllRequestPosts(ctx);
        }
    }

    private static void PerformRequestTagSearch(Context ctx) {
        String url = GlobalUtil.POST_URL + "/communitypost/requestTags";
        loading.setValue(true);
        RequestQueue queue = Volley.newRequestQueue(ctx);
        JSONObject body = new JSONObject();
        try {
            body.put("tagList", TagManager.getJSONArr());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomJSONObjectRequest request = new CustomJSONObjectRequest(Request.Method.PUT,
                url,
                body,
                (JSONObject res) -> {
                    JSONArray response = null;
                    try {
                        response = res.getJSONArray("results");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "requestSearch: " + response);
                    if (response.length() == 0) {
                        loading.setValue(false);
                        offerPosts.setValue(new ArrayList<>());
                        return;
                    }
                    ArrayList<ReqPostObj> searchResults = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            ReqPostObj currReqPost = new ReqPostObj(response.getJSONObject(i));
                            searchResults.add(currReqPost);
                        } catch (JSONException e) {
                            Log.e(TAG, "getChats: " + e);
                            e.printStackTrace();
                        }
                    }
                    loading.setValue(false);
                    requestPosts.setValue(searchResults);
                },
                error -> {
                    loading.setValue(false);
                    Log.e(TAG, "getChats: " + error);
                });
        queue.add(request);
    }

    private static void PerformOfferTagSearch(Context ctx) {
        String url = GlobalUtil.POST_URL + "/communitypost/offerTags";
        loading.setValue(true);
        RequestQueue queue = Volley.newRequestQueue(ctx);
        JSONObject body = new JSONObject();
        try {
            body.put("tagList", TagManager.getJSONArr());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomJSONObjectRequest request = new CustomJSONObjectRequest(Request.Method.PUT,
                url,
                body,
                (JSONObject res) -> {
                    JSONArray response = null;
                    try {
                        response = res.getJSONArray("results");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "requestSearch: " + response);
                    if (response.length() == 0) {
                        loading.setValue(false);
                        offerPosts.setValue(new ArrayList<>());
                        return;
                    }
                    ArrayList<OfferPostObj> searchResults = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            OfferPostObj currReqPost = new OfferPostObj(response.getJSONObject(i));
                            searchResults.add(currReqPost);
                        } catch (JSONException e) {
                            Log.e(TAG, "getChats: " + e);
                            e.printStackTrace();
                        }
                    }
                    loading.setValue(false);
                    offerPosts.setValue(searchResults);
                },
                error -> {
                    loading.setValue(false);
                    Log.e(TAG, "getChats: " + error);
                });
        queue.add(request);
    }

    public static String getQuery() {
        return query;
    }

    public static void setQuery(String q) {
        query = q;
    }

    public static boolean isLoading() {
        return Boolean.TRUE.equals(loading.getValue());
    }

    public static MutableLiveData<Boolean> getLoadingData() {
        return loading;
    }

    public static ArrayList<OfferPostObj> getOffers() {
        return offerPosts.getValue();
    }

    public static ArrayList<ReqPostObj> getRequests() {
        return requestPosts.getValue();
    }

    public static MutableLiveData<ArrayList<ReqPostObj>> getRequestLiveData() {
        return requestPosts;
    }

    public static MutableLiveData<ArrayList<OfferPostObj>> getOfferPostLiveData() {
        return offerPosts;
    }


}

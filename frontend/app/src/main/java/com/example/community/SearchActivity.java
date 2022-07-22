package com.example.community;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.community.classes.GlobalUtil;
import com.example.community.classes.OfferPostObj;
import com.example.community.classes.ReqPostObj;
import com.example.community.classes.Tags;
import com.example.community.offer_list.OfferPostAdapter;
import com.example.community.request_list.ReqPostAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SEARCH_ACTIVITY";
    private EditText searchField;
    private final MutableLiveData<ArrayList<ReqPostObj>> mRequestPosts = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<OfferPostObj>> mOfferPosts = new MutableLiveData<>();
    private ListView reqPostResultList;
    private ListView offerPostResultList;
    private Tags tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.searchField = findViewById(R.id.search_text_input);
        TextView fruit = findViewById(R.id.fruit);
        TextView vegetable = findViewById(R.id.vegetable);
        TextView nut = findViewById(R.id.nut);

        Tags tags = new Tags(fruit, vegetable, nut);
        this.tags = tags;

        // Ack: https://stackoverflow.com/questions/4165414/how-to-hide-soft-keyboard-on-android-after-clicking-outside-edittext
        this.searchField.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(view);
            }
        });
        this.reqPostResultList = (ListView) findViewById(R.id.req_post_result_list);
        this.offerPostResultList = (ListView) findViewById(R.id.offer_post_result_list);
        ImageButton searchButton = findViewById(R.id.submit_search_button);
        Switch reqOfferSwitch = findViewById(R.id.req_or_offer_switch);
        reqOfferSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                this.reqPostResultList.setVisibility(View.INVISIBLE);
                this.offerPostResultList.setVisibility(View.VISIBLE);
                reqOfferSwitch.setText("Offers");
            } else {
                this.reqPostResultList.setVisibility(View.VISIBLE);
                this.offerPostResultList.setVisibility(View.INVISIBLE);
                reqOfferSwitch.setText("Requests");
            }
        });
        searchButton.setOnClickListener(v -> {
            hideKeyboard(this.searchField);
            this.PerformSearch();
            Log.d(TAG, "onCreate: Searching!");
        });
        this.searchField.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                //do what you want on the press of 'done'
                searchButton.performClick();
                searchField.clearFocus();
            }
            return false;
        });
        this.mOfferPosts.observe(this, newOfferPostList -> {
            OfferPostAdapter offerAdapter = new OfferPostAdapter(this, newOfferPostList);
            this.offerPostResultList.setAdapter(offerAdapter);
        });
        this.mRequestPosts.observe(this, newReqPostList -> {
            Log.d(TAG, "onCreate: " + newReqPostList);
            ReqPostAdapter postAdapter = new ReqPostAdapter(this, newReqPostList);
            this.reqPostResultList.setAdapter(postAdapter);
        });

    }

    private void PerformRequestSearch() {
        String query = this.searchField.getText().toString();
        String url = GlobalUtil.POST_URL + "/communitypost/requests/search/" + query;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                (JSONArray response) -> {
                    Log.d(TAG, "requestSearch: " + response);
                    if (response.length() == 0) {
//                        TODO tell user they have no chats
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

                    mRequestPosts.setValue(searchResults);
                },
                error -> {
                    Log.e(TAG, "getChats: " + error);
                })  {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("token", GlobalUtil.getHeaderToken());
                return headers;
            }
        };
        queue.add(request);
    }

    private void PerformOfferSearch() {
        String query = this.searchField.getText().toString();
        String url = GlobalUtil.POST_URL + "/communitypost/offers/search/" + query;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                (JSONArray response) -> {
                    Log.d(TAG, "requestSearch: " + response);
                    if (response.length() == 0) {
//                        TODO tell user they have no chats
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

                    mOfferPosts.setValue(searchResults);
                },
                error -> {
                    Log.e(TAG, "getChats: " + error);
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

    private void PerformSearch() {
        if (!this.searchField.getText().toString().equals("")) {
            this.mOfferPosts.setValue(new ArrayList<>());
            this.mRequestPosts.setValue(new ArrayList<>());
            PerformRequestSearch();
            PerformOfferSearch();
        } else {
            Log.d(TAG, "PerformSearch: TAGSEARCH");
            PerformTagSearch();
        }
    }

    private void PerformTagSearch() {
        PerformOfferTagSearch();
        PerformRequestTagSearch();
    }

    private void PerformOfferTagSearch() {
        String url = GlobalUtil.POST_URL + "/communitypost/offerTags/";
        JSONObject body = new JSONObject();
        try {
            body.put("tagList", this.tags.getJSONArr());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "PerformOfferTagSearch: " + e);
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT,
                url,
                body,
                (JSONObject response) -> {
                    Log.d(TAG, "tagSearch: " + response);
                    if (response.length() == 0) {
                        return;
                    }
                    JSONArray results;
                    try {
                        results = response.getJSONArray("results");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                    ArrayList<OfferPostObj> searchResults = new ArrayList<>();
                    for (int i = 0; i < results.length(); i++) {
                        try {
                            OfferPostObj currReqPost = new OfferPostObj(results.getJSONObject(i));
                            searchResults.add(currReqPost);
                        } catch (JSONException e) {
                            Log.e(TAG, "tagSearch: " + e);
                            e.printStackTrace();
                        }
                    }

                    mOfferPosts.setValue(searchResults);
                },
                error -> {
                    Log.e(TAG, "tagSearch: " + error);
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

    private void PerformRequestTagSearch() {
        String url = GlobalUtil.POST_URL + "/communitypost/requestTags";
        JSONObject body = new JSONObject();
        try {
            body.put("tagList", this.tags.getJSONArr());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "PerformOfferTagSearch: " + e);
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT,
                url,
                body,
                (JSONObject response) -> {
                    Log.d(TAG, "tagSearch: " + response);
                    if (response.length() == 0) {
                        return;
                    }
                    JSONArray results;
                    try {
                        results = response.getJSONArray("results");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                    ArrayList<ReqPostObj> searchResults = new ArrayList<>();
                    for (int i = 0; i < results.length(); i++) {
                        try {
                            ReqPostObj currReqPost = new ReqPostObj(results.getJSONObject(i));
                            searchResults.add(currReqPost);
                        } catch (JSONException e) {
                            Log.e(TAG, "tagSearch: " + e);
                            e.printStackTrace();
                        }
                    }

                    Log.d(TAG, "PerformRequestTagSearch: " + searchResults);

                    mRequestPosts.setValue(searchResults);
                },
                error -> {
                    Log.e(TAG, "getChats: " + error);
                })  {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("token", GlobalUtil.getHeaderToken());
                return headers;
            }
        };
        queue.add(request);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
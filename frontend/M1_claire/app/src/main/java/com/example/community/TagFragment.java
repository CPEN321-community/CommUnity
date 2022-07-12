package com.example.community;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.community.classes.Global;
import com.example.community.classes.Tag;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class TagFragment extends Fragment {

    private static final String TAG = "TAG_FRAGMENT";
    private TagListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: hello wotld?");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tag_list, container, false);
        Log.d(TAG, "onCreate: hello wotld?");
        FetchTags();

        ImageButton addTagButton = view.findViewById(R.id.add_tag_button);
        addTagButton.setOnClickListener(v -> {
            Log.d(TAG, "onCreateView: Hello from button");
        });
        EditText tagInput = view.findViewById(R.id.add_tag_input);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            this.adapter = new TagListAdapter();
            recyclerView.setAdapter(this.adapter);
        }

        return view;
    }

    public ArrayList<Tag> getClickedTags() {
        return this.adapter.getClickedTags();
    }

    private void FetchTags() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = Global.POST_URL + "/communitypost/tags";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                (JSONArray response) -> {
                    Log.d(TAG, "fetchTags: " + response);
                    ArrayList<Tag> tags = new ArrayList<>();
                    if (response != null) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                String tagName = response.getString(i);
                                Tag currTag = new Tag(tagName);
                                tags.add(currTag);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        this.adapter.setItems(tags);
                    }

                },
                error -> {
                    Log.e(TAG, "fetchTags: " + error);
                });
        queue.add(request);
    }
}
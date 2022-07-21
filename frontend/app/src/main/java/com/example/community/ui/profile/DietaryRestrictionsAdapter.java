package com.example.community.ui.profile;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.community.R;
import com.example.community.classes.DietaryRestriction;
import com.example.community.classes.GlobalUtil;

import java.util.ArrayList;

public class DietaryRestrictionsAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<DietaryRestriction> restrictions;
    private final LayoutInflater inflater;
    private final String TAG = "DIETARY_RESTRICTIONS_ADAPTER";

    public DietaryRestrictionsAdapter(Context applicationContext, ArrayList<DietaryRestriction> dietaryRestrictions) {
        this.context = applicationContext;
        this.restrictions = dietaryRestrictions;
        inflater = (LayoutInflater.from(applicationContext));
    }

    public DietaryRestrictionsAdapter(Context applicationContext) {
        this.context = applicationContext;
        this.restrictions = new ArrayList<>();
        inflater = (LayoutInflater.from(applicationContext));
    }

    public void updateList(ArrayList<DietaryRestriction> newRestrictons) {
        Log.d(TAG, "updateList: " + newRestrictons);
        this.restrictions.clear();
        this.restrictions.addAll(newRestrictons);
    }

    public void notifySelf() {
        ((Activity) context).runOnUiThread(() -> {
            this.notifyDataSetChanged();
        });
    }

    @Override
    public int getCount() {
        return this.restrictions.size();
    }

    @Override
    public Object getItem(int i) {
        return this.restrictions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View newView = view;
        newView = inflater.inflate(R.layout.fragment_dietary_restriction, null);
        TextView name = (TextView) newView.findViewById(R.id.restriction_name);
        ImageButton removeButton = newView.findViewById(R.id.remove_restriction_button);
        DietaryRestriction restriction = this.restrictions.get(i);
        removeButton.setOnClickListener(v -> {
            removePreference(restriction, i);
        });
        Log.d(TAG, "getView: " + restriction.name);

        name.setText(restriction.name);

        return newView;
    }

    private void removePreference(DietaryRestriction pref, int index) {
        RequestQueue queue = Volley.newRequestQueue(this.context);
        String url = GlobalUtil.USER_URL + "/user/preference/" + pref.getId();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE,
                url,
                null,
                res -> {
                    this.restrictions.remove(index);
                    this.notifySelf();
                },
                error -> {
                    Log.e(TAG, "fetchLeaderboard: " + error);
                });
        queue.add(request);

    }


}

package com.example.community.ui.profile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.community.R;
import com.example.community.classes.DietaryRestriction;
import com.example.community.classes.UserWithScore;
import com.example.community.classes.Utils;

import java.util.ArrayList;
import java.util.Objects;

public class DietaryRestrictionsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<DietaryRestriction> restrictions;
    private LayoutInflater inflater;
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
        view = inflater.inflate(R.layout.fragment_dietary_restriction, null);
        TextView name = (TextView) view.findViewById(R.id.restriction_name);
        DietaryRestriction restriction = this.restrictions.get(i);
        Log.d(TAG, "getView: " + restriction.name);

        name.setText(restriction.name);

        return view;
    }



}

package com.example.community.ui.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.community.offer_list.OfferPostFragment;
import com.example.community.request_list.ReqPostFragment;

public class PostTabAdapter extends FragmentStateAdapter {

    private static final String TAG = "POST_TAB_ADAPTER";

    public PostTabAdapter(FragmentManager fm, Lifecycle lifecycle) {
        super(fm, lifecycle);
        Log.d(TAG, "PostTabAdapter: ");
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d(TAG, "createFragment: " + position);
        Fragment toReturn;
        if (position == 0) {
            toReturn = new OfferPostFragment();
        } else {
            toReturn = new ReqPostFragment();
        }
        return toReturn;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}

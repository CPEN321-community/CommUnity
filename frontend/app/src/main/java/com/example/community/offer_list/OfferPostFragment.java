package com.example.community.offer_list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.example.community.VolleyCallBack;
import com.example.community.classes.SearchManager;
import com.example.community.databinding.FragmentPostListBinding;

import java.util.ArrayList;


public class OfferPostFragment extends Fragment {

    private static final String TAG = "OFFER_POST_FRAGMENT";
    private FragmentPostListBinding binding_offer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding_offer = FragmentPostListBinding.inflate(inflater, container, false);

        View root = binding_offer.getRoot();
        final RecyclerView listView = binding_offer.postList;
        LinearLayoutManager manager = new LinearLayoutManager(requireContext());
        listView.setLayoutManager(manager);
        SwipeRefreshLayout refresher = binding_offer.pullToRefresh;
        refresher.setOnRefreshListener(() -> {
            Log.d(TAG, "onCreateView: Refreshing");
                SearchManager.search(requireContext());
                Observer<Boolean> observer = new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean loading) {
                        if (!loading) {
                            SearchManager.getLoadingData().removeObserver(this);
                            refresher.setRefreshing(false);
                        }
                    }
                };
                SearchManager.getLoadingData().observe(getViewLifecycleOwner(), observer);

        });

        OfferPostAdapter adapter = new OfferPostAdapter(requireContext(), new ArrayList<>());
        listView.setAdapter(adapter);

        SearchManager.getOfferPostLiveData().observe(getViewLifecycleOwner(), offerList -> {
            adapter.setItems(offerList);
            adapter.notifyDataSetChanged();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding_offer = null;
    }
}
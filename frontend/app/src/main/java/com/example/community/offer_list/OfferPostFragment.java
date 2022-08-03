package com.example.community.offer_list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.example.community.VolleyCallBack;
import com.example.community.databinding.FragmentPostListBinding;


public class OfferPostFragment extends Fragment {

    private static final String TAG = "OFFER_POST_FRAGMENT";
    private FragmentPostListBinding binding_offer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        OfferPostViewModel offerPostViewModel = new ViewModelProvider(this).get(OfferPostViewModel.class);
        binding_offer = FragmentPostListBinding.inflate(inflater, container, false);

        View root = binding_offer.getRoot();
        SwipeRefreshLayout refresher = binding_offer.pullToRefresh;
        refresher.setOnRefreshListener(() -> {
            Log.d(TAG, "onCreateView: Refreshing");
            offerPostViewModel.fetchOfferPosts(new VolleyCallBack() {
                @Override
                public void onError(VolleyError error) {
                }

                @Override
                public void onSuccess(boolean b) {
                }

                @Override
                public void onSuccess() {
                    refresher.setRefreshing(false);
                }

                @Override
                public void onError() {
                    refresher.setRefreshing(false);
                }
            });
        });
        final RecyclerView listView = binding_offer.offerPostList;
        listView.setLayoutManager(new LinearLayoutManager(requireContext()));
        offerPostViewModel.getList().observe(getViewLifecycleOwner(), offerList -> {
            OfferPostAdapter adapter = new OfferPostAdapter(requireContext(),
                    offerList);
            listView.setAdapter(adapter);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding_offer = null;
    }
}
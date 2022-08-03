package com.example.community.offer_list;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.example.community.VolleyCallBack;
import com.example.community.databinding.FragmentOfferPostListBinding;


public class OfferPostFragment extends Fragment {

    private static final String TAG = "OFFER_POST_FRAGMENT";
    private FragmentOfferPostListBinding binding_offer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        OfferPostViewModel offerPostViewModel = new ViewModelProvider(this).get(OfferPostViewModel.class);
        binding_offer = FragmentOfferPostListBinding.inflate(inflater, container, false);

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
        binding_offer.addOfferPostButton.setOnClickListener(view -> {
            Intent addReqIntent = new Intent(requireActivity(), NewOfferForm.class);
            startActivity(addReqIntent);
        });
        final ListView listView = binding_offer.offerPostList;
        listView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        offerPostViewModel.getList().observe(getViewLifecycleOwner(), offerList -> {
            OfferPostAdapter adapter;
            adapter = new OfferPostAdapter(requireContext(),
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
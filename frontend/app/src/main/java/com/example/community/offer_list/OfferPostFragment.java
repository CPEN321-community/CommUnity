package com.example.community.offer_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.community.databinding.FragmentOfferPostBinding;

public class OfferPostFragment extends Fragment {

    private FragmentOfferPostBinding binding_offer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        OfferPostViewModel offerPostViewModel = new ViewModelProvider(this).get(OfferPostViewModel.class);
        binding_offer = FragmentOfferPostBinding.inflate(inflater, container, false);

        View root = binding_offer.getRoot();
        Button refreshButton = binding_offer.buttonRefreshOfferPosts;
        refreshButton.setOnClickListener(view -> {
            offerPostViewModel.fetchOfferPosts();
        });
        final ListView listView = binding_offer.offerPostList;
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
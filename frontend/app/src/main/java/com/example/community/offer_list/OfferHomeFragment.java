package com.example.community.offer_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.community.SearchActivity;
import com.example.community.databinding.FragmentOfferHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class OfferHomeFragment extends Fragment {
    private FragmentOfferHomeBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOfferHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ImageButton searchButton = binding.searchButton;
        searchButton.setOnClickListener(v -> {
            Intent searchIntent = new Intent(requireActivity(), SearchActivity.class);
            startActivity(searchIntent);
        });

        FloatingActionButton addNewOfferButton = binding.addOfferPostButton;
        addNewOfferButton.setOnClickListener(view -> {
            Intent addOfferIntent = new Intent(requireActivity(), NewOfferForm.class);
            startActivity(addOfferIntent);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
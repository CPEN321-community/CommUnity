package com.example.community.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.community.classes.DietaryRestriction;
import com.example.community.classes.Global;
import com.example.community.classes.Stats;
import com.example.community.classes.Utils;
import com.example.community.databinding.FragmentProfileBinding;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ImageView avatar = binding.profileAvatar;
        String profileImageURL = Global.getAccount().getPhotoUrl().toString();
        Utils.setImageWhenLoaded(requireContext(), profileImageURL, avatar);
        LiveData<ArrayList<DietaryRestriction>> restrictions = profileViewModel.getRestrictions();
        LiveData<Stats> stats = profileViewModel.getStats();
        ListView restrictionList = binding.dietaryRestrictionsList;
        restrictions.observe(getViewLifecycleOwner(), newRestrictons -> {
        DietaryRestrictionsAdapter adapter = new DietaryRestrictionsAdapter(requireContext());
            restrictionList.setAdapter(adapter);
            adapter.updateList(newRestrictons);
        });
        TextView offerText = binding.statOfferpost;
        TextView requestText = binding.statRequestpost;
        TextView scoreText = binding.statScore;
        stats.observe(getViewLifecycleOwner(), newStats -> {
            offerText.setText("Offer Posts: " + newStats.offerPosts);
            requestText.setText("Request Posts: " + newStats.requestPosts);
            scoreText.setText("Score: " + newStats.score);
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
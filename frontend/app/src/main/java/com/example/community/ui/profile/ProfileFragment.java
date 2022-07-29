package com.example.community.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.community.EditRestrictionsActivity;
import com.example.community.R;
import com.example.community.classes.DietaryRestriction;
import com.example.community.classes.GlobalUtil;
import com.example.community.classes.LoginManager;
import com.example.community.classes.Stats;
import com.example.community.classes.UserProfile;
import com.example.community.classes.DateImgUtil;
import com.example.community.databinding.FragmentProfileBinding;
import com.example.community.login.LoginActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.ArrayList;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private static final int requestCode = 15;
    private ProfileViewModel pvm;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        this.pvm = profileViewModel;

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ImageView avatar = binding.profileAvatar;
        ImageButton addRestrictionButton = binding.addRestrictionButton;
        Button signOutButton = binding.signOutButton;

        signOutButton.setOnClickListener(v -> {
            LoginManager.SignOut(this.requireContext());
            Intent loginIntent = new Intent(this.requireContext(), LoginActivity.class);
            startActivity(loginIntent);
            this.requireActivity().finish();
        });

        addRestrictionButton.setOnClickListener(v -> {
            Intent addRestrictionIntent = new Intent(requireContext(), EditRestrictionsActivity.class);
            startActivityForResult(addRestrictionIntent, requestCode);
        });
        LiveData<ArrayList<DietaryRestriction>> restrictions = profileViewModel.getRestrictions();
        LiveData<Stats> stats = profileViewModel.getStats();
        LiveData<UserProfile> profile = profileViewModel.getProfile();
        profile.observe(getViewLifecycleOwner(), (newProfile) -> {
            String profileImageURL = newProfile.profilePicture;
            DateImgUtil.setImageWhenLoaded(requireContext(), profileImageURL, avatar);
        });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ProfileFragment.requestCode) {
            this.pvm.fetchUser();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
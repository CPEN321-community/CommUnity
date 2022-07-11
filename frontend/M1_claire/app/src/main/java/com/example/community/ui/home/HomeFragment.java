package com.example.community.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.community.R;
import com.example.community.SearchActivity;
import com.example.community.classes.Global;
import com.example.community.login.LoginActivity;
import com.example.community.databinding.FragmentHomeBinding;
import com.example.community.offer_list.OfferHomeFragment;
import com.example.community.offer_list.OfferPosts;
import com.example.community.request_list.ExpandedReqPost;
import com.example.community.request_list.NewRequestForm;
import com.example.community.request_list.RequestPosts;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;

public class HomeFragment extends Fragment {

    private static final String TAG = "HOME_FRAGMENT";
    private FragmentHomeBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            String token = task.getResult();
            Log.d(TAG, "onCreateView: " + token);
        });
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ImageButton searchButton = binding.searchButton;
        searchButton.setOnClickListener(v -> {
            Intent searchIntent = new Intent(requireActivity(), SearchActivity.class);
            startActivity(searchIntent);
        });

        FloatingActionButton addNewReqButton = binding.addReqPostButton;
        addNewReqButton.setOnClickListener(view -> {
            Intent addReqIntent = new Intent(requireActivity(), NewRequestForm.class);
            startActivity(addReqIntent);
        });

        binding.viewOffersButton.setOnClickListener(view -> {
            Log.d("OfferButtonClick", "Offer button clicked");
            Intent viewOffersIntent = new Intent(requireContext(), OfferPosts.class);
            startActivity(viewOffersIntent);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
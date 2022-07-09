package com.example.community.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.community.SearchActivity;
import com.example.community.classes.Global;
import com.example.community.login.LoginActivity;
import com.example.community.databinding.FragmentHomeBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class HomeFragment extends Fragment {

    private static final String TAG = "HOME_FRAGMENT";
    private FragmentHomeBinding binding;
    private GoogleSignInClient mGoogleSignInClient;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            String token = task.getResult();
            Toast.makeText(requireContext(), token, Toast.LENGTH_LONG).show();
            Log.d(TAG, "onCreateView: " + token);
        });
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        Button searchButton = binding.searchButton;
        searchButton.setOnClickListener(v -> {
            Intent searchIntent = new Intent(requireActivity(), SearchActivity.class);
            startActivity(searchIntent);
        });
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        binding.signOutButton.setOnClickListener(v -> {
            mGoogleSignInClient.signOut().addOnCompleteListener(requireActivity(), (OnCompleteListener<Void>) task -> {
                Intent mainActivityIntent = new Intent(requireActivity(), LoginActivity.class);
                startActivity(mainActivityIntent);
                Global.setAccount(null);
                requireActivity().finish();
            });
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
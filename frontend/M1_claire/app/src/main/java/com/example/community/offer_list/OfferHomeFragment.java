package com.example.community.offer_list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.community.SearchActivity;
import com.example.community.databinding.FragmentHomeBinding;
import com.example.community.databinding.FragmentOfferHomeBinding;
import com.example.community.request_list.NewRequestForm;
import com.example.community.ui.home.HomeViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;

public class OfferHomeFragment extends Fragment {
    private static final String TAG = "OFFER_HOME_FRAGMENT";
    private FragmentOfferHomeBinding binding;
    private GoogleSignInClient mGoogleSignInClient;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            String token = task.getResult();
            Toast.makeText(requireContext(), token, Toast.LENGTH_LONG).show();
            Log.d(TAG, "onCreateView: " + token);
        });
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
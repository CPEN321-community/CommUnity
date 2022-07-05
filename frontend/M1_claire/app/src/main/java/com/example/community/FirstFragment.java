package com.example.community;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.community.databinding.FragmentFirstBinding;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class FirstFragment extends Fragment {

    private static final int RC_SIGN_IN = 1;
    private FragmentFirstBinding binding;
    private GoogleSignInClient mGoogleSignInClient;
    private LoginViewModel viewModel;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        LoginActivity parent = (LoginActivity) getActivity();

        binding.signInButton.setOnClickListener(v -> {
            parent.signIn();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
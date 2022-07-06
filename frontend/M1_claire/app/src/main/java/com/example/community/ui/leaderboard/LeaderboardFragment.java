package com.example.community.ui.leaderboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.community.databinding.FragmentLeaderboardBinding;

public class LeaderboardFragment extends Fragment {

    private FragmentLeaderboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LeaderboardViewModel leaderboardViewModel =
                new ViewModelProvider(this).get(LeaderboardViewModel.class);

        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button refreshButton = binding.buttonRefreshLeaderboard;
        refreshButton.setOnClickListener(view -> {
            leaderboardViewModel.fetchLeaderboard();
        });
        final ListView listView = binding.leaderboardList;
        leaderboardViewModel.getList().observe(getViewLifecycleOwner(), userList -> {
            UserAdapter adapter;
            adapter = new UserAdapter(requireContext(),
                    userList);
            listView.setAdapter(adapter);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


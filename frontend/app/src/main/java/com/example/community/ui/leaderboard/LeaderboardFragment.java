package com.example.community.ui.leaderboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.community.databinding.FragmentLeaderboardBinding;
import com.example.community.databinding.FragmentLeaderboardUserBinding;
import com.example.community.ui.chat.ChatActivity;

import java.util.ArrayList;

public class LeaderboardFragment extends Fragment {

    LeaderboardAdapter adapter;
    LeaderboardViewModel leaderboardViewModel;
    private FragmentLeaderboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        leaderboardViewModel =
                new ViewModelProvider(this).get(LeaderboardViewModel.class);

        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        FragmentLeaderboardUserBinding myScoreBinding = binding.myScore;
        LinearLayout myScore = myScoreBinding.leaderboardUserCard;
        View root = binding.getRoot();
        binding.toolbar.chatButton.setOnClickListener((view) -> {
            Intent chatIntent = new Intent(requireActivity(), ChatActivity.class);
            startActivity(chatIntent);
        });
        adapter = new LeaderboardAdapter(requireContext(), new ArrayList<>());
        final ListView listView = binding.leaderboardList;
        leaderboardViewModel.getList().observe(getViewLifecycleOwner(), userList -> {
            adapter.setItems(userList);
            listView.setAdapter(adapter);
        });

        SwipeRefreshLayout refresher = binding.leaderboardRefrfesher;
        refresher.setOnRefreshListener(() -> {
            refresher.setRefreshing(true);
            leaderboardViewModel.fetchLeaderboard();
            refresher.setRefreshing(false);
        });

        leaderboardViewModel.getRankData().observe(getViewLifecycleOwner(), rank -> {
            if (rank <= 10) {
                myScore.setVisibility(View.INVISIBLE);
            } else {
                myScore.setVisibility(View.VISIBLE);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public LeaderboardViewModel getViewModel() {
        return this.leaderboardViewModel;
    }
}


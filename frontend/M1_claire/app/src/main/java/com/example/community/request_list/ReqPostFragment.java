package com.example.community.request_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.community.databinding.FragmentReqPostBinding;
import com.example.community.ui.leaderboard.LeaderboardAdapter;

public class ReqPostFragment extends Fragment {

    private FragmentReqPostBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ReqPostViewModel reqPostViewModel = new ViewModelProvider(this).get(ReqPostViewModel.class);

        binding = FragmentReqPostBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button refreshButton = binding.buttonRefreshReqPosts;
        refreshButton.setOnClickListener(view -> {
            reqPostViewModel.fetchReqPosts();
        });
        final ListView listView = binding.reqPostList;
        reqPostViewModel.getList().observe(getViewLifecycleOwner(), reqList -> {
            ReqPostAdapter adapter;
            adapter = new ReqPostAdapter(requireContext(),
                    reqList);
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



package com.example.community.ui.chat.message;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.community.databinding.FragmentMessageListBinding;
import com.example.community.ui.leaderboard.LeaderboardAdapter;

public class MessageFragment extends Fragment {

    private static final String TAG = "MESSAGE_FRAGMENT";
    private FragmentMessageListBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentMessageListBinding.inflate(inflater, container, false);
        ListView messageList = binding.messageList;
        MessageActivity parent = (MessageActivity) requireActivity();
        Log.d(TAG, "onCreateView: " + parent.initialMessages);
        MessageAdapter adapter = new MessageAdapter(requireContext(),
                parent.initialMessages);
        messageList.setAdapter(adapter);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
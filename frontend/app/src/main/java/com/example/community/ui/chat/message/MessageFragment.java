package com.example.community.ui.chat.message;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.community.classes.Chat;
import com.example.community.classes.ChatMessageHandler;
import com.example.community.databinding.FragmentMessageListBinding;

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
        String postId = parent.chat.postId;
        MessageAdapter adapter = new MessageAdapter(requireContext(),
                ChatMessageHandler.getChatMap().get(postId));
        Log.d(TAG, "AddAdapter: " + adapter);
        ChatMessageHandler.AddAdapter(postId, adapter);
        messageList.setAdapter(ChatMessageHandler.GetAdapterHashMap().get(postId));
        EditText editText = binding.messageTextInput;
        binding.messageSendButton.setOnClickListener(v -> {
            String message = editText.getText().toString();
            String roomId = postId;
            Chat.sendMessage(roomId, message);
            editText.setText("");
        });

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
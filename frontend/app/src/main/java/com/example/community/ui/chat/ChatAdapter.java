package com.example.community.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.community.R;
import com.example.community.classes.ChatRoom;
import com.example.community.classes.DateImgUtil;
import com.example.community.classes.GlobalUtil;
import com.example.community.classes.Message;
import com.example.community.exceptions.NoMessagesException;
import com.example.community.ui.chat.message.MessageActivity;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<ChatRoom> chats;
    private final LayoutInflater inflater;

    public ChatAdapter(Context applicationContext, ArrayList<ChatRoom> chatList) {
        this.context = applicationContext;
        this.chats = chatList;
        inflater = (LayoutInflater.from(applicationContext));
    }

    public ChatAdapter(ChatActivity applicationContext) {
        this.context = applicationContext;
        this.chats = new ArrayList<>();
        this.inflater = LayoutInflater.from(applicationContext);
    }

    public void setItems(ArrayList<ChatRoom> chatRooms) {
        this.chats.clear();
        this.chats.addAll(chatRooms);
    }

    @Override
    public int getCount() {
        return this.chats.size();
    }

    @Override
    public Object getItem(int i) {
        return this.chats.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View newView = view;
        newView = inflater.inflate(R.layout.fragment_chat_item, null);
        ChatRoom chat = this.chats.get(i);
        TextView chatPreview = newView.findViewById(R.id.chat_message_preview);
        try {
            Message m = chat.getLastMessage();
            if (m.userId.equals(GlobalUtil.getId())) {
                chatPreview.setBackgroundResource(R.drawable.mebub);
            } else {
                chatPreview.setBackgroundResource(R.drawable.chatbub);
            }
            chatPreview.setText(m.messageText);
        } catch (NoMessagesException e) {
            chatPreview.setVisibility(View.GONE);
        }

        TextView chatName = newView.findViewById(R.id.leaderboard_name);
        ImageView avatar = newView.findViewById(R.id.chat_avatar);
        DateImgUtil.setImageWhenLoaded(context, chat.getYou().profilePicture, avatar);
        newView.setOnClickListener(v -> {
            Intent messageIntent = new Intent(context, MessageActivity.class);
            messageIntent.putExtra("roomId", chat.getRoomId());
            context.startActivity(messageIntent);
        });
        chatName.setText(chat.getYou().firstName);


        return newView;
    }
}

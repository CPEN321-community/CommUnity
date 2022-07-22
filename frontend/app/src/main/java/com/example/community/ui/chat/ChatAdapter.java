package com.example.community.ui.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.community.R;
import com.example.community.classes.Chat;
import com.example.community.classes.ChatMessageHandlerUtil;
import com.example.community.classes.GlobalUtil;
import com.example.community.classes.Message;
import com.example.community.classes.DateImgUtil;
import com.example.community.exceptions.NoMessagesException;
import com.example.community.ui.chat.message.MessageActivity;

import java.util.ArrayList;
import java.util.Objects;

public class ChatAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Chat> chats;
    private final LayoutInflater inflater;

    public ChatAdapter(Context applicationContext, ArrayList<Chat> chatList) {
        this.context = applicationContext;
        this.chats = chatList;
        inflater = (LayoutInflater.from(applicationContext));
    }

    public ChatAdapter(ChatActivity applicationContext) {
        this.context = applicationContext;
        this.chats = new ArrayList<>();
        this.inflater = LayoutInflater.from(applicationContext);
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
        Chat chat = this.chats.get(i);

        TextView chatName = (TextView) newView.findViewById(R.id.restriction_name);
        ImageView avatar = (ImageView) newView.findViewById(R.id.chat_avatar);
        TextView chatPreview = (TextView) newView.findViewById(R.id.chat_message_preview);
        LinearLayout chatBubble = (LinearLayout) newView.findViewById(R.id.chat_message_bubble);
        Message previewMessage;
        try {
            previewMessage = ChatMessageHandlerUtil.GetPreview(chat.postId);
            chatPreview.setText(previewMessage.messageText);
            if (!Objects.equals(previewMessage.userId, GlobalUtil.getAccount().getId())) {
                chatBubble.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
                chatPreview.setTextColor(ContextCompat.getColor(context, R.color.white));
            }
        } catch (NoMessagesException e) {
            chatBubble.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
            chatPreview.setText("No messages yet!");
        }
        DateImgUtil.setImageWhenLoaded(context, chat.other.profilePicture, avatar);
        newView.setOnClickListener(v -> {
            Intent messageIntent = new Intent(context, MessageActivity.class);
            messageIntent.putExtra("chat", chat);
            context.startActivity(messageIntent);
        });
        chatName.setText(chat.other.firstName);


        return newView;
    }

    public void notifySelf() {
        ((Activity) context).runOnUiThread(() -> {
            this.notifyDataSetChanged();
        });
    }

    public void setChats(ArrayList<Chat> chatsList) {
        this.chats.clear();
        this.chats.addAll(chatsList);
        this.notifySelf();
    }
}

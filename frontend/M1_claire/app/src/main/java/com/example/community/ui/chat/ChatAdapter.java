package com.example.community.ui.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.community.R;
import com.example.community.classes.Chat;
import com.example.community.classes.ChatMessageHandler;
import com.example.community.classes.Global;
import com.example.community.classes.Message;
import com.example.community.classes.Utils;
import com.example.community.ui.chat.message.MessageActivity;

import java.util.ArrayList;
import java.util.Objects;

import io.socket.client.Socket;

public class ChatAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Chat> chats;
    private LayoutInflater inflater;

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
        view = inflater.inflate(R.layout.fragment_chat_item, null);
        Chat chat = this.chats.get(i);

        TextView chatName = (TextView) view.findViewById(R.id.chat_name);
        ImageView avatar = (ImageView) view.findViewById(R.id.chat_avatar);
        TextView chatPreview = (TextView) view.findViewById(R.id.chat_message_preview);
        LinearLayout chatBubble = (LinearLayout) view.findViewById(R.id.chat_message_bubble);
        Message previewMessage = ChatMessageHandler.GetPreview(chat.postId);
        chatPreview.setText(previewMessage.messageText);
        if (!Objects.equals(previewMessage.userId, Global.getAccount().getId())) {
            chatBubble.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
            chatPreview.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
        Utils.setImageWhenLoaded(context, chat.other.profilePicture, avatar);
        view.setOnClickListener(v -> {
            Intent messageIntent = new Intent(context, MessageActivity.class);
            messageIntent.putExtra("chat", chat);
            context.startActivity(messageIntent);
        });
        chatName.setText(chat.other.firstName);


        return view;
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

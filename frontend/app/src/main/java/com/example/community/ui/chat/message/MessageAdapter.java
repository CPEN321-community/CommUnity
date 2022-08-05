package com.example.community.ui.chat.message;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.community.R;
import com.example.community.classes.GlobalUtil;
import com.example.community.classes.Message;

import java.util.ArrayList;
import java.util.Objects;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private static final String TAG = "MESSAGE_ADAPTER";
    private final Context context;
    private final ArrayList<Message> messages;

    public MessageAdapter(Context applicationContext, ArrayList<Message> messages) {
        this.context = applicationContext;
        this.messages = messages;
    }

    public void setMessages(ArrayList<Message> m) {
        this.messages.clear();
        this.messages.addAll(m);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_message_bubbles, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = this.messages.get(position);
        holder.layout.setClickable(false);
        holder.date.setVisibility(View.GONE);
        holder.myTime.setVisibility(View.GONE);
        holder.youTime.setVisibility(View.GONE);
        Log.d(TAG, "onBindViewHolder: " + message.messageText);
        if (Objects.equals(message.userId, GlobalUtil.getId())) {
            renderMyMessage(holder, message);
        } else {
            renderOtherMessage(holder, message);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return this.messages.size();
    }

    private void renderMyMessage(@NonNull ViewHolder holder, Message message) {
        holder.myText.setText(message.messageText);
        holder.myText.setVisibility(View.VISIBLE);
        holder.youText.setVisibility(View.GONE);
        holder.youTime.setVisibility(View.GONE);
    }

    private void renderOtherMessage(@NonNull ViewHolder holder, Message message) {
        holder.youText.setText(message.messageText);
        holder.youText.setVisibility(View.VISIBLE);
        holder.myText.setVisibility(View.GONE);
        holder.myTime.setVisibility(View.GONE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView youTime;
        private final TextView youText;
        private final View layout;
        private final TextView myTime;
        private final TextView myText;
        private final TextView date;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.date = view.findViewById(R.id.message_date);
            this.myTime = view.findViewById(R.id.my_message_timestamp);
            this.layout = view.findViewById(R.id.message_layout);
            this.myText = view.findViewById(R.id.my_message_text);
            this.youText = view.findViewById(R.id.other_message_text);
            this.youTime = view.findViewById(R.id.other_message_timestamp);
        }
    }
}

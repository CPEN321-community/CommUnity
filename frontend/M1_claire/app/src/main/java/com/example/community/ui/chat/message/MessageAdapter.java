package com.example.community.ui.chat.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.community.R;
import com.example.community.classes.Chat;
import com.example.community.classes.Global;
import com.example.community.classes.Message;

import java.util.ArrayList;
import java.util.Objects;

public class MessageAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Message> messages;
    private LayoutInflater inflater;

    public MessageAdapter(Context applicationContext, ArrayList<Message> messages) {
        this.context = applicationContext;
        this.messages = messages;
        inflater = LayoutInflater.from(applicationContext);
    }

    @Override
    public int getCount() {
        return this.messages.size();
    }

    @Override
    public Object getItem(int i) {
        return this.messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Message message = this.messages.get(i);
        if (Objects.equals(message.userId, Global.account.getId())) {
            return renderMyMessage(message);
        } else {
            return renderOtherMessage(message);
        }
    }

    private View renderMyMessage(Message message) {
        View view = inflater.inflate(R.layout.fragment_my_message_bubble, null);
        TextView message_text = (TextView) view.findViewById(R.id.my_message_text);
        message_text.setText(message.messageText);
        view.findViewById(R.id.my_message_date).setVisibility(View.GONE);
        view.findViewById(R.id.my_message_timestamp).setVisibility(View.GONE);
        view.findViewById(R.id.message_layout).setClickable(false);


        return view;
    }

    private View renderOtherMessage(Message message) {
        View view = inflater.inflate(R.layout.fragment_other_message_bubble, null);
        TextView message_text = (TextView) view.findViewById(R.id.other_message_text);
        TextView message_name = (TextView) view.findViewById(R.id.other_message_name);
        view.findViewById(R.id.message_layout).setClickable(false);

        view.findViewById(R.id.other_message_date).setVisibility(View.GONE);
        view.findViewById(R.id.other_message_timestamp).setVisibility(View.GONE);

        message_text.setText(message.messageText);
        Chat parent = message.parentChat;
        message_name.setText(parent.other.firstName);

        return view;
    }
}

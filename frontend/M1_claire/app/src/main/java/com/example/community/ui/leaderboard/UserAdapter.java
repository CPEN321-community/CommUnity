package com.example.community.ui.leaderboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.community.R;
import com.example.community.classes.UserWithScore;

import java.util.ArrayList;

public class UserAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<UserWithScore> users;
    private LayoutInflater inflater;

    public UserAdapter(Context applicationContext, ArrayList<UserWithScore> userList) {
        this.context = applicationContext;
        this.users = userList;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return this.users.size();
    }

    @Override
    public Object getItem(int i) {
        return this.users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.fragment_leaderboard_user, null);
        TextView number = (TextView) view.findViewById(R.id.item_number);
        TextView name = (TextView) view.findViewById(R.id.chat_name);
        TextView score = (TextView) view.findViewById(R.id.leaderboard_score);
        UserWithScore user = this.users.get(i);

        number.setText(String.valueOf(i));
        name.setText(user.id);
        score.setText(String.valueOf(user.score));

        ImageView avatar = (ImageView) view.findViewById(R.id.leaderboard_image);
        return view;
    }
}

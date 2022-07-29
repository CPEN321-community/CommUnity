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
import com.example.community.classes.DateImgUtil;

import java.util.ArrayList;
import java.util.Objects;

public class LeaderboardAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<UserWithScore> users;
    private final LayoutInflater inflater;

    public LeaderboardAdapter(Context applicationContext, ArrayList<UserWithScore> userList) {
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
        View newView = view;
        newView = inflater.inflate(R.layout.fragment_leaderboard_user, null);
        TextView number = (TextView) newView.findViewById(R.id.item_number);
        TextView name = (TextView) newView.findViewById(R.id.leaderboard_name);
        TextView score = (TextView) newView.findViewById(R.id.leaderboard_score);
        UserWithScore user = this.users.get(i);

        number.setText(String.valueOf(i + 1));
        String nameAndInitial = user.firstName + " " + user.lastName.substring(0, 1).toUpperCase() + ".";
        name.setText(nameAndInitial);
        score.setText(String.valueOf(user.score));

        ImageView avatar = (ImageView) newView.findViewById(R.id.chat_avatar);
        if (!Objects.equals(user.profilePicture, "")) {
            DateImgUtil.setImageWhenLoaded(context, user.profilePicture, avatar);
        } else {
            avatar.setImageDrawable(DateImgUtil.GetDefaultAvatar(context));
        }
        return newView;
    }


}

package com.example.community.ui.leaderboard;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.graphics.drawable.VectorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.community.R;
import com.example.community.classes.UserWithScore;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class LeaderboardAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<UserWithScore> users;
    private LayoutInflater inflater;
    private final String TAG = "LEADERBOARD_ADAPTER";

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
        view = inflater.inflate(R.layout.fragment_leaderboard_user, null);
        TextView number = (TextView) view.findViewById(R.id.item_number);
        TextView name = (TextView) view.findViewById(R.id.chat_name);
        TextView score = (TextView) view.findViewById(R.id.leaderboard_score);
        UserWithScore user = this.users.get(i);

        number.setText(String.valueOf(i + 1));
        String nameAndInitial = user.firstName + " " + user.lastName.substring(0, 1).toUpperCase() + ".";
        name.setText(nameAndInitial);
        score.setText(String.valueOf(user.score));

        ImageView avatar = (ImageView) view.findViewById(R.id.leaderboard_image);
        if (!Objects.equals(user.profilePicture, "")) {
            setImageWhenLoaded(user, avatar);
        } else {
            avatar.setImageDrawable(GetDefaultAvatar());
        }
        return view;
    }

    public void setImageWhenLoaded(UserWithScore user, ImageView imageView) {
        Thread thread = new Thread(() -> {
            try {
                ((Activity) context).runOnUiThread( () ->{
                    imageView.setImageDrawable(GetDefaultAvatar());
                });
                Drawable d = LoadImageFromWeb(user.profilePicture);
                ((Activity) context).runOnUiThread( () ->{
                    imageView.setImageDrawable(d);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }

    public Drawable GetDefaultAvatar() {
        Resources res = context.getResources();
        VectorDrawable d =
                (VectorDrawable) ResourcesCompat.getDrawable(res, R.drawable.ic_default_avatar, null);
        return d;
    }

    public Drawable LoadImageFromWeb(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            Log.e(TAG, "LoadImageFromWeb: " + e);
            return GetDefaultAvatar();
        }
    }
}

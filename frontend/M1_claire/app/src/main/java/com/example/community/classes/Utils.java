package com.example.community.classes;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.util.Log;
import android.widget.ImageView;

import androidx.core.content.res.ResourcesCompat;

import com.example.community.R;

import java.io.InputStream;
import java.net.URL;

public class Utils {

    private static final String TAG = "UTILS";

    public static void setImageWhenLoaded(Context context, String url, ImageView imageView) {
        Thread thread = new Thread(() -> {
            try {
                ((Activity) context).runOnUiThread( () ->{
                    imageView.setImageDrawable(GetDefaultAvatar(context));
                });
                Drawable d = LoadImageFromWeb(context, url);
                ((Activity) context).runOnUiThread( () ->{
                    imageView.setImageDrawable(d);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }

    public static Drawable GetDefaultAvatar(Context context) {
        Resources res = context.getResources();
        VectorDrawable d =
                (VectorDrawable) ResourcesCompat.getDrawable(res, R.drawable.ic_default_avatar, null);
        return d;
    }

    public static Drawable LoadImageFromWeb(Context context, String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            Log.e(TAG, "LoadImageFromWeb: " + e);
            return GetDefaultAvatar(context);
        }
    }
}

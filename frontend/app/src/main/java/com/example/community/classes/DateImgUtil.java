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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateImgUtil {

    public static final String dateFormatString = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    private static final String TAG = "DateImgUtil";

    public static void setImageWhenLoaded(Context context, String url, ImageView imageView) {
        Thread thread = new Thread(() -> {
            try {
                ((Activity) context).runOnUiThread(() -> {
                    imageView.setImageDrawable(GetDefaultAvatar(context));
                });
                Drawable d = LoadImageFromWeb(context, url);
                ((Activity) context).runOnUiThread(() -> {
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

    public static Date StringToDate(String dateString) throws ParseException {
        Date sdf = new SimpleDateFormat(dateFormatString).parse(dateString);
        return sdf;
    }

    public static String DateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormatString);
        String dateString = sdf.format(date);
        return dateString;
    }
}

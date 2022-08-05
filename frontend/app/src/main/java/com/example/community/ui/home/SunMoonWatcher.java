package com.example.community.ui.home;

import android.os.CountDownTimer;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageView;

import com.example.community.R;
import com.example.community.databinding.TimeSunMoonElementBinding;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class SunMoonWatcher {

    private static final String TAG = "SUN_MOON_WATCHER";
    private final static long twelvePM = LocalTime.of(12, 0).toNanoOfDay();
    private final static long twelveAM = LocalTime.of(0, 0).toNanoOfDay();
    private final static long sixAM = LocalTime.of(6, 0).toNanoOfDay();
    private final static long sixPM = LocalTime.of(18, 0).toNanoOfDay();

    private final TimeSunMoonElementBinding binding;
    private final CountDownTimer timer;
    private final ImageView imageView;
    private final int screenWidth;
    private final int effectiveScreenHeight;
    private final int rW;
    private final int rH;


    public SunMoonWatcher(TimeSunMoonElementBinding binding, int screenWidth, int screenHeight) {
        this.binding = binding;
        this.screenWidth = screenWidth;
        this.effectiveScreenHeight = screenHeight / 3;
        this.imageView = binding.sunOrMoon;
        this.rW = screenWidth / 2;
        this.rH = screenHeight / 3;
        this.timer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long l) {

                Tick();

            }

            @Override
            public void onFinish() {
            }
        };
        timer.start();
    }

    private void Tick() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        boolean isDay = hour >= 6 && hour <= 18;
        Log.d(TAG, "Tick: " + hour);
        if (isDay) {
            if (hour < 12) {
                binding.greeting.setText("Good Morning!");
            } else {
                binding.greeting.setText("Good Afternoon!");
            }
            imageView.setImageResource(R.mipmap.ic_sun_img);
        } else {
            if (hour > 6 && hour < 20) {
                binding.greeting.setText("Good Evening!");
            } else {
                binding.greeting.setText("Good Night!");
            }
            imageView.setImageResource(R.mipmap.ic_moon_img);
            Pair<Float, Float> coords = getCoordinates(isDay);
            imageView.setX(coords.first);
            imageView.setY(coords.second);
        }
    }

    private Pair<Float, Float> getCoordinates(boolean isDay) {
        long now = LocalTime.now().toNanoOfDay();
        long midYTime;
        long leftXTime;
        long rightXTime;
        if (isDay) {
            midYTime = twelvePM;
            leftXTime = sixAM;
            rightXTime = sixPM;
        } else {
            midYTime = twelveAM;
            rightXTime = sixAM;
            leftXTime = sixPM;
        }
        Float absY = (float) Math.abs(now - midYTime);
        Float absX = (float) (leftXTime + now - midYTime);
        return new Pair<Float, Float>(0.0f, absY);
    }
}

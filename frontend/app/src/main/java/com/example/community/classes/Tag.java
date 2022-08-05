package com.example.community.classes;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

public class Tag {
    private static final String TAG = "TAG_CLASS";
    public String name;
    public final MutableLiveData<Boolean> clicked = new MutableLiveData<>(false);

    public Tag(String name) {
        this.name = name;
    }

    public void click() {
        Log.d(TAG, "click: Clicked!");
        this.clicked.postValue(!clicked.getValue());
    }

    public MutableLiveData<Boolean> getClickData(){
        return this.clicked;
    }

}

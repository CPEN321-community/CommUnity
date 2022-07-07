package com.example.community.ui.home;

import android.widget.Button;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.community.classes.Global;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Hello, " + Global.account.getGivenName());
    }

    public LiveData<String> getText() {
        return mText;
    }
}
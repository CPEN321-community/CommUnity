package com.example.community.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.community.classes.GlobalUtility;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Hello, " + GlobalUtility.getAccount().getGivenName());
    }

    public LiveData<String> getText() {
        return mText;
    }
}
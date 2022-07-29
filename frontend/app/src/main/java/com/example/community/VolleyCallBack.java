package com.example.community;

import com.android.volley.VolleyError;

public interface VolleyCallBack {
    void onError(VolleyError error);

    void onSuccess(boolean b);

    void onSuccess();

    void onError();
}


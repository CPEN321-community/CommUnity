package com.example.community.classes;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import io.socket.client.Socket;

public class Global {
    private static GoogleSignInAccount account;
    private static Socket socket;
    private static Context appContext;

    public static final String CHAT_URL = "http://10.0.2.2:3000";
    public static final String USER_URL = "http://10.0.2.2:8080";
    public static final String POST_URL = "http://10.0.2.2:8081";

    public static GoogleSignInAccount getAccount() {
        return account;
    }

    public static void setAccount(GoogleSignInAccount account) {
        Global.account = account;
    }

    public static Socket getSocket() {
        return socket;
    }

    public static void setSocket(Socket socket) {
        Global.socket = socket;
    }


    public static Context getAppContext() {
        return appContext;
    }

    public static void setAppContext(Context appContext) {
        Global.appContext = appContext;
    }
}

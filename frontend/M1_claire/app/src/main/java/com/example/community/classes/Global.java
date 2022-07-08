package com.example.community.classes;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import io.socket.client.Socket;

public class Global {
    private static GoogleSignInAccount account;
    private static Socket socket;

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
}

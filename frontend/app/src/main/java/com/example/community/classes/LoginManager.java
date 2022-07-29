package com.example.community.classes;

import android.content.Context;

import com.example.community.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class LoginManager {
    public static GoogleSignInOptions GetGoogleSignInOptions(Context context) {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
    }

    public static GoogleSignInClient GetGoogleClient(Context context) {
        GoogleSignInOptions gso = GetGoogleSignInOptions(context);
        return GoogleSignIn.getClient(context, gso);
    }

    public static GoogleSignInAccount GetLastSignedInAccount(Context context) {
        return GoogleSignIn.getLastSignedInAccount(context);
    }
    public static void SignOut(Context context) {
        GoogleSignInClient client = GetGoogleClient(context);
        client.signOut();
    }
}

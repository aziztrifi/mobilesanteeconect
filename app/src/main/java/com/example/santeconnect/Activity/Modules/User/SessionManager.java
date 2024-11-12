package com.example.santeconnect.Activity.Modules.User;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class SessionManager {

    private final String PREF_FILE_NAME = "user";
    private final int PRIVATE_MODE = 0;
    private final String KEY_IF_LOGGED_IN = "key_if_logged_in";
    private final String KEY_NAME = "key_session_name";
    private final String KEY_EMAIL = "key_session_email";
    private final String KEY_ROLE = "key_session_role";

    Context context;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        this.context = context;
        sp = context.getSharedPreferences(PREF_FILE_NAME, PRIVATE_MODE);
        editor = sp.edit();
    }

    public boolean checkSession() {
        return sp.getBoolean(KEY_IF_LOGGED_IN, false); // Changed to Boolean check
    }

    public void createSession(String name, String email, String role) {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ROLE, role);
        //editor.putString(KEY_PROFILE_IMAGE, profileImageUrl);
        editor.putBoolean(KEY_IF_LOGGED_IN, true);
        editor.commit();

    }



    public String getSessionDetails(String key) {
        return sp.getString(key, null);
    }

    public void logoutSession() {
        editor.clear();
        editor.commit();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
        context.startActivity(intent);
    }


}

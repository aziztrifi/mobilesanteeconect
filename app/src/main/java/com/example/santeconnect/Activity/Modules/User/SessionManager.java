package com.example.santeconnect.Activity.Modules.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SessionManager {

    private final String PREF_FILE_NAME = "user";
    private final int PRIVATE_MODE = 0;
    private final String KEY_IF_LOGGED_IN = "key_if_logged_in";
    private final String KEY_NAME = "key_session_name";
    private final String KEY_EMAIL = "key_session_email";
    private final String KEY_ROLE = "key_session_role";
    private final String KEY_USER_ID = "key_session_user_id"; // New key for storing user ID

    Context context;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        this.context = context;
        sp = context.getSharedPreferences(PREF_FILE_NAME, PRIVATE_MODE);
        editor = sp.edit();
    }

    public boolean checkSession() {
        return sp.getBoolean(KEY_IF_LOGGED_IN, false); // Check if logged in
    }

    // Modified to also save user ID
    public void createSession(int userId, String name, String email, String role) {
        editor.putInt(KEY_USER_ID, userId); // Save user ID
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ROLE, role);
        editor.putBoolean(KEY_IF_LOGGED_IN, true);
        editor.commit();
    }

    public String getSessionDetails(String key) {
        return sp.getString(key, null);
    }

    // New method to get user ID from session
    public int getUserId() {
        return sp.getInt(KEY_USER_ID, -1); // Return -1 if no user ID is found
    }

    public void logoutSession() {
        editor.clear();
        editor.commit();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
        context.startActivity(intent);
    }

    public void saveUserEmail(String email) {
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public String getUserEmail() {
        return sp.getString(KEY_EMAIL, null);
    }
}

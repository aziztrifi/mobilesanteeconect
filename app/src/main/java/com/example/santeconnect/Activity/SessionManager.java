package com.example.santeconnect.Activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SessionManager {

    private final String PREF_FILE_NAME = "user";
    private final int PRIVATE_MODE = 0;
    private final String KEY_IF_LOGGED_IN = "false";
    private final String KEY_NAME = "key_session_name";
    private final String KEY_EMAIL = "key_session_email";
    private final String KEY_PHONE = "key_session_phone";
    Context context;
    SharedPreferences sp;
    SharedPreferences.Editor editor;


    public SessionManager(Context context) {
        this.context = context;
        sp = context.getSharedPreferences(PREF_FILE_NAME, PRIVATE_MODE);
        editor = sp.edit();

    }

    public boolean checkSession() {
        if(sp.contains(KEY_IF_LOGGED_IN)) {
            return true;
        }
        else {
            return false;
        }
    }

    public void createSession(String name, String email) {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        //editor.putString(KEY_PHONE, phone);
        editor.putBoolean(KEY_IF_LOGGED_IN, true);
        editor.commit();

    }

    public String getSessionDetails(String key){
        return sp.getString(key,null);
    }

    public void logoutSession(){
        editor.clear();
        editor.commit();

        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }
}

package com.example.santeconnect.Activity.Modules.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.santeconnect.R;

public class Profil extends AppCompatActivity {

    private SessionManager sessionManager;
    private TextView tvUserName, tvUserEmail;

    private Button btnLogout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        sessionManager = new SessionManager(getApplicationContext());

        // Initialize views
        btnLogout = findViewById(R.id.btnLogout);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
       // imgProfile = findViewById(R.id.imgProfile); // Initialize the profile image view

        // Retrieve session details
        String name = sessionManager.getSessionDetails("key_session_name");
        String email = sessionManager.getSessionDetails("key_session_email");
        String imageUri = sessionManager.getSessionDetails("key_session_profile_image");

        // Set user information
        tvUserName.setText(name != null ? name : "Unknown User");
        tvUserEmail.setText(email != null ? email : "No Email");



        // Set logout button listener
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    // Logout method
    public void logout() {
        sessionManager.logoutSession(); // Clear session
        Intent intent = new Intent(this, LoginActivity.class); // Redirect to login
        startActivity(intent);
        finish();
    }
}

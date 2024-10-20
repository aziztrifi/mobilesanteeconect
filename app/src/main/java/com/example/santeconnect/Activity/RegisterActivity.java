package com.example.santeconnect.Activity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.santeconnect.R;


public class RegisterActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private DatabaseHelper dbHelper;

    private EditText editTextName, editTextEmail, editTextPassword, editTextConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(getApplicationContext());

        // Initialize EditText fields using the class-level variables
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
    }

    public void onLoginClick(View view) {
        // Start LoginActivity
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Optionally finish the current activity to prevent going back to it
    }

    public void onRegisterClick(View view) {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        // Validate input fields
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            if (name.isEmpty()) editTextName.setError("Name is required");
            if (email.isEmpty()) editTextEmail.setError("Email is required");
            if (password.isEmpty()) editTextPassword.setError("Password is required");
            if (confirmPassword.isEmpty()) editTextConfirmPassword.setError("Please confirm your password");
            return;
        }

        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Passwords do not match");
            return;
        }

        if (dbHelper.checkUserExists(email)) {
            editTextEmail.setError("Email already registered");
            return;
        }

        // Insert data into the database
        boolean isInserted = dbHelper.insertUser(name, email, password);
        if (isInserted) {
            sessionManager.createSession(name, email);
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }


}
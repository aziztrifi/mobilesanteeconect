package com.example.santeconnect.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.santeconnect.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private RelativeLayout relative_layout;
    private TextInputLayout textInputLayoutEmail, textInputLayoutPassword;
    private TextInputEditText textInputEditTextEmail, textInputEditTextPassword;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Assuming the XML is named activity_login.xml

        // Initialize Views
        initViews();

        // Initialize Database Helper
        dbHelper = new DatabaseHelper(this);

        // Set up Login Button Click Listener
        Button loginButton = findViewById(R.id.LoginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginClick();
            }
        });
    }

    private void initViews() {
        relative_layout = findViewById(R.id.relative_layout);
        textInputLayoutEmail = findViewById(R.id.textInputEmail);
        textInputLayoutPassword = findViewById(R.id.textInputPassword);
        textInputEditTextEmail = findViewById(R.id.editTextEmail);
        textInputEditTextPassword = findViewById(R.id.editTextPassword);
    }

    private void onLoginClick() {
        String email = textInputEditTextEmail.getText().toString().trim();
        String password = textInputEditTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            textInputEditTextEmail.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            textInputEditTextPassword.setError("Password is required");
            return;
        }

        if (dbHelper.checkUser(email, password)) {
            Intent intent = new Intent(this, Profil.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }

    public void onRegisterClick(View view) {
        // Redirect to RegisterActivity
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

}

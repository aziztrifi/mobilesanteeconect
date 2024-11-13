package com.example.santeconnect.Activity.Modules.User;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.santeconnect.Activity.DAO.UserDAO;
import com.example.santeconnect.Activity.Entities.User;
import com.example.santeconnect.Activity.Modules.Blog.BlogActivity;
import com.example.santeconnect.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private RelativeLayout relative_layout;
    private TextInputLayout textInputLayoutEmail, textInputLayoutPassword;
    private TextInputEditText textInputEditTextEmail, textInputEditTextPassword;

    private UserDAO userDAO;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(getApplicationContext());
        initViews();

        TextView forgotPasswordTextView = findViewById(R.id.forgetPassword);
        forgotPasswordTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        userDAO = new UserDAO(this);
        Button loginButton = findViewById(R.id.LoginButton);
        loginButton.setOnClickListener(v -> onLoginClick());
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

        // Verify user
        User user = userDAO.getUserByEmailAndPassword(email, password);
        if (user != null) {
            // Create session with user's details, including the profile image URI
            sessionManager.createSession(user.getName(), user.getEmail(), user.getRole());

            Intent intent = new Intent(this, BlogActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }

    public void onRegisterClick(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

}

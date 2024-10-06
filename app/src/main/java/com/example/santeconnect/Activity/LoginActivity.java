package com.example.santeconnect.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.santeconnect.MainActivity;
import com.example.santeconnect.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private RelativeLayout relative_layout ;
    private TextInputLayout textInputLayoutEmail , textInputLayoutPassword;
    private TextInputEditText textInputEditTextEmail,textInputEditTextPassword;

    private final AppCompatActivity activity = LoginActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Assuming the XML is named activity_login.xml
        initViews();
        // initListeners();
        initObjects();
        // Button for login
        Button loginButton = findViewById(R.id.LoginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginClick(v);
            }
        });
    }
    private void initViews() {

        relative_layout = (RelativeLayout) findViewById(R.id.relative_layout);

        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputPassword);

        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.editTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.editTextPassword);





    }



    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {


    }

    public void onLoginClick(View view) {
        // Implement the login logic here
        // Example: Intent to the next screen after login
        Intent intent = new Intent(LoginActivity.this, MainActivity.class); // Assuming HomeActivity exists
        startActivity(intent);
    }



}

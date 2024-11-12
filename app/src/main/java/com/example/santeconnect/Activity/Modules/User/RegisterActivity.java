package com.example.santeconnect.Activity.Modules.User;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.santeconnect.Activity.DAO.UserDAO;
import com.example.santeconnect.Activity.Entities.User;
import com.example.santeconnect.R;

public class RegisterActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private UserDAO userDAO;
    private EditText editTextName, editTextEmail, editTextPassword, editTextConfirmPassword;
   // private ImageView imageViewProfilePreview;
   // private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        userDAO = new UserDAO(this);

        // Initialize EditText fields
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);

        // Initialize ImageView for profile preview and button for selecting image
        //imageViewProfilePreview = findViewById(R.id.imageViewProfilePreview);
       // Button buttonSelectImage = findViewById(R.id.buttonSelectImage);

        // Set click listener for image selection button
       // buttonSelectImage.setOnClickListener(view -> openImageSelector());
    }

    private void openImageSelector() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void onLoginClick(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void onRegisterClick(View view) {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
        String role = "user";

        // Validate input fields
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            if (name.isEmpty()) editTextName.setError("Name is required");
            if (email.isEmpty()) editTextEmail.setError("Email is required");
            if (password.isEmpty()) editTextPassword.setError("Password is required");
            if (confirmPassword.isEmpty())
                editTextConfirmPassword.setError("Please confirm your password");
            return;
        }

        /*if (selectedImageUri == null) {
            Toast.makeText(this, "Profile image is required", Toast.LENGTH_SHORT).show();
            return;
        }*/

        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Passwords do not match");
            return;
        }

        if (userDAO.checkUserExists(email)) {
            editTextEmail.setError("Email already registered");
            return;
        }

        // Create a new user with all required inputs
        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setRole(role);
       // newUser.setProfileImageUri(selectedImageUri.toString());

        // Insert data into the database
        boolean isInserted = userDAO.insertUser(newUser);
        if (isInserted) {
            Toast.makeText(this, "Registration successful. Please log in.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}

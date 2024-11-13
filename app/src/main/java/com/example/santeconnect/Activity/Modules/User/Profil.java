package com.example.santeconnect.Activity.Modules.User;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.santeconnect.Activity.DAO.UserDAO;
import com.example.santeconnect.Activity.Entities.User;
import com.example.santeconnect.R;

import java.io.IOException;

public class Profil extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 100;

    private SessionManager sessionManager;
    private TextView tvUserName, tvUserEmail;
    private Button btnLogout, btnUpdateProfile;
    private ImageView imgProfile;

    private UserDAO userDAO;
    private Uri imageUri;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        sessionManager = new SessionManager(getApplicationContext());
        userDAO = new UserDAO(getApplicationContext());

        // Initialize views
        btnLogout = findViewById(R.id.btnLogout);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);  // Update profile button
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        imgProfile = findViewById(R.id.imgProfile); // Initialize the profile image view

        // Retrieve session details
        int userId = sessionManager.getUserId();
        loadUserDetails(userId);

        // Set logout button listener
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        // Set update profile button listener
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateProfileDialog(userId);
            }
        });

        // Set profile image click listener to choose image
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Profil.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Profil.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
                } else {
                    openGallery();
                }
            }
        });
    }

    // Method to load user details from the database
    private void loadUserDetails(int userId) {
        User user = userDAO.getUserById(userId);
        if (user != null) {
            tvUserName.setText(user.getName() != null ? user.getName() : "Unknown User");
            tvUserEmail.setText(user.getEmail() != null ? user.getEmail() : "No Email");

            if (user.getProfileImageUri() != null) {
                imgProfile.setImageURI(Uri.parse(user.getProfileImageUri()));
            }
        } else {
            Toast.makeText(this, "Unable to load user details", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to show update profile dialog
    private void showUpdateProfileDialog(int userId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_update_profile, null);

        EditText etUpdateName = dialogView.findViewById(R.id.etUpdateName);
        EditText etUpdateEmail = dialogView.findViewById(R.id.etUpdateEmail);

        User user = userDAO.getUserById(userId);
        if (user != null) {
            etUpdateName.setText(user.getName());
            etUpdateEmail.setText(user.getEmail());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle("Update Profile")
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String updatedName = etUpdateName.getText().toString();
                        String updatedEmail = etUpdateEmail.getText().toString();

                        user.setName(updatedName);
                        user.setEmail(updatedEmail);

                        if (imageUri != null) {
                            user.setProfileImageUri(imageUri.toString());
                        }

                        boolean isUpdated = userDAO.updateUser(user);
                        if (isUpdated) {
                            Toast.makeText(Profil.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                            loadUserDetails(userId);
                        } else {
                            Toast.makeText(Profil.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    // Open the gallery to pick an image
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    // Handle activity result for image selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imgProfile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Handle permissions result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Storage permission is required to select a profile image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Logout method
    public void logout() {
        sessionManager.logoutSession();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

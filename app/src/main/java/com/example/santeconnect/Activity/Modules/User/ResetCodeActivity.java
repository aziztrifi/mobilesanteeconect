package com.example.santeconnect.Activity.Modules.User;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.santeconnect.Activity.DAO.UserDAO;
import com.example.santeconnect.Activity.Database.DatabaseHelper;
import com.example.santeconnect.Activity.Entities.EmailSender;
import com.example.santeconnect.R;
import com.google.android.material.textfield.TextInputEditText;

public class ResetCodeActivity extends AppCompatActivity {
    private TextInputEditText editTextResetCode;
    private TextInputEditText editTextNewPassword;
    private TextInputEditText editTextConfirmPassword;
    private Button buttonSubmit;
    private TextView resendCodeText;
    private DatabaseHelper databaseHelper;
    private String email;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_code);
        userDAO = new UserDAO(this);
        // Initialize views
        editTextResetCode = findViewById(R.id.editTextResetCode);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        resendCodeText = findViewById(R.id.resendCodeText);
        databaseHelper = new DatabaseHelper(this);

        // Get email from intent
        email = getIntent().getStringExtra("email");
        if (email == null) {
            Toast.makeText(this, "Error: Email not provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Submit button click listener
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        // Resend code click listener
        resendCodeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendCode();
            }
        });
    }

    private void resetPassword() {
        String resetCode = editTextResetCode.getText().toString().trim();
        String newPassword = editTextNewPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(resetCode)) {
            editTextResetCode.setError("Please enter reset code");
            return;
        }

        if (TextUtils.isEmpty(newPassword)) {
            editTextNewPassword.setError("Please enter new password");
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            editTextConfirmPassword.setError("Please confirm new password");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Passwords do not match");
            return;
        }

        // Password strength validation
        if (newPassword.length() < 6) {
            editTextNewPassword.setError("Password must be at least 6 characters");
            return;
        }

        // Verify reset code
        if (userDAO.verifyResetCode(email, resetCode)) {
            // Update password
            if (userDAO.updatePassword(email, newPassword)) {
                Toast.makeText(this, "Password reset successful", Toast.LENGTH_SHORT).show();
                // Navigate to login activity
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Invalid or expired reset code", Toast.LENGTH_SHORT).show();
        }
    }

    private void resendCode() {
        // Generate new reset code
        String newResetCode = generateResetCode();
        long expiryTime = System.currentTimeMillis() + (30 * 60 * 1000); // 30 minutes from now

        if (userDAO.updateResetCode(email, newResetCode, expiryTime)) {
            // Send email with new reset code
            sendResetEmail(email, newResetCode);
            Toast.makeText(this, "New reset code sent to your email", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to generate new reset code", Toast.LENGTH_SHORT).show();
        }
    }

    private String generateResetCode() {
        // Generate a 6-digit random code
        return String.format("%06d", (int) (Math.random() * 1000000));
    }

    private void sendResetEmail(String email, String resetCode) {
        // Create an AsyncTask to send email
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EmailSender.sendResetEmail(email, resetCode);
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ResetCodeActivity.this,
                                    "Failed to send email", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Clear reset code and expiry time when user navigates back
        userDAO.updateResetCode(email, null, 0);
    }
}
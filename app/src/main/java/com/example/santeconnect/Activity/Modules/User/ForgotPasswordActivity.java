package com.example.santeconnect.Activity.Modules.User;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.santeconnect.Activity.DAO.UserDAO;
import com.example.santeconnect.Activity.Database.DatabaseHelper;
import com.example.santeconnect.Activity.Entities.EmailSender;
import com.example.santeconnect.R;

import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailInput;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        userDAO = new UserDAO(this);
        emailInput = findViewById(R.id.editTextEmail);

        Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(v -> initiatePasswordReset());
    }

    private void initiatePasswordReset() {
        String email = emailInput.getText().toString().trim();

        if (!isValidEmail(email)) {
            emailInput.setError("Enter a valid email address");
            return;
        }

        String resetCode = generateResetCode();
        long expiryTime = System.currentTimeMillis() + (30 * 60 * 1000); // 30 minutes

        if (userDAO.updateResetCode(email, resetCode, expiryTime)) {
            EmailSender.sendResetEmail(email, resetCode);

            // Navigate to reset code verification activity
            Intent intent = new Intent(this, ResetCodeActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Email not found in our records",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private String generateResetCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6-digit code
        return String.valueOf(code);
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
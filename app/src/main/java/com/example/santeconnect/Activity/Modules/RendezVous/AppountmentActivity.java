package com.example.santeconnect.Activity.Modules.RendezVous;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.santeconnect.Activity.DAO.RendezVousDAO;
import com.example.santeconnect.Activity.DAO.UserDAO;
import com.example.santeconnect.Activity.Entities.RendezVou;
import com.example.santeconnect.Activity.Modules.User.SessionManager;
import com.example.santeconnect.R;

import java.util.Calendar;

public class AppountmentActivity extends AppCompatActivity {

    private EditText descriptionEditText, dateEditText, timeEditText;
    private Switch urgentSwitch;
    private Button submitButton;
    private RendezVousDAO rendezVousDAO;
    private SessionManager sessionManager;
    private UserDAO userDAO;
    private int doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appountment);

        // Initialize views
        descriptionEditText = findViewById(R.id.descriptionEditText);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        urgentSwitch = findViewById(R.id.dangerSwitch);
        submitButton = findViewById(R.id.submitButton);

        // Initialize DAOs and SessionManager
        rendezVousDAO = new RendezVousDAO(this);
        sessionManager = new SessionManager(this);
        userDAO = new UserDAO(this);

        // Get doctorId from intent
        Intent intent = getIntent();
        doctorId = intent.getIntExtra("doctorId", -1);

        if (doctorId == -1) {
            Toast.makeText(this, "Invalid doctor. Please try again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Date picker for selecting the date
        dateEditText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
                dateEditText.setText(year1 + "-" + (month1 + 1) + "-" + dayOfMonth);
            }, year, month, day).show();
        });

        // Time picker for selecting the time
        timeEditText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
                timeEditText.setText(String.format("%02d:%02d", hourOfDay, minute1));
            }, hour, minute, true).show();
        });

        // Save appointment when submit button is clicked
        submitButton.setOnClickListener(v -> saveAppointment());
    }

    private void saveAppointment() {
        String description = descriptionEditText.getText().toString();
        String date = dateEditText.getText().toString();
        String time = timeEditText.getText().toString();
        boolean isUrgent = urgentSwitch.isChecked();

        if (description.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the email of the connected user from SessionManager
        String userEmail = sessionManager.getSessionDetails("key_session_email");

        if (userEmail == null) {
            Toast.makeText(this, "Unable to identify the logged-in user. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch userId from UserDAO using the email
        int userId = userDAO.getUserIdByEmail(userEmail);

        if (userId == -1) {
            Toast.makeText(this, "User not found in the database. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new RendezVou object and add the userId (patient ID) and doctorId
        RendezVou rendezVou = new RendezVou(date, time, description, "Pending", isUrgent, userId, doctorId);

        // Insert the rendezvous using DAO
        rendezVousDAO.insertRendezVou(rendezVou);

        Toast.makeText(this, "Appointment saved!", Toast.LENGTH_SHORT).show();
        finish(); // Go back to the previous activity
    }
}

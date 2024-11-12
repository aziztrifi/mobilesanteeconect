package com.example.santeconnect.Activity.Modules.RendezVous;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.santeconnect.Activity.DAO.RendezVousDAO;
import com.example.santeconnect.Activity.Entities.RendezVou;
import com.example.santeconnect.R;

import java.util.Calendar;

public class AppountmentActivity extends AppCompatActivity {

    private EditText descriptionEditText, dateEditText, timeEditText;
    private Switch urgentSwitch;
    private Button submitButton;
    private RendezVousDAO rendezVousDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appountment);

        // Initialize views
        descriptionEditText = findViewById(com.example.santeconnect.R.id.descriptionEditText);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        urgentSwitch = findViewById(R.id.dangerSwitch);
        submitButton = findViewById(R.id.submitButton);

        // Initialize DAO
        rendezVousDAO = new RendezVousDAO(this);

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

        // Create a new RendezVou object
        RendezVou rendezVou = new RendezVou(0, description, date, time, "Pending", isUrgent);

        // Insert the rendezvous using DAO
        rendezVousDAO.insertRendezVou(rendezVou);

        Toast.makeText(this, "Appointment saved!", Toast.LENGTH_SHORT).show();
        finish(); // Go back to the previous activity
    }
}
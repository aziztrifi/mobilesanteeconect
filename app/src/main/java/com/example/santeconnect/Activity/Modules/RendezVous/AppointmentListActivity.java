package com.example.santeconnect.Activity.Modules.RendezVous;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.santeconnect.Activity.DAO.RendezVousDAO;
import com.example.santeconnect.Activity.Entities.RendezVou;
import com.example.santeconnect.R;


import java.util.ArrayList;
import java.util.List;

public class AppointmentListActivity extends AppCompatActivity {
    private ListView appointmentListView;
    private AppointmentAdapter appointmentAdapter;
    private ArrayList<RendezVou> appointments; // Changed to ArrayList for easier manipulation
    private RendezVousDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_list);

        // Initialize DAO
        dao = new RendezVousDAO(this); // Adjust constructor as necessary for your DAO

        // Initialize appointment list
        appointments = new ArrayList<>();

        // Set up the adapter before loading appointments
        appointmentAdapter = new AppointmentAdapter(this, appointments);
        appointmentListView = findViewById(R.id.appointment_list);
        appointmentListView.setAdapter(appointmentAdapter);

        // Load appointments from the database
        loadAppointmentsFromDatabase();

        // Set an item click listener for the ListView
        appointmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RendezVou selectedAppointment = appointments.get(position);
                Toast.makeText(AppointmentListActivity.this, "Selected: " + selectedAppointment.getDescription(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to load appointments from the database
    public void loadAppointmentsFromDatabase() {
        List<RendezVou> dbAppointments = dao.getAllRendezVous();
        for (RendezVou rdv: dbAppointments
             ) {
            System.out.println(rdv);
        }// Fetch from the DAO
        appointments.clear();  // Clear the list
        appointments.addAll(dbAppointments);  // Add all appointments from the database
        appointmentAdapter.notifyDataSetChanged();  // Refresh the adapter to update the ListView
    }
}

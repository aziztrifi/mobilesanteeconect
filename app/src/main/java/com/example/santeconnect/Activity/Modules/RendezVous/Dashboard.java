package com.example.santeconnect.Activity.Modules.RendezVous;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.santeconnect.Activity.Entities.Doctor;
import com.example.santeconnect.Activity.Entities.RendezVou;
import com.example.santeconnect.R;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {
    private RecyclerView doctorRecyclerView;
    private DoctorAdapter doctorAdapter;
    private List<Doctor> doctorList;
    private ArrayList<RendezVou> appointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        doctorRecyclerView = findViewById(R.id.doctorRecyclerView);
        doctorRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample data
        doctorList = new ArrayList<>();
        doctorList.add(new Doctor("Dr. Smith", "Cardiologist", R.drawable.image1)); // Replace with your images
        doctorList.add(new Doctor("Dr. Johnson", "Dermatologist", R.drawable.image1));
        doctorList.add(new Doctor("Dr. Lee", "Pediatrician", R.drawable.image1));

        doctorAdapter = new DoctorAdapter(doctorList, this);
        doctorRecyclerView.setAdapter(doctorAdapter);

        // Button to view appointments
        Button viewAppointmentsButton = findViewById(R.id.viewAppointmentsButton);
        viewAppointmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, AppountmentActivity.class);
                startActivity(intent);
            }
        });
    }
}

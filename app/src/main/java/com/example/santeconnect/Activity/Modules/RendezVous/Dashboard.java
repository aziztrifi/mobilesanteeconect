package com.example.santeconnect.Activity.Modules.RendezVous;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.santeconnect.Activity.DAO.UserDAO;
import com.example.santeconnect.Activity.Entities.User;
import com.example.santeconnect.R;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {
    private RecyclerView doctorRecyclerView;
    private DoctorAdapter doctorAdapter;
    private List<User> doctorList;
    private UserDAO userDAO;
    private Button viewAppointmentsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        // Initialize UserDAO
        userDAO = new UserDAO(this);

        // Set up RecyclerView
        doctorRecyclerView = findViewById(R.id.doctorRecyclerView);
        doctorRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize doctor list
        doctorList = new ArrayList<>();

        // Load doctors from database
        new LoadDoctorsTask().execute();

        // Set up button to view appointments
        viewAppointmentsButton = findViewById(R.id.viewAppointmentsButton);
        viewAppointmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, AppointmentListActivity.class);
                startActivity(intent);
            }
        });
    }

    // AsyncTask to load doctors from the database
    private class LoadDoctorsTask extends AsyncTask<Void, Void, List<User>> {
        @Override
        protected List<User> doInBackground(Void... voids) {
            return userDAO.getDoctors(); // Fetch users with role "doctor" from database
        }

        @Override
        protected void onPostExecute(List<User> users) {
            if (users != null && !users.isEmpty()) {
                for (User user : users) {
                    // Create Doctor object and add to doctorList
                    doctorList.add(new User( user.getId(), user.getName(), user.getEmail()));
                }

                // Set up the adapter with the doctor list
                doctorAdapter = new DoctorAdapter(doctorList, Dashboard.this);
                doctorRecyclerView.setAdapter(doctorAdapter);
            } else {

            }
        }
    }
}

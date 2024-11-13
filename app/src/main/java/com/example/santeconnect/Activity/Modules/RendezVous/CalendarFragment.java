package com.example.santeconnect.Activity.Modules.RendezVous;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.santeconnect.Activity.Entities.User;
import com.example.santeconnect.Activity.Modules.User.SessionManager;
import com.example.santeconnect.R;

import java.util.ArrayList;
import java.util.List;

public class CalendarFragment extends Fragment {
    private SessionManager sessionManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the SessionManager
        sessionManager = new SessionManager(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // You might get the user's role from a shared preference, for example:
        String userRole = sessionManager.getSessionDetails("key_session_role");

        // Route based on the user's role
        if ("Doctor".equalsIgnoreCase(userRole)) {
            // Route to Dashboard if user is a patient
            Intent intent = new Intent(getActivity(), AppointmentListActivity.class);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish(); // Optional: close current fragment/activity
            }
            return null; // Stop further execution by returning early
        } else if ("Patient".equalsIgnoreCase(userRole)) {
            // Route to Appointment List if user is not a patient
            Intent intent = new Intent(getActivity(), Dashboard.class);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish(); // Optional: close current fragment/activity
            }
            return null; // Stop further execution by returning early
        }

        // If userRole doesn't match known cases, continue to load the fragment view
        return inflater.inflate(R.layout.dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the RecyclerView and Button here
        RecyclerView doctorRecyclerView = view.findViewById(R.id.doctorRecyclerView);
        doctorRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Sample data (you can customize this part)
        List<User> doctorList = new ArrayList<>();

        DoctorAdapter doctorAdapter = new DoctorAdapter(doctorList, getContext());
        doctorRecyclerView.setAdapter(doctorAdapter);

        // Button to view appointments
        Button viewAppointmentsButton = view.findViewById(R.id.viewAppointmentsButton);
        viewAppointmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click here, for example, open another activity
                Intent intent = new Intent(getActivity(), AppointmentListActivity.class);
                startActivity(intent);
            }
        });
    }
}

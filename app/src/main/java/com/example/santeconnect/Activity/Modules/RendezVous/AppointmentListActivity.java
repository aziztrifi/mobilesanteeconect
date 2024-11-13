package com.example.santeconnect.Activity.Modules.RendezVous;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.santeconnect.Activity.DAO.RendezVousDAO;
import com.example.santeconnect.Activity.Entities.RendezVou;
import com.example.santeconnect.R;

import java.util.ArrayList;
import java.util.List;

public class AppointmentListFragment extends Fragment {
    private static final String TAG = "AppointmentListFragment";

    private ListView appointmentListView;
    private AppointmentAdapter appointmentAdapter;
    private ArrayList<RendezVou> appointments; // Changed to ArrayList for easier manipulation
    private RendezVousDAO dao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_appointment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize DAO
        dao = new RendezVousDAO(getActivity()); // Adjust constructor as necessary for your DAO

        // Initialize appointment list
        appointments = new ArrayList<>();

        // Set up the adapter before loading appointments
        appointmentAdapter = new AppointmentAdapter(getActivity(), appointments);
        appointmentListView = view.findViewById(R.id.appointment_list);
        appointmentListView.setAdapter(appointmentAdapter);

        // Load appointments from the database
        loadAppointmentsFromDatabase();

        // Set an item click listener for the ListView
        appointmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RendezVou selectedAppointment = appointments.get(position);
                Toast.makeText(getActivity(), "Selected: " + selectedAppointment.getDescription(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to load appointments from the database
    private void loadAppointmentsFromDatabase() {
        List<RendezVou> dbAppointments = dao.getAllRendezVous();

        // Log the loaded appointments for debugging purposes
        for (RendezVou rdv : dbAppointments) {
            Log.d(TAG, "Loaded Appointment: " + rdv.toString());
        }

        // Clear the existing list and add all appointments from the database
        appointments.clear();
        appointments.addAll(dbAppointments);

        // Notify adapter of data change
        appointmentAdapter.notifyDataSetChanged();
    }
}

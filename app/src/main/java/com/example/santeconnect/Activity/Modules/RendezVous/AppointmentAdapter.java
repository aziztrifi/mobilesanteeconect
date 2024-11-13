package com.example.santeconnect.Activity.Modules.RendezVous;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.santeconnect.Activity.DAO.RendezVousDAO;
import com.example.santeconnect.Activity.Entities.RendezVou;
import com.example.santeconnect.R;

import java.util.List;


public class AppointmentAdapter extends ArrayAdapter<RendezVou> {
    public AppointmentAdapter(Context context, List<RendezVou> appointments) {
        super(context, 0, appointments);
    }

    private void showUpdateDialog(RendezVou appointment) {
        // Create a dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Update Rendez-vous");

        // Inflate the dialog layout
        View dialogView = LayoutInflater.from(getContext()).inflate(com.example.santeconnect.R.layout.dialog_update_rendez_vou, null);
        builder.setView(dialogView);

        // Get references to the input fields and checkbox
        EditText descriptionInput = dialogView.findViewById(R.id.descriptionInput);
        EditText dateInput = dialogView.findViewById(R.id.dateInput);
        EditText timeInput = dialogView.findViewById(R.id.timeInput);
        CheckBox urgentCheckbox = dialogView.findViewById(R.id.urgentCheckbox);

        // Set existing values
        descriptionInput.setText(appointment.getDescription());
        dateInput.setText(appointment.getDate());
        timeInput.setText(appointment.getTime());
        urgentCheckbox.setChecked(appointment.isUrgent());  // Set checkbox state based on urgency

        // Set up the buttons
        builder.setPositiveButton("Update", (dialog, which) -> {
            // Get updated values
            String updatedDescription = descriptionInput.getText().toString();
            String updatedDate = dateInput.getText().toString();
            String updatedTime = timeInput.getText().toString();
            boolean isUrgent = urgentCheckbox.isChecked();  // Get urgency status from checkbox

            // Create a new RendezVou object with updated values
            appointment.setDescription(updatedDescription);
            appointment.setDate(updatedDate);
            appointment.setTime(updatedTime);
            appointment.setUrgent(isUrgent);  // Update urgency status

            // Update the rendez-vous in the database
            RendezVousDAO dao = new RendezVousDAO(getContext());
            dao.updateRendezVou(appointment);

            // Refresh the list of appointments
            ((AppointmentListActivity) getContext()).loadAppointmentsFromDatabase();

            Toast.makeText(getContext(), "Rendez-vous updated successfully", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if we are reusing a view, otherwise inflate a new one
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_appointment, parent, false);
        }

        // Get the appointment for this position
        RendezVou appointment = getItem(position);

        // Find the TextViews and Buttons in the layout
        TextView descriptionTextView = convertView.findViewById(R.id.appointmentDescriptionTextView);
        TextView dateTimeTextView = convertView.findViewById(R.id.appointmentDateTimeTextView);
        TextView statusTextView = convertView.findViewById(R.id.appointmentStatusTextView);
        TextView urgentTextView = convertView.findViewById(R.id.appointmentUrgentTextView);
        Button acceptButton = convertView.findViewById(R.id.acceptButton);
        Button rejectButton = convertView.findViewById(R.id.rejectButton);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);
        Button updateButton = convertView.findViewById(R.id.updateButton);
        // Set values for the appointment details
        descriptionTextView.setText(appointment.getDescription());
        dateTimeTextView.setText(appointment.getDate() + " at " + appointment.getTime());
        statusTextView.setText("Status: " + appointment.getStatus());
        urgentTextView.setText(appointment.isUrgent() ? "Urgent" : "Not Urgent");

        // Handle button click events if necessary
        acceptButton.setOnClickListener(v -> {
            // Update the status in the database
            RendezVousDAO dao = new RendezVousDAO(getContext());
            dao.acceptRendezVou(appointment.getId());

            // Update the appointment status in the list and notify the adapter
            appointment.setStatus("Accepted");
            notifyDataSetChanged();
            Toast.makeText(getContext(), "Rendezvous accepted successfully", Toast.LENGTH_SHORT).show();

        });

        // Set up the Reject button click listener
        rejectButton.setOnClickListener(v -> {
            // Update the status in the database
            RendezVousDAO dao = new RendezVousDAO(getContext());
            dao.rejectRendezVou(appointment.getId());

            // Update the appointment status in the list and notify the adapter
            appointment.setStatus("Rejected");
            notifyDataSetChanged();
            Toast.makeText(getContext(), "Rendezvous rejected successfully", Toast.LENGTH_SHORT).show();

        });


        deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Confirm Deletion")
                    .setMessage("Are you sure you want to delete this rendez-vous?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // User confirmed deletion
                        RendezVousDAO dao = new RendezVousDAO(getContext());
                        dao.deleteRendezVou(appointment.getId());

                        // Refresh the list of appointments
                        ((AppointmentListActivity) getContext()).loadAppointmentsFromDatabase(); // Call refresh method from Activity
                        Toast.makeText(getContext(), "Rendez-vous deleted successfully", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null) // Do nothing on "No"
                    .show();
        });

        updateButton.setOnClickListener(v -> {
            // Open a dialog or activity to update the rendez-vous
            showUpdateDialog(appointment);
        });



        // Return the completed view to render on screen
        return convertView;
    }
}

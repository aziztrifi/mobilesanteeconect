package com.example.santeconnect.Activity.Modules.RendezVous;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.santeconnect.Activity.Entities.User;
import com.example.santeconnect.R;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private List<User> doctorList;
    private Context context;

    public DoctorAdapter(List<User> doctorList, Context context) {
        this.doctorList = doctorList;
        this.context = context;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        User doctor = doctorList.get(position);
        holder.doctorNameTextView.setText(doctor.getName());
        holder.doctorSpecialtyTextView.setText("Specialty: Unknown"); // Set specialty if available
        holder.doctorImageView.setImageResource(R.drawable.pp); // Replace with doctor's image if available

        // Set click listener for the add appointment button
        holder.addAppointmentButton.setOnClickListener(v -> {
            // Start AppountmentActivity and pass the doctor's ID
            Intent intent = new Intent(context, AppountmentActivity.class);
            intent.putExtra("doctorId", doctor.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    static class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView doctorNameTextView, doctorSpecialtyTextView;
        ImageView doctorImageView;
        Button addAppointmentButton;

        DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorNameTextView = itemView.findViewById(R.id.doctorNameTextView);
            doctorSpecialtyTextView = itemView.findViewById(R.id.doctorSpecialtyTextView);
            doctorImageView = itemView.findViewById(R.id.doctorImageView);
            addAppointmentButton = itemView.findViewById(R.id.addAppointmentButton);
        }
    }
}

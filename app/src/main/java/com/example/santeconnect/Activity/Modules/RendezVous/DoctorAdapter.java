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

import com.example.santeconnect.Activity.Entities.Doctor;
import com.example.santeconnect.R;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {
    private final List<Doctor> doctorList;
    private final Context context;

    public DoctorAdapter(List<Doctor> doctorList, Context context) {
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
        Doctor doctor = doctorList.get(position);
        holder.nameTextView.setText(doctor.getName());
        holder.specialtyTextView.setText(doctor.getSpecialty());
        holder.imageView.setImageResource(doctor.getImageResource());

        holder.bookAppointmentButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, AppountmentActivity.class);
            intent.putExtra("doctorName", doctor.getName()); // Pass the doctor name if needed
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    static class DoctorViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView specialtyTextView;
        Button bookAppointmentButton;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.doctorImageView);
            nameTextView = itemView.findViewById(R.id.doctorNameTextView);
            specialtyTextView = itemView.findViewById(R.id.doctorSpecialtyTextView);
            bookAppointmentButton = itemView.findViewById(R.id.addAppointmentButton);
        }
    }
}

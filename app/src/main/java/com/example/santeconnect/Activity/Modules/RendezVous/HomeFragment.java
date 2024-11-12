package com.example.santeconnect.Activity.Modules.RendezVous;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.santeconnect.R;

public class HomeFragment extends Fragment {

    private ImageView heartRateImage;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homeactivity, container, false); // replace with your actual layout name
        heartRateImage = view.findViewById(R.id.ic_heart_rate); // Ensure this matches the ID in your layout

        // Load the heartbeat animation
        @SuppressLint("ResourceType") Animation heartbeatAnimation = AnimationUtils.loadAnimation(getActivity(), R.layout.heartbeat);
        heartRateImage.startAnimation(heartbeatAnimation); // Start the animation

        return view;
    }
}
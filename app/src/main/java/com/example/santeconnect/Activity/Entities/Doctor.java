package com.example.santeconnect.Activity.Entities;

public class Doctor {
    private String name;
    private String specialty;
    private int imageResource;

    public Doctor(String name, String specialty, int imageResource) {
        this.name = name;
        this.specialty = specialty;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public int getImageResource() {
        return imageResource;
    }
}

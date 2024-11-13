package com.example.santeconnect.Activity.Entities;

public class RendezVou {
    private int id;
    private String date;
    private String time;
    private String description;
    private String status;  // e.g., "Pending", "Completed"
    private boolean urgent; // true for urgent, false for non-urgent
    private int idUserPatient; // ID of the patient
    private int idUserDoctor;  // ID of the doctor

    public RendezVou(int id, String date, String time, String description, String status, boolean urgent, int idUserPatient, int idUserDoctor) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.description = description;
        this.status = status;
        this.urgent = urgent;
        this.idUserPatient = idUserPatient;
        this.idUserDoctor = idUserDoctor;
    }
    public RendezVou( String date, String time, String description, String status, boolean urgent, int idUserPatient, int idUserDoctor) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.description = description;
        this.status = status;
        this.urgent = urgent;
        this.idUserPatient = idUserPatient;
        this.idUserDoctor = idUserDoctor;
    }

    public RendezVou() {
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public int getIdUserPatient() {
        return idUserPatient;
    }

    public void setIdUserPatient(int idUserPatient) {
        this.idUserPatient = idUserPatient;
    }

    public int getIdUserDoctor() {
        return idUserDoctor;
    }

    public void setIdUserDoctor(int idUserDoctor) {
        this.idUserDoctor = idUserDoctor;
    }

    @Override
    public String toString() {
        return "RendezVou{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", urgent=" + urgent +
                ", idUserPatient=" + idUserPatient +
                ", idUserDoctor=" + idUserDoctor +
                '}';
    }
}

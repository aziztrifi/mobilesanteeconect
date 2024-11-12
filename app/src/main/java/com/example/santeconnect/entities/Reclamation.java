package com.example.santeconnect.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import kotlin.jvm.internal.PropertyReference0Impl;

@Entity(tableName = "reclamation")
public class Reclamation {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String email;
    private String description;
    private String type;
    private String status ;
  //  private String newColumn;
    public Reclamation(int id,String name, String email, String description, String type, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.description = description;
        this.type = type;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
   /* public String getNewColumn() {
        return newColumn;
    }
    public void setNewColumn(String newColumn) {
        this.newColumn = newColumn;
    }*/

    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

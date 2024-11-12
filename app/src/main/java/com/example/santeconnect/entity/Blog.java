package com.example.santeconnect.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "blogs")
public class Blog {

    @PrimaryKey(autoGenerate = true)
    private int idblog;
    private String titleofblog;
    private String description;
    private Date date;

    public Blog(int idblog, String titleofblog, String description, Date date) {
        this.idblog = idblog;
        this.titleofblog = titleofblog;
        this.description = description;
        this.date = date;
    }

    public Blog() {

    }


    // Getters and Setters
    public int getIdblog() {
        return idblog;
    }

    public void setIdblog(int idblog) {
        this.idblog = idblog;
    }

    public String getTitleofblog() {
        return titleofblog;
    }

    public void setTitleofblog(String titleofblog) {
        this.titleofblog = titleofblog;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

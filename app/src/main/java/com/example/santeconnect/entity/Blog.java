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
    private byte[] imageblog;  // Store the image as a byte array
    private String imageUri;  // Optional: Store the image URI or file path as a String

    // Constructor with image as byte[] and image URI
    public Blog(int idblog, String titleofblog, String description, Date date, byte[] imageblog, String imageUri) {
        this.idblog = idblog;
        this.titleofblog = titleofblog;
        this.description = description;
        this.date = date;
        this.imageblog = imageblog;
        this.imageUri = imageUri;  // Initialize the image URI
    }

    // Default constructor
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

    public byte[] getImageblog() {
        return imageblog;
    }

    public void setImageblog(byte[] imageblog) {
        this.imageblog = imageblog;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    // Optional: Add a method to format the date into a readable string
    public String getFormattedDate() {
        if (date != null) {
            return new java.text.SimpleDateFormat("dd-MM-yyyy").format(date);
        } else {
            return "";
        }
    }

    // Override toString method for debugging or logging purposes
    @Override
    public String toString() {
        return "Blog{" +
                "idblog=" + idblog +
                ", titleofblog='" + titleofblog + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", imageUri='" + imageUri + '\'' +
                '}';
    }
}

package com.example.santeconnect.Activity.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "comments")
public class Comment {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String author;
    private String content;
    private String date;
    private int blogId; // Foreign key referencing Blog table
    private int userId;
    // Constructor
    public Comment(int blogId, String content,int userId) {
        this.blogId = blogId;
        this.content = content;
        this.author = author;
        this.date = date;
        this.userId = userId;

    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }
    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }
}

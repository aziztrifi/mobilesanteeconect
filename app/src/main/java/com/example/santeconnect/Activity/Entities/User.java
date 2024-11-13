package com.example.santeconnect.Activity.Entities;

import java.util.List;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String role;
    //private String profileImageUri; // New field to store the URI of the profile image
    private List<Blog> blogs;

    // List of comments made by the user
    private List<Comment> comments;
    public User() {
    }

    public User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        //this.profileImageUri = profileImageUri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public List<Blog> getBlogs() { return blogs; }

    public void setBlogs(List<Blog> blogs) { this.blogs = blogs; }

    public List<Comment> getComments() { return comments; }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}

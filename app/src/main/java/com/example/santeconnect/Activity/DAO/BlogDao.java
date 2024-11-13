package com.example.santeconnect.Activity.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.santeconnect.Activity.Entities.Blog;

import java.util.List;

@Dao
public interface BlogDao {
    @Insert
    void insert(Blog blog);

    @Query("SELECT * FROM blogs")
    List<Blog> getAllBlogs();  // Method to fetch all blogs from the database


    @Query("SELECT * FROM blogs WHERE idblog = :idblog")
    Blog getBlogById(int idblog); // New method to fetch a blog by its ID

    @Delete
    void delete(Blog blog);

    @Query("SELECT * FROM blogs WHERE titleofblog LIKE :title")
    List<Blog> searchBlogsByTitle(String title);
}


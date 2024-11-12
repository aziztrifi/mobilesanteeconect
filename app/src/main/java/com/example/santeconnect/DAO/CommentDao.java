package com.example.santeconnect.DAO;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.santeconnect.entity.Comment;

import java.util.List;

@Dao
public interface CommentDao {

    @Insert
    void insert(Comment comment);

    @Update
    void update(Comment comment);

    @Delete
    void delete(Comment comment);

    @Query("SELECT * FROM comments WHERE blogId = :blogId")
    List<Comment> getCommentsForBlog(int blogId);

    @Query("SELECT * FROM comments")
    List<Comment> getAllComments();
}

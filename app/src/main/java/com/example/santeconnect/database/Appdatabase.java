package com.example.santeconnect.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.santeconnect.Activity.DAO.BlogDao;
import com.example.santeconnect.Activity.DAO.CommentDao;

import com.example.santeconnect.Activity.Entities.Blog;
import com.example.santeconnect.Activity.Entities.Comment;
import com.example.santeconnect.adapter.Converters;

@Database(entities = {Blog.class, Comment.class}, version = 2, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class Appdatabase extends RoomDatabase {
    private static Appdatabase instance;

    public abstract BlogDao blogDao();
    public abstract CommentDao commentDao();

    public static synchronized Appdatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            Appdatabase.class, "santeconnect_database+")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}

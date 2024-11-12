package com.example.santeconnect.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.santeconnect.R;
import com.example.santeconnect.adapter.CommentsAdapter;
import com.example.santeconnect.database.Appdatabase;
import com.example.santeconnect.entity.Blog;
import com.example.santeconnect.entity.Comment;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class showblog extends AppCompatActivity {

    private int blogId;
    private Appdatabase db;
    private RecyclerView commentsRecyclerView;
    private TextInputEditText commentInput;
    private ImageView blogImageView;  // ImageView to display the blog image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showblog);

        // Initialize the database instance
        db = Appdatabase.getInstance(this);

        // Initialize the ImageView
        blogImageView = findViewById(R.id.blogImageView);

        // Get the blog ID from the Intent
        Intent intent = getIntent();
        blogId = intent.getIntExtra("blogId", -1);  // Default value is -1 if no ID is passed

        if (blogId == -1) {
            Log.d("showblog", "Invalid blog ID passed");
            Toast.makeText(this, "Invalid blog ID passed", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Proceed to fetch and display the blog details
        loadBlogDetails(blogId);

        Button submitCommentButton = findViewById(R.id.button3);
        commentInput = findViewById(R.id.editTextEmail);
        commentsRecyclerView = findViewById(R.id.comments_recycler_view);

        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadComments(blogId);

        submitCommentButton.setOnClickListener(v -> {
            String commentText = commentInput.getText().toString();
            if (!commentText.isEmpty()) {
                Comment comment = new Comment(blogId, commentText);

                new Thread(() -> {
                    db.commentDao().insert(comment);

                    runOnUiThread(() -> {
                        loadComments(blogId);
                        commentInput.setText("");
                    });
                }).start();
            } else {
                Toast.makeText(this, "Please write a comment", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Function to load blog details from the database
    private void loadBlogDetails(int idBlog) {
        new Thread(() -> {
            // Fetch the blog from the database
            Blog blog = db.blogDao().getBlogById(idBlog);

            if (blog != null) {
                Log.d("showblog", "Blog found: " + blog.getTitleofblog());

                runOnUiThread(() -> {
                    TextView blogTitle = findViewById(R.id.titleblog1);
                    TextView blogDate = findViewById(R.id.blogDate);
                    TextView blogDescription = findViewById(R.id.postblog);

                    if (blogTitle != null) {
                        blogTitle.setText(blog.getTitleofblog());
                    }
                    if (blogDate != null && blog.getDate() != null) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        String formattedDate = dateFormat.format(blog.getDate());
                        blogDate.setText(formattedDate);
                    }
                    if (blogDescription != null) {
                        blogDescription.setText(blog.getDescription());
                    }

                    // Handle Image Display (if the image exists)
                    if (blog.getImageblog() != null && blog.getImageblog().length > 0) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(blog.getImageblog(), 0, blog.getImageblog().length);
                        blogImageView.setImageBitmap(bitmap);  // Set the image in ImageView
                    } else {
                        // If no image, set a default placeholder
                        blogImageView.setImageResource(R.drawable.ic_search);
                    }
                });
            } else {
                Log.d("showblog", "No blog found with ID: " + idBlog);
            }
        }).start();
    }

    private void loadComments(int blogId) {
        new Thread(() -> {
            List<Comment> comments = db.commentDao().getCommentsForBlog(blogId);

            runOnUiThread(() -> {
                CommentsAdapter adapter = new CommentsAdapter(comments);
                commentsRecyclerView.setAdapter(adapter);
            });
        }).start();
    }
}

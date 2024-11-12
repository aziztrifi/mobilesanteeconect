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

public class showblog extends AppCompatActivity {

    private int blogId;  // Make this a field to reuse in multiple places
    private Appdatabase db;
    private RecyclerView commentsRecyclerView;
    private TextInputEditText commentInput;
    private String username = "YourUsername";  // Replace with actual logic for retrieving username

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showblog);

        // Initialize the database instance
        db = Appdatabase.getInstance(this);

        // Get the blog ID from the Intent
        Intent intent = getIntent();
        blogId = intent.getIntExtra("blogId", -1);  // Default value is -1 if no ID is passed

        if (blogId == -1) {
            // If the ID is invalid, show a message or handle the error
            Log.d("showblog", "Invalid blog ID passed");
            Toast.makeText(this, "Invalid blog ID passed", Toast.LENGTH_SHORT).show();
            finish();  // Optionally, finish the activity or show an error screen
            return;
        }

        // Proceed to fetch and display the blog details
        loadBlogDetails(blogId);

        // Set up the comment submission logic
        Button submitCommentButton = findViewById(R.id.button3);
        commentInput = findViewById(R.id.editTextEmail);
        commentsRecyclerView = findViewById(R.id.comments_recycler_view);

        // Set up the RecyclerView layout manager
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load existing comments for the blog
        loadComments(blogId);

        submitCommentButton.setOnClickListener(v -> {
            String commentText = commentInput.getText().toString();
            if (!commentText.isEmpty()) {
                // Create a new Comment object with the blogId, comment text, username, and current date
                Comment comment = new Comment(blogId,commentText);

                // Insert the comment in the database
                new Thread(() -> {
                    db.commentDao().insert(comment);

                    // Reload comments to reflect the new one
                    runOnUiThread(() -> {
                        loadComments(blogId);
                        commentInput.setText("");  // Clear the input field after submission
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
                Log.d("showblog", "Blog found: " + blog.getTitleofblog());  // Check if blog is fetched

                runOnUiThread(() -> {
                    TextView blogTitle = findViewById(R.id.titleblog1);
                    TextView blogDate = findViewById(R.id.blogDate);
                    TextView blogDescription = findViewById(R.id.postblog);
                    TextView authorName = findViewById(R.id.textView8);  // Ensure correct ID for author name

                    // Ensure the TextViews exist and then set the values
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
                    //if (authorName != null) {
                        //authorName.setText(blog.getAuthorName());
                   // }
                });
            } else {
                Log.d("showblog", "No blog found with ID: " + idBlog);
            }
        }).start();
    }

    // Function to load comments from the database and update the RecyclerView
    private void loadComments(int blogId) {
        new Thread(() -> {
            // Fetch the comments for the blog from the database
            List<Comment> comments = db.commentDao().getCommentsForBlog(Integer.parseInt(String.valueOf(blogId)));

            // Update the RecyclerView on the main thread
            runOnUiThread(() -> {
                CommentsAdapter adapter = new CommentsAdapter(comments);
                commentsRecyclerView.setAdapter(adapter);
            });
        }).start();
    }

    // Function to get the current date as a string
    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return dateFormat.format(new Date());
    }
}

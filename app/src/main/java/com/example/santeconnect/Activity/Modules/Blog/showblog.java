package com.example.santeconnect.Activity.Modules.Blog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.santeconnect.Activity.DAO.UserDAO;
import com.example.santeconnect.Activity.Entities.Blog;
import com.example.santeconnect.Activity.Entities.Comment;
import com.example.santeconnect.Activity.Entities.User;
import com.example.santeconnect.Activity.Modules.User.SessionManager;
import com.example.santeconnect.R;
import com.example.santeconnect.adapter.CommentsAdapter;
import com.example.santeconnect.database.Appdatabase;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class showblog extends AppCompatActivity {

    private int blogId;
    private UserDAO userDAO;
    private Appdatabase db;
    private RecyclerView commentsRecyclerView;
    private TextInputEditText commentInput;
    private ImageView blogImageView;  // ImageView to display the blog image
    private SessionManager sessionManager;
    private int userId;  // Store the userId from session
    private int authorId;  // Store the authorId of the blog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showblog);

        // Initialize the Appdatabase instance, UserDAO, and SessionManager directly
        db = Appdatabase.getInstance(this); // Correctly initialize db
        userDAO = new UserDAO(this); // Initialize UserDAO
        sessionManager = new SessionManager(this);

        // Retrieve userId from session
        String userEmail = sessionManager.getSessionDetails("key_session_email");
        if (userEmail != null) {
            userId = userDAO.getUserIdByEmail(userEmail); // Get userId by email
        } else {
            userId = -1;
        }

        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Proceed with other initializations as before
        blogImageView = findViewById(R.id.blogImageView);

        Intent intent = getIntent();
        blogId = intent.getIntExtra("blogId", -1);  // Default value is -1 if no ID is passed

        if (blogId == -1) {
            Log.d("showblog", "Invalid blog ID passed");
            Toast.makeText(this, "Invalid blog ID passed", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadBlogDetails(blogId);

        Button submitCommentButton = findViewById(R.id.button3);
        commentInput = findViewById(R.id.editTextEmail);
        commentsRecyclerView = findViewById(R.id.comments_recycler_view);

        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadComments(blogId);

        submitCommentButton.setOnClickListener(v -> {
            String commentText = commentInput.getText().toString();
            if (!commentText.isEmpty()) {
                Comment comment = new Comment(blogId, commentText, userId);

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
                authorId = blog.getUserId();  // Get the author's userId

                runOnUiThread(() -> {
                    TextView blogTitle = findViewById(R.id.titleblog1);
                    TextView blogDate = findViewById(R.id.blogDate);
                    TextView blogDescription = findViewById(R.id.postblog);
                    TextView authorNameTextView = findViewById(R.id.textView8); // TextView to display the author's name

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

                    // Load the author's name
                    loadAuthorName(authorId, authorNameTextView);
                });
            } else {
                Log.d("showblog", "No blog found with ID: " + idBlog);
            }
        }).start();
    }

    private void loadAuthorName(int authorId, TextView authorNameTextView) {
        new Thread(() -> {
            // Fetch the author from the database
            User author = userDAO.getUserById(authorId);

            runOnUiThread(() -> {
                if (author != null) {
                    authorNameTextView.setText(author.getName());
                } else {
                    authorNameTextView.setText("Unknown Author");
                }
            });
        }).start();
    }

    private void loadComments(int blogId) {
        new Thread(() -> {
            List<Comment> comments = db.commentDao().getCommentsForBlog(blogId);

            runOnUiThread(() -> {
                CommentsAdapter adapter = new CommentsAdapter(showblog.this, comments);
                commentsRecyclerView.setAdapter(adapter);
            });
        }).start();
    }
}

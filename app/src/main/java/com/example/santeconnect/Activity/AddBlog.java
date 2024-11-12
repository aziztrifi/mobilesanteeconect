package com.example.santeconnect.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.santeconnect.DAO.BlogDao;
import com.example.santeconnect.R;
import com.example.santeconnect.database.Appdatabase;
import com.example.santeconnect.entity.Blog;

import java.util.Date;

public class AddBlog extends AppCompatActivity {

    private EditText titleEditText, descriptionEditText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addblog);

        // Initialize views
        titleEditText = findViewById(R.id.blogTitle);
        descriptionEditText = findViewById(R.id.blogDescription);
        submitButton = findViewById(R.id.submitPostButton);

        // Set onClickListener for the submit button
        submitButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String description = descriptionEditText.getText().toString();

            // Validate inputs
            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
                Toast.makeText(AddBlog.this, "Title and Description are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a new Blog object
            Blog blog = new Blog();
            blog.setTitleofblog(title);
            blog.setDescription(description);
            blog.setDate(new Date());

            // Insert the Blog into the database
            new Thread(() -> {
                // Get the database instance and DAO
                Appdatabase db = Appdatabase.getInstance(AddBlog.this);
                BlogDao blogDao = db.blogDao();

                // Insert the blog into the database
                blogDao.insert(blog);

                // Show a Toast on the main thread once insertion is done
                runOnUiThread(() -> {
                    Toast.makeText(AddBlog.this, "Blog added successfully", Toast.LENGTH_SHORT).show();

                    // Optionally, navigate back to BlogActivity or refresh RecyclerView
                    // Navigate back to BlogActivity (if needed)
                    finish();  // Close AddBlog and go back to previous activity
                });
            }).start();
        });
    }
}

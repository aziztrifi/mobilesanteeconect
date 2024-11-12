package com.example.santeconnect.Activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.santeconnect.DAO.BlogDao;
import com.example.santeconnect.R;
import com.example.santeconnect.database.Appdatabase;
import com.example.santeconnect.entity.Blog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
public class AddBlog extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;  // Request code for image picker
    private EditText titleEditText, descriptionEditText;
    private Button submitButton;
    private ImageView imageView;
    private Uri imageUri;  // Store the URI of the selected image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addblog);

        // Initialize views
        titleEditText = findViewById(R.id.blogTitle);
        descriptionEditText = findViewById(R.id.blogDescription);
        submitButton = findViewById(R.id.submitPostButton);
        imageView = findViewById(R.id.imageView8);

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

            // If an image is selected, convert URI to byte array and save it
            if (imageUri != null) {
                byte[] imageByteArray = getImageByteArray(imageUri);
                blog.setImageblog(imageByteArray);  // Store the image as a byte array
            }

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
                    finish();  // Close AddBlog and go back to previous activity
                });
            }).start();
        });

        // Set onClickListener for the ImageView to open the gallery for image selection
        imageView.setOnClickListener(v -> openImagePicker());
    }

    // Open gallery for image selection
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");  // Set MIME type for images
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handle the result of image selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();  // Get the URI of the selected image
            imageView.setImageURI(imageUri);  // Display the selected image in the ImageView
        }
    }

    // Method to convert image URI to byte array
    private byte[] getImageByteArray(Uri imageUri) {
        try {
            ContentResolver contentResolver = getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Compress the Bitmap to byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

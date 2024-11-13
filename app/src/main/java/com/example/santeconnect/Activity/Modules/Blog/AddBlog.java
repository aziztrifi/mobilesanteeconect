package com.example.santeconnect.Activity.Modules.Blog;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.santeconnect.Activity.DAO.BlogDao;
import com.example.santeconnect.Activity.DAO.UserDAO;
import com.example.santeconnect.Activity.Entities.Blog;
import com.example.santeconnect.Activity.Modules.User.SessionManager;
import com.example.santeconnect.R;
import com.example.santeconnect.database.Appdatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddBlog extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText titleEditText, descriptionEditText, blogDateEditText;
    private Button submitButton;
    private ImageView imageView;
    private Uri imageUri;
    private Calendar calendar;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addblog);

        // Initialize views
        titleEditText = findViewById(R.id.blogTitle);
        descriptionEditText = findViewById(R.id.blogDescription);
        submitButton = findViewById(R.id.submitPostButton);
        imageView = findViewById(R.id.imageView8);
        blogDateEditText = findViewById(R.id.blogDate);

        // Initialize the calendar for DatePickerDialog
        calendar = Calendar.getInstance();

        // Initialize SessionManager to get the logged-in user's details
        sessionManager = new SessionManager(this);

        // Set onClickListener for the submit button
        submitButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String date = blogDateEditText.getText().toString();

            // Validate inputs
            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || TextUtils.isEmpty(date)) {
                Toast.makeText(AddBlog.this, "Title, Description, and Date are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get the user's email from the session
            String email = sessionManager.getSessionDetails("key_session_email");

            if (email == null) {
                Toast.makeText(AddBlog.this, "User is not logged in", Toast.LENGTH_SHORT).show();
                return;
            }

            // Fetch userId using UserDAO and email
            UserDAO userDAO = new UserDAO(this);
            int userId = userDAO.getUserIdByEmail(email);

            if (userId == -1) {
                Toast.makeText(AddBlog.this, "User not found", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a new Blog object
            Blog blog = new Blog();
            blog.setTitleofblog(title);
            blog.setDescription(description);
            blog.setDate(new Date()); // Use the current date or a selected date if needed
            blog.setUserId(userId); // Assign the user ID to the blog

            // If an image is selected, convert URI to byte array and save it
            if (imageUri != null) {
                byte[] imageByteArray = getImageByteArray(imageUri);
                if (imageByteArray != null) {
                    blog.setImageblog(imageByteArray); // Store the image as a byte array
                }
            }

            // Insert the Blog into the database
            new Thread(() -> {
                Appdatabase db = Appdatabase.getInstance(AddBlog.this);
                BlogDao blogDao = db.blogDao();
                blogDao.insert(blog);

                runOnUiThread(() -> {
                    Toast.makeText(AddBlog.this, "Blog added successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close AddBlog and go back to previous activity
                });
            }).start();
        });

        // Set onClickListener for the ImageView to open the gallery for image selection
        imageView.setOnClickListener(v -> openImagePicker());

        // Set onClickListener for the Date EditText to open DatePickerDialog
        blogDateEditText.setOnClickListener(v -> openDatePickerDialog());
    }

    // Open gallery for image selection
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*"); // Set MIME type for images
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handle the result of image selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData(); // Get the URI of the selected image
            imageView.setImageURI(imageUri); // Display the selected image in the ImageView
        }
    }

    // Method to convert image URI to byte array
    private byte[] getImageByteArray(Uri imageUri) {
        try {
            ContentResolver contentResolver = getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(imageUri);
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                // Resize the image if needed to optimize memory usage
                int maxSize = 1024; // Set the max size (in pixels) for resizing
                Bitmap resizedBitmap = resizeImage(bitmap, maxSize);

                // Compress the Bitmap to byte array
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                return byteArrayOutputStream.toByteArray();
            } else {
                Log.e("AddBlog", "InputStream is null");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to resize the image to avoid memory issues with large images
    private Bitmap resizeImage(Bitmap originalBitmap, int maxSize) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        float ratio = Math.min((float) maxSize / width, (float) maxSize / height);
        int newWidth = Math.round(ratio * width);
        int newHeight = Math.round(ratio * height);

        return Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, false);
    }

    // Open the DatePickerDialog when the EditText is clicked
    private void openDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddBlog.this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String formattedDate = dateFormat.format(calendar.getTime());
                    blogDateEditText.setText(formattedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }
}

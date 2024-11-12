package com.example.santeconnect.Activity;

import android.app.DatePickerDialog;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddBlog extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;  // Request code for image picker
    private EditText titleEditText, descriptionEditText, blogDateEditText;
    private Button submitButton;
    private ImageView imageView;
    private Uri imageUri;  // Store the URI of the selected image
    private Calendar calendar;

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

            // Create a new Blog object
            Blog blog = new Blog();
            blog.setTitleofblog(title);
            blog.setDescription(description);
            blog.setDate(new Date()); // Using current date, you can replace it with the selected date if needed

            // If an image is selected, convert URI to byte array and save it
            if (imageUri != null) {
                byte[] imageByteArray = getImageByteArray(imageUri);
                if (imageByteArray != null) {
                    blog.setImageblog(imageByteArray);  // Store the image as a byte array
                }
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

        // Set onClickListener for the Date EditText to open DatePickerDialog
        blogDateEditText.setOnClickListener(v -> openDatePickerDialog());
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
                    // Set the calendar with the selected date
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // Format the date to display in the EditText
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String formattedDate = dateFormat.format(calendar.getTime());

                    // Set the formatted date in the EditText
                    blogDateEditText.setText(formattedDate);
                },
                calendar.get(Calendar.YEAR), // Set current year
                calendar.get(Calendar.MONTH), // Set current month
                calendar.get(Calendar.DAY_OF_MONTH) // Set current day
        );

        // Show the DatePickerDialog
        datePickerDialog.show();
    }
}

package com.example.santeconnect.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.santeconnect.Fragment.BlogFragment;
import com.example.santeconnect.R;
import com.example.santeconnect.adapter.BlogAdapter;
import com.example.santeconnect.database.Appdatabase;
import com.example.santeconnect.entity.Blog;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Collections;
import java.util.List;

public class BlogActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BlogAdapter blogAdapter;
    private List<Blog> blogList;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blogs);

        recyclerView = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.searchEditText);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch the data from the database and update the RecyclerView
        fetchBlogsFromDatabase();

        // Add a listener for search input
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Perform search as the user types
                performSearch(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed here
            }

        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set the default fragment when the activity is first created
        loadFragment(new BlogFragment());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_trophy) {
                selectedFragment = new BlogFragment();  // Replace with your desired fragment
            } else if (item.getItemId() == R.id.nav_play) {
                selectedFragment = new BlogFragment();  // Replace with your desired fragment
            } else if (item.getItemId() == R.id.nav_settings) {
                selectedFragment = new BlogFragment();  // Replace with your desired fragment
            } else if (item.getItemId() == R.id.nav_power) {
                selectedFragment = new BlogFragment();  // Replace with your desired fragment
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });

    }

    // Method to load the selected fragment
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

        private void performSearch(String searchQuery) {
        new Thread(() -> {
            // Get the instance of the database and search for blogs by title
            Appdatabase db = Appdatabase.getInstance(this);
            blogList = db.blogDao().searchBlogsByTitle("%" + searchQuery + "%");

            // Log the size of the fetched list
            Log.d("BlogActivity", "Searched blogs: " + (blogList != null ? blogList.size() : 0));

            // Update the RecyclerView on the main thread
            runOnUiThread(() -> {
                if (blogList != null && !blogList.isEmpty()) {
                    if (blogAdapter == null) {
                        blogAdapter = new BlogAdapter(BlogActivity.this, blogList);
                        recyclerView.setAdapter(blogAdapter);
                    } else {
                        blogAdapter.updateBlogList(blogList);
                    }
                } else {
                    // If no blogs found, clear the RecyclerView
                    if (blogAdapter != null) {
                        blogAdapter.updateBlogList(Collections.emptyList()); // Clear the list in adapter
                    }
                    Toast.makeText(BlogActivity.this, "No blogs found", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    // Method to fetch all blogs from the database when loading
    private void fetchBlogsFromDatabase() {
        new Thread(() -> {
            Appdatabase db = Appdatabase.getInstance(this);
            blogList = db.blogDao().getAllBlogs();

            runOnUiThread(() -> {
                if (blogList != null && !blogList.isEmpty()) {
                    blogAdapter = new BlogAdapter(BlogActivity.this, blogList);
                    recyclerView.setAdapter(blogAdapter);
                } else {
                    Toast.makeText(BlogActivity.this, "No blogs found", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    // Method to refresh the blogs when a new blog is added
    public void refreshBlogs() {
        fetchBlogsFromDatabase();
    }

    public void onLoginClick(View view) {
        try {
            // Start the AddBlog activity
            Intent intent = new Intent(BlogActivity.this, AddBlog.class);
            startActivityForResult(intent, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Handle result from AddBlog activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Refresh the blog list after adding a new blog
            refreshBlogs();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBlogsFromDatabase();
    }

    private void loadBlogsFromDatabase() {
        fetchBlogsFromDatabase();
    }
}

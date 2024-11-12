package com.example.santeconnect.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.santeconnect.Activity.showblog;
import com.example.santeconnect.R;
import com.example.santeconnect.database.Appdatabase;
import com.example.santeconnect.entity.Blog;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {

    private List<Blog> blogList;
    private Context context;

    // Constructor to initialize context and blog list
    public BlogAdapter(Context context, List<Blog> blogList) {
        this.context = context;  // Initialize context
        this.blogList = blogList;
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {

        TextView blogTitle, blogDate, blogDescription;
        ImageView ii, detailsButton;

        public BlogViewHolder(View itemView) {
            super(itemView);
            blogTitle = itemView.findViewById(R.id.blogTitle);
            blogDate = itemView.findViewById(R.id.blogDate);
            blogDescription = itemView.findViewById(R.id.blogDescription);
            ii = itemView.findViewById(R.id.deleteButton);
            detailsButton = itemView.findViewById(R.id.show);
        }
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_blog_item, parent, false);
        return new BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
        Blog blog = blogList.get(position);

        holder.blogTitle.setText(blog.getTitleofblog());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        holder.blogDate.setText(sdf.format(blog.getDate()));
        holder.blogDescription.setText(blog.getDescription());

        // Set the click listener for the details button
        holder.detailsButton.setOnClickListener(v -> {
            // Check if the blog ID is valid (greater than 0)
            if (blog.getIdblog() > 0) {
                // Launch the ShowBlog activity to view blog details
                Intent intent = new Intent(context, showblog.class);
                intent.putExtra("blogId", blog.getIdblog()); // Pass the blog ID
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Invalid blog ID", Toast.LENGTH_SHORT).show();
            }
        });



        // Handle delete button click
        holder.ii.setOnClickListener(v -> {
            deleteBlog(blog, position);
        });
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public void updateBlogList(List<Blog> newBlogList) {
        this.blogList = newBlogList;
        notifyDataSetChanged();
    }

    // Method to insert a new blog and update the RecyclerView
    public void insertNewBlog(Blog newBlog) {
        blogList.add(newBlog);  // Add new blog to the list
        notifyDataSetChanged();  // Notify the adapter that the data set has changed
    }

    // Method to delete a blog and update the RecyclerView
    private void deleteBlog(Blog blog, int position) {
        new Thread(() -> {
            Appdatabase db = Appdatabase.getInstance(context);
            db.blogDao().delete(blog);  // Delete the blog from the database

            ((Activity) context).runOnUiThread(() -> {
                blogList.remove(position);  // Remove the blog from the list by position
                notifyItemRemoved(position);  // Notify the adapter of the data change
                notifyItemRangeChanged(position, blogList.size());  // Update the remaining list
                Toast.makeText(context, "Blog deleted", Toast.LENGTH_SHORT).show();
            });
        }).start();
    }
}
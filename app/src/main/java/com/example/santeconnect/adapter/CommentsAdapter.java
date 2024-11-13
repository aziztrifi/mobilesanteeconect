package com.example.santeconnect.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.santeconnect.Activity.DAO.UserDAO;
import com.example.santeconnect.Activity.Entities.Comment;
import com.example.santeconnect.Activity.Entities.User;
import com.example.santeconnect.R;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private List<Comment> comments;
    private Context context;

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_commentitem, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.commentText.setText(comment.getContent());
        holder.date.setText(comment.getDate());

        // Fetch user details asynchronously
        new FetchUserTask(holder, context).execute(comment.getUserId());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentText, username, date;

        CommentViewHolder(View itemView) {
            super(itemView);
            commentText = itemView.findViewById(R.id.comment_text);
            username = itemView.findViewById(R.id.comment_user_name);
            date = itemView.findViewById(R.id.comment_date);
        }
    }

    private static class FetchUserTask extends AsyncTask<Integer, Void, User> {
        private CommentViewHolder holder;
        private Context context;

        FetchUserTask(CommentViewHolder holder, Context context) {
            this.holder = holder;
            this.context = context;
        }

        @Override
        protected User doInBackground(Integer... userIds) {
            UserDAO userDAO = new UserDAO(context);
            return userDAO.getUserById(userIds[0]); // Add a method in UserDAO to get User by ID
        }

        @Override
        protected void onPostExecute(User user) {
            if (user != null) {
                holder.username.setText(user.getName());
            } else {
                holder.username.setText("Unknown User");
            }
        }
    }
}

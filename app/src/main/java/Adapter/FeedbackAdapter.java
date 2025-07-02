package Adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rental.R;

import java.util.List;

import retrofit.CommentListResponse;
import retrofit.CommentResponse;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {

    private List<CommentListResponse.Data> commentList;
    private OnCommentInteractionListener listener;
    String userId;

    public FeedbackAdapter(List<CommentListResponse.Data> commentList, OnCommentInteractionListener listener) {
        this.commentList = commentList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentListResponse.Data comment = commentList.get(position);
        Log.d(TAG, "Binding comment: " + comment.getContent());

        // Bind data to the views
        holder.commentTextView.setText(comment.getContent());
        holder.userPhoneNumber.setText(comment.getUser().getName());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(comment);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(comment);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView commentTextView;
        public TextView userPhoneNumber;
        public ImageButton deleteButton;
        public ImageView avatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commentTextView = itemView.findViewById(R.id.commentText);
            userPhoneNumber = itemView.findViewById(R.id.userPhoneNumber);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            avatar = itemView.findViewById(R.id.avatar);
        }
    }

    public interface OnCommentInteractionListener {
        void onEditClick(CommentListResponse.Data comment);
        void onDeleteClick(CommentListResponse.Data comment);
    }
}

package com.example.eventmanageri.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.example.eventmanageri.Models.Comment;
import com.example.eventmanageri.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context mContext;
    private List<Comment> mData;

    public CommentAdapter(Context mContext, List<com.example.eventmanageri.Models.Comment> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.event_comment_item, parent, false);
        return new CommentViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.tvUser.setText(mData.get(position).getUname());
        holder.tvComment.setText(mData.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{

        TextView tvUser, tvComment;

        public CommentViewHolder (View itemView) {
            super(itemView);
            tvUser = itemView.findViewById(R.id.user_viewTxt);
            tvComment = itemView.findViewById(R.id.comment_txtView);
        }
    }

}

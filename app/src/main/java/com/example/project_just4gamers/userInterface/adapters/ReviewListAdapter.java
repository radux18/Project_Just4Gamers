package com.example.project_just4gamers.userInterface.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.models.Review;
import com.example.project_just4gamers.models.User;
import com.example.project_just4gamers.utilities.glideLoader.GlideLoader;

import java.util.ArrayList;

public class ReviewListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private ArrayList<Review> reviewArrayList;

    public ReviewListAdapter(Context context, ArrayList<Review> reviewArrayList) {
        this.context = context;
        this.reviewArrayList = reviewArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder( LayoutInflater.from(context).inflate(
                R.layout.item_review_layout,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Review model = reviewArrayList.get(position);
        User user = model.getUser();

        if (holder instanceof ViewHolder){
            ((ViewHolder) holder).tvDescription.setText(model.getDescription());
            ((ViewHolder) holder).tvDate.setText(model.getDate());
            ((ViewHolder) holder).tvTitle.setText(model.getTitle());
            ((ViewHolder) holder).rbScore.setRating(model.getScore());
            ((ViewHolder) holder).tvName.setText(context.getResources().getString(R.string.tv_settings_name, model.getUser().getFirstName(), model.getUser().getLastName()));
           new GlideLoader(context).loadUserPicture(user.getImage(), ((ViewHolder) holder).ivProfileImg);
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvDate;
        private TextView tvName;
        private TextView tvDescription;
        private RatingBar rbScore;
        private ImageView ivProfileImg;


        public ViewHolder(View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_review_title);
            tvDate = itemView.findViewById(R.id.tv_review_date);
            tvDescription = itemView.findViewById(R.id.tv_review_description);
            rbScore = itemView.findViewById(R.id.rb_score);
            ivProfileImg = itemView.findViewById(R.id.iv_user_image);
            tvName = itemView.findViewById(R.id.tv_user_name);
        }
    }
}

package com.example.project_just4gamers.ui.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.FirestoreManager;
import com.example.project_just4gamers.models.Review;
import com.example.project_just4gamers.models.User;
import com.example.project_just4gamers.utils.GlideLoader;

import java.util.ArrayList;

public class ReviewListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private ArrayList<Review> reviewArrayList;
    private User currentUser;

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
        String userId = model.getUser_id();

        new FirestoreManager().getUser(ReviewListAdapter.this,userId);

        successGetUser(currentUser);
        System.out.println("AICIV2 + " + currentUser.toString());
        if (holder instanceof ViewHolder){
            ((ViewHolder) holder).tvDescription.setText(model.getDescription());
            ((ViewHolder) holder).tvDate.setText(model.getDate());
            ((ViewHolder) holder).tvTitle.setText(model.getTitle());
            ((ViewHolder) holder).rbScore.setRating(model.getScore());

            new GlideLoader(context).loadUserPicture(currentUser.getImage(), ((ViewHolder) holder).ivProfileImg);
           //de pus si poza de profil al utiliz

        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void successGetUser(User user){
        System.out.println("AICI + " + user.toString());
        currentUser = user;
    }

    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvDate;
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

        }
    }
}

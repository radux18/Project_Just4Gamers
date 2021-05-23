package com.example.project_just4gamers.utilities.glideLoader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.project_just4gamers.R;

public class GlideLoader  {

    private Context context ;

    public GlideLoader(Context context) {
        this.context = context;
    }

    public void loadUserPicture(Object imageUri, ImageView imageView){
        try {
            //Load the user image in the ImageView
            Glide
                    .with(context)
                    .load(imageUri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_profile_placeholder)
                    .into(imageView);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadProductPicture(Object imageUri, ImageView imageView){
        try {
            //Load the user image in the ImageView
            Glide
                    .with(context)
                    .load(imageUri)
                    .centerCrop()
                    .into(imageView);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

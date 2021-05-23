package com.example.project_just4gamers.userInterface.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.CloudFirestoreManager;
import com.example.project_just4gamers.models.SoldProduct;
import com.example.project_just4gamers.models.User;
import com.example.project_just4gamers.utilities.Constants;
import com.example.project_just4gamers.utilities.glideLoader.GlideLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class PodiumListAdapterV2 extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private ArrayList<User> userList;
    private ArrayList<SoldProduct> soldProducts;
    private static int TYPE_BRONZE = 2;
    private static int TYPE_SILVER = 1;
    private static int TYPE_GOLD = 0;
    private static int TYPE_DEFAULT = 3;

    public PodiumListAdapterV2(Context context, ArrayList<User> userList, ArrayList<SoldProduct> soldProductList) {
        this.context = context;
        this.userList = userList;
        this.soldProducts = soldProductList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        if (viewType == TYPE_BRONZE) {
            view = LayoutInflater.from(context).inflate(R.layout.item_userbronzestate_layout,
                    parent,
                    false);
            return new ViewHolder(view);
        } else if (viewType == TYPE_SILVER){
            view = LayoutInflater.from(context).inflate(R.layout.item_usersilverstate_layout,
                    parent,
                    false);
            return new ViewHolder(view);
        } else if (viewType == TYPE_GOLD) {
            view = LayoutInflater.from(context).inflate(R.layout.item_usergoldstate_layout,
                    parent,
                    false);
            return new ViewHolder(view);
         }
        else {
            view = LayoutInflater.from(context).inflate(R.layout.item_userdefaultstate_layout,
                    parent,
                    false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        User user = userList.get(position);
        HashMap<String, Object> userHashMap = new HashMap<>();
        int counter = 0;

        for (SoldProduct soldProduct : soldProducts){
            if (soldProduct.getUser_id().equals(user.getId())){
                counter ++;
            }
        }

        userHashMap.put(Constants.getSOLDPRODUCTS(), counter);
        new CloudFirestoreManager().updateSoldProductsUser(user.getId() ,userHashMap);

        if (holder instanceof ViewHolder){
            ((ViewHolder) holder).tvPosition.setText(String.valueOf(holder.getAdapterPosition() + 1));
            ((ViewHolder) holder).tvName.setText(context.getString(R.string.tv_settings_name, user.getFirstName(), user.getLastName()));
            ((ViewHolder) holder).tvPoints.setText(String.valueOf(counter));
            new GlideLoader(context).loadProductPicture(user.getImage(), ((ViewHolder) holder).ivProfile);

        }
    }



    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_GOLD;
            case 1:
                return TYPE_SILVER;
            case 2:
                return TYPE_BRONZE;
            default:
                return TYPE_DEFAULT;
        }
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvPoints;
        private ImageView ivProfile;
        private TextView tvPosition;

        public ViewHolder(View itemView){
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_userName);
            tvPoints = itemView.findViewById(R.id.tv_userPoints);
            ivProfile = itemView.findViewById(R.id.iv_profile_image);
            tvPosition = itemView.findViewById(R.id.tv_position);
        }
    }
}

package com.example.project_just4gamers.userInterface.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.models.Product;
import com.example.project_just4gamers.userInterface.activities.ProductDetailsActivity;
import com.example.project_just4gamers.utilities.Constants;
import com.example.project_just4gamers.utilities.glideLoader.GlideLoader;

import java.util.ArrayList;

public class FavoriteProductsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private ArrayList<Product> products;

    public FavoriteProductsListAdapter(@NonNull Context context, @NonNull ArrayList<Product> product) {
        this.context = context;
        this.products = product;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.item_favorites_layout,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder itemList, int position) {
        Product model = products.get(position);

        if (itemList instanceof FavoriteProductsListAdapter.ViewHolder){
            new GlideLoader(context).loadProductPicture(model.getImage(), ((ViewHolder) itemList).image);
            ((ViewHolder) itemList).tvName.setText(model.getTitle());
            ((ViewHolder) itemList).tvPrice.setText(context.getResources().getString(R.string.item_price_format,String.valueOf(model.getPrice())));
            ((ViewHolder) itemList).tvUserName.setText(model.getUser_name());

            itemList.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductDetailsActivity.class);
                    intent.putExtra(Constants.getExtraProductId(), model.getProduct_id());
                    intent.putExtra(Constants.getExtraProductOwnerId(), model.getUser_id());
                    context.startActivity(intent);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView tvName;
        private TextView tvPrice;
        private TextView tvUserName;

        public ViewHolder(View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.iv_favItem_image);
            tvName = itemView.findViewById(R.id.tv_favItem_name);
            tvPrice = itemView.findViewById(R.id.tv_favItem_price);
            tvUserName = itemView.findViewById(R.id.tv_favItem_userName);
        }
    }
}

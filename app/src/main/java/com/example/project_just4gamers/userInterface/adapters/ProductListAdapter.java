package com.example.project_just4gamers.userInterface.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_just4gamers.userInterface.activities.ProductDetailsActivity;
import com.example.project_just4gamers.R;
import com.example.project_just4gamers.models.Product;
import com.example.project_just4gamers.userInterface.fragments.ProductsFragment;
import com.example.project_just4gamers.utilities.Constants;
import com.example.project_just4gamers.utilities.glideLoader.GlideLoader;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private ArrayList<Product> products;
    private ProductsFragment fragment;

    public ProductListAdapter(@NonNull Context context,@NonNull ArrayList<Product> productss, ProductsFragment fragment) {
        this.context = context;
        this.products = productss;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.item_product_layout,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder itemList, int position) {
        Product model = products.get(position);

        if (itemList instanceof ProductListAdapter.ViewHolder){
            new GlideLoader(context).loadProductPicture(model.getImage(), ((ViewHolder) itemList).image);
            ((ViewHolder) itemList).tvName.setText(model.getTitle());
            ((ViewHolder) itemList).tvPrice.setText(context.getResources().getString(R.string.item_price_format,String.valueOf(model.getPrice())));
            ((ViewHolder) itemList).tvUserName.setText(model.getUser_name());

            ((ViewHolder) itemList).ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.deleteProduct(model.getProduct_id());
                }
            });

            itemList.itemView.setOnClickListener(getProductDetailsListener(model));
        }
    }

    private View.OnClickListener getProductDetailsListener(Product model) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra(Constants.getExtraProductId(), model.getProduct_id());
                intent.putExtra(Constants.getExtraProductOwnerId(), model.getUser_id());
                context.startActivity(intent);
            }
        };
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
        private ImageButton ibDelete;


        public ViewHolder(View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.iv_item_image);
            tvName = itemView.findViewById(R.id.tv_item_name);
            tvPrice = itemView.findViewById(R.id.tv_item_price);
            ibDelete = itemView.findViewById(R.id.ib_delete_product);
            tvUserName = itemView.findViewById(R.id.tv_product_userName);
        }
    }
}

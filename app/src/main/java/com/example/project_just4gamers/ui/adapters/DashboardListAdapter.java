package com.example.project_just4gamers.ui.adapters;

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
import com.example.project_just4gamers.ui.activities.ProductDetailsActivity;
import com.example.project_just4gamers.utils.Constants;
import com.example.project_just4gamers.utils.GlideLoader;

import java.util.ArrayList;

public class DashboardListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private ArrayList<Product> products;

    public DashboardListAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder( LayoutInflater.from(context).inflate(
                R.layout.item_dashboard_layout,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder itemList, int position) {
        //catch the product obj from each position of the list
        Product model = products.get(position);

        //set into the adapterView the contents.
        if (itemList instanceof DashboardListAdapter.ViewHolder) {
            new GlideLoader(context).loadProductPicture(model.getImage(), ((ViewHolder) itemList).image);
            ((ViewHolder) itemList).tvTitle.setText(model.getTitle());
            ((ViewHolder) itemList).tvPrice.setText(context.getString(R.string.item_price_format, model.getPrice()));
        }

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

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView tvTitle;
        private TextView tvPrice;

        public ViewHolder(View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.iv_dashboard_item_image);
            tvTitle = itemView.findViewById(R.id.tv_dashboard_item_title);
            tvPrice = itemView.findViewById(R.id.tv_dashboard_item_price);
        }
    }



}

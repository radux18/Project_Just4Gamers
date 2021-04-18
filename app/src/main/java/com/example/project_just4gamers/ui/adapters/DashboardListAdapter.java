package com.example.project_just4gamers.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.FirestoreManager;
import com.example.project_just4gamers.models.Product;
import com.example.project_just4gamers.ui.activities.ProductDetailsActivity;
import com.example.project_just4gamers.utils.Constants;
import com.example.project_just4gamers.utils.GlideLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DashboardListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private ArrayList<Product> products;
    private ArrayList<Product> filteredProducts;
    private Filter filter;

    public DashboardListAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    public Filter getFilter(){
        if (filter == null){
            filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();

                    if (constraint == null || constraint.length() ==0){
                        results.values = products;
                        results.count = products.size();
                    } else {
                        ArrayList<Product> beanList = new ArrayList<Product>();

                        for (Product product : products) {
                            if (product.getTitle().toUpperCase().contains(constraint.toString().toUpperCase())) {
                                beanList.add(product);
                            }
                        }
                        results.values = beanList;
                        results.count = beanList.size();
                    }
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    filteredProducts = (ArrayList<Product>) results.values;
                    products = filteredProducts;
                    notifyDataSetChanged();
                }
            };
        }
        return filter;
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
        Product model = products.get(position);

        if (itemList instanceof DashboardListAdapter.ViewHolder) {
            new GlideLoader(context).loadProductPicture(model.getImage(), ((ViewHolder) itemList).image);
            ((ViewHolder) itemList).tvTitle.setText(model.getTitle());
            ((ViewHolder) itemList).tvAge.setText(model.getAge());
            ((ViewHolder) itemList).tvPrice.setText(context.getString(R.string.item_price_format,String.valueOf(model.getPrice())));
        }

        itemList.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra(Constants.getExtraProductId(), model.getProduct_id());
                intent.putExtra(Constants.getExtraProductOwnerId(), model.getUser_id());
                System.out.println(model.getProduct_id() + "AICI1");
                System.out.println(model.getUser_id() + "AICI2");
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
        private TextView tvAge;

        public ViewHolder(View itemView){
            super(itemView);
            tvAge = itemView.findViewById(R.id.tv_dashboard_item_age);
            image = itemView.findViewById(R.id.iv_dashboard_item_image);
            tvTitle = itemView.findViewById(R.id.tv_dashboard_item_title);
            tvPrice = itemView.findViewById(R.id.tv_dashboard_item_price);
        }
    }



}

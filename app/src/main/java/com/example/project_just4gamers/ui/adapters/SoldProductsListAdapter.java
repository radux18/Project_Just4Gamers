package com.example.project_just4gamers.ui.adapters;

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

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.models.SoldProduct;
import com.example.project_just4gamers.ui.activities.SoldProductDetailsActivity;
import com.example.project_just4gamers.utils.Constants;
import com.example.project_just4gamers.utils.GlideLoader;

import java.util.ArrayList;

public class SoldProductsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private ArrayList<SoldProduct> soldProducts;

    public SoldProductsListAdapter(Context context, ArrayList<SoldProduct> soldProducts) {
        this.context = context;
        this.soldProducts = soldProducts;
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
        SoldProduct model = soldProducts.get(position);

        if (itemList instanceof SoldProductsListAdapter.ViewHolder){
            new GlideLoader(context).loadProductPicture(model.getImage(), ((ViewHolder) itemList).image);
            ((ViewHolder) itemList).tvName.setText(model.getTitle());
            ((ViewHolder) itemList).tvPrice.setText(context.getResources().getString(R.string.item_price_format,model.getPrice()));

            ((ViewHolder) itemList).ibDelete.setVisibility(View.GONE);

            itemList.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SoldProductDetailsActivity.class);
                    intent.putExtra(Constants.getExtraSoldProductDetails(), model);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return soldProducts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView tvName;
        private TextView tvPrice;
        private ImageButton ibDelete;


        public ViewHolder(View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.iv_item_image);
            tvName = itemView.findViewById(R.id.tv_item_name);
            tvPrice = itemView.findViewById(R.id.tv_item_price);
            ibDelete = itemView.findViewById(R.id.ib_delete_product);
        }
    }


}

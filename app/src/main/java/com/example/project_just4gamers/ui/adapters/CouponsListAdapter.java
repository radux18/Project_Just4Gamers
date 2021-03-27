package com.example.project_just4gamers.ui.adapters;

import android.app.Activity;
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
import com.example.project_just4gamers.models.Address;
import com.example.project_just4gamers.models.DiscountCoupon;
import com.example.project_just4gamers.ui.activities.CheckoutActivity;
import com.example.project_just4gamers.utils.Constants;


import java.util.ArrayList;

public class CouponsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private ArrayList<DiscountCoupon> discountCouponsList;
    private Activity activity;
    private Address address;

    public CouponsListAdapter(Context context, ArrayList<DiscountCoupon> discountCouponsList, Activity activity,Address address) {
        this.context = context;
        this.discountCouponsList = discountCouponsList;
        this.activity = activity;
        this.address = address;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CouponsListAdapter.ViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.item_coupon_layout,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder itemList, int position) {
            DiscountCoupon model = discountCouponsList.get(position);

            if (itemList instanceof CouponsListAdapter.ViewHolder){

                ((ViewHolder) itemList).tvCouponType.setText(model.getType());

                    itemList.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, CheckoutActivity.class);
                            intent.putExtra(Constants.getExtraCoupon(),model);
                            intent.putExtra(Constants.getExtraSelectedAddress(), address);
                            activity.startActivity(intent);
                        }
                    });


            }
    }

    @Override
    public int getItemCount() {
        return discountCouponsList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImage;
        private TextView tvCouponType;


        public ViewHolder(View itemView){
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_cart_item_image);
            tvCouponType= itemView.findViewById(R.id.tv_item_type);
        }
    }
}

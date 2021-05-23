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

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.models.Order;
import com.example.project_just4gamers.userInterface.activities.OrderDetailsActivity;
import com.example.project_just4gamers.utilities.Constants;
import com.example.project_just4gamers.utilities.glideLoader.GlideLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class OrdersListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<Order> orderList;
    private Context context;

    public OrdersListAdapter(ArrayList<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(
                R.layout.item_ordeproduct_layout,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder itemList, int position) {
        Order model = orderList.get(position);

        String dateFormat = "dd/MM/yyyy HH:mm";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        String date = formatter.format(model.getOrder_dateTime());

        if (itemList instanceof OrdersListAdapter.ViewHolder){
            new GlideLoader(context).loadProductPicture(model.getImage(), ((ViewHolder) itemList).image);
            ((ViewHolder) itemList).tvName.setText(model.getTitle());
            ((ViewHolder) itemList).tvTotalAmount.setText(model.getTotalAmount());
            ((ViewHolder) itemList).tvDate.setText(String.valueOf(date));
            ((ViewHolder) itemList).ibDelete.setVisibility(View.GONE);

            itemList.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OrderDetailsActivity.class);
                    intent.putExtra(Constants.getExtraMyOrderDetails(), model);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView image;
        private TextView tvName;
        private TextView tvDate;
        private TextView tvTotalAmount;
        private ImageButton ibDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.iv_item_image);
            tvName = itemView.findViewById(R.id.tv_item_name);
            tvDate = itemView.findViewById(R.id.tv_product_orderDate);
            tvTotalAmount = itemView.findViewById(R.id.tv_item_price);
            ibDelete = itemView.findViewById(R.id.ib_delete_product);
        }
    }


}

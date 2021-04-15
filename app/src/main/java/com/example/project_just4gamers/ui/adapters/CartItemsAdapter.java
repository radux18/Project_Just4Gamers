package com.example.project_just4gamers.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.FirestoreManager;
import com.example.project_just4gamers.models.CartItem;
import com.example.project_just4gamers.ui.activities.CartListActivity;
import com.example.project_just4gamers.utils.Constants;
import com.example.project_just4gamers.utils.GlideLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class CartItemsAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private ArrayList<CartItem> cartItems;
    private boolean updateCartItems;

    public CartItemsAdapter(Context context, ArrayList<CartItem> cartItems, boolean updateCartItems) {
        this.context = context;
        this.cartItems = cartItems;
        this.updateCartItems = updateCartItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartItemsAdapter.ViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.item_cart_layout,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder itemList, int position) {
        CartItem model = cartItems.get(position);

        if (itemList instanceof  CartItemsAdapter.ViewHolder){
            new GlideLoader(context).loadProductPicture(model.getImage(), ((CartItemsAdapter.ViewHolder) itemList).image);
            ((ViewHolder) itemList).tvTitle.setText(model.getTitle());
            ((ViewHolder) itemList).tvPrice.setText(context.getString(R.string.item_price_format, String.valueOf(model.getPrice())));
            ((ViewHolder) itemList).tvQuantity.setText(model.getCart_quantity());

            if (model.getCart_quantity().equals("0")){
                ((ViewHolder) itemList).ibRemove.setVisibility(View.GONE);
                ((ViewHolder) itemList).ibAdd.setVisibility(View.GONE);

                if (updateCartItems){
                    ((ViewHolder) itemList).ibDelete.setVisibility(View.VISIBLE);
                } else {
                    ((ViewHolder) itemList).ibDelete.setVisibility(View.GONE);
                }

                ((ViewHolder) itemList).tvQuantity.setText(
                        context.getResources().getString(R.string.lbl_out_of_stock));

                ((ViewHolder) itemList).tvQuantity.setTextColor(
                        ContextCompat.getColor(context, R.color.redError));
            } else {
                //if the user is in cart edit mode
                if (updateCartItems){
                    ((ViewHolder) itemList).ibRemove.setVisibility(View.VISIBLE);
                    ((ViewHolder) itemList).ibAdd.setVisibility(View.VISIBLE);
                    ((ViewHolder) itemList).ibDelete.setVisibility(View.VISIBLE);
                } else { //if the user is in the checkout mode
                    ((ViewHolder) itemList).ibRemove.setVisibility(View.GONE);
                    ((ViewHolder) itemList).ibAdd.setVisibility(View.GONE);
                    ((ViewHolder) itemList).ibDelete.setVisibility(View.GONE);
                }

                ((ViewHolder) itemList).tvQuantity.setTextColor(
                        ContextCompat.getColor(context, R.color.mediumGrey));
            }

            ((ViewHolder) itemList).ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context instanceof CartListActivity){
                        //context.showDialog
                    }
                    new FirestoreManager().removeItemFromCart(context, model.getId());
                }
            });

            ((ViewHolder) itemList).ibAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int cartQuantity= Integer.parseInt(model.getCart_quantity());
                    if (cartQuantity < Integer.parseInt(model.getStock_quantity())){
                        HashMap<String, Object> itemHashMap = new HashMap<>();

                        itemHashMap.put(Constants.getCartQuantity(),String.valueOf(cartQuantity + 1));

                        if (context instanceof CartListActivity){
                            //show progress dialog
                        }
                        new FirestoreManager().updateMyCart(context, model.getId(), itemHashMap);
                    } else {
                        Toast.makeText(context, context.getResources()
                                .getString(R.string.msg_for_available_stock, model.getStock_quantity()),Toast.LENGTH_SHORT).show();
                    }
                }
            });

            ((ViewHolder) itemList).ibRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (model.getCart_quantity().equals("1")){
                        new FirestoreManager().removeItemFromCart(context, model.getId());
                    } else {
                        int cartQuantity= Integer.parseInt(model.getCart_quantity());

                        HashMap<String, Object> itemHashMap = new HashMap<>();

                        itemHashMap.put(Constants.getCartQuantity(), String.valueOf((cartQuantity - 1)));

                        if (context instanceof CartListActivity){
                            //show progress dialog
                        }

                        new FirestoreManager().updateMyCart(context, model.getId(), itemHashMap);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView tvTitle;
        private TextView tvPrice;
        private TextView tvQuantity;
        private ImageButton ibDelete;
        private ImageButton ibRemove;
        private ImageButton ibAdd;


        public ViewHolder(View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.iv_cart_item_image);
            tvTitle = itemView.findViewById(R.id.tv_cart_item_title);
            tvPrice = itemView.findViewById(R.id.tv_cart_item_price);
            tvQuantity = itemView.findViewById(R.id.tv_cart_quantity);
            ibDelete = itemView.findViewById(R.id.ib_deleteCartItem);
            ibRemove = itemView.findViewById(R.id.ib_remove_cart_item);
            ibAdd = itemView.findViewById(R.id.ib_add_cart_item);

        }
    }
}

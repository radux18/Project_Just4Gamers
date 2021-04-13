package com.example.project_just4gamers.ui.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.FirestoreManager;
import com.example.project_just4gamers.models.CartItem;
import com.example.project_just4gamers.models.Product;
import com.example.project_just4gamers.ui.adapters.CartItemsAdapter;
import com.example.project_just4gamers.utils.Constants;

import java.util.ArrayList;

public class CartListActivity extends AppCompatActivity {
    private LinearLayout ll_checkout;
    private RecyclerView rv_CartList;
    private Toolbar tb_cart;
    private TextView tvNoItemFound;
    private TextView tvSubtotal;
    private TextView tvShippingCharge;
    private TextView tvTotalAmount;
    private Button btnCheckout;

    private ArrayList<Product> productList;
    private ArrayList<CartItem> cartListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        initComp();
        setupActionBar();

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartListActivity.this, AddressListActivity.class);
                intent.putExtra(Constants.getExtraSelectAddress(), true);
                startActivity(intent);
            }
        });
    }

    private void initComp() {
        tb_cart = findViewById(R.id.toolbar_cart_list);
        rv_CartList = findViewById(R.id.rv_cart_items_list);
        ll_checkout = findViewById(R.id.ll_checkout);
        tvNoItemFound = findViewById(R.id.tv_no_cart_item_found);
        tvSubtotal = findViewById(R.id.tv_sub_total);
        tvShippingCharge = findViewById(R.id.tv_shipping_charge);
        tvTotalAmount = findViewById(R.id.tv_total_amount);
        btnCheckout = findViewById(R.id.btn_checkout);
    }

    public void successCartItemsList(ArrayList<CartItem> cartList){
        //hide progress dialog

        for (Product product : productList){
            for (CartItem cartItem : cartList){
                if (product.getProduct_id().equals(cartItem.getProduct_id())){

                    cartItem.setStock_quantity(product.getStock_quantity());

                    if (Integer.parseInt(product.getStock_quantity()) == 0){
                        cartItem.setCart_quantity(product.getStock_quantity());
                    }
                }
            }
        }

        cartListItems = cartList;

        if (cartListItems.size() > 0){
            rv_CartList.setVisibility(View.VISIBLE);
            ll_checkout.setVisibility(View.VISIBLE);
            tvNoItemFound.setVisibility(View.GONE);

            rv_CartList.setLayoutManager(new LinearLayoutManager(CartListActivity.this));
            rv_CartList.setHasFixedSize(true);

            CartItemsAdapter adapter = new CartItemsAdapter(CartListActivity.this, cartListItems, true);
            rv_CartList.setAdapter(adapter);

            double subTotal = 0.0;

            for (CartItem item : cartListItems){
                int availableQuantity = Integer.parseInt(item.getStock_quantity());
                if (availableQuantity > 0){
                    int price = item.getPrice();
                    int quantity = Integer.parseInt(item.getCart_quantity());

                    subTotal += (price * quantity);
                }

            }
            tvSubtotal.setText(String.valueOf(subTotal));
            tvShippingCharge.setText("$10.0");

            if (subTotal > 0){
                ll_checkout.setVisibility(View.VISIBLE);

                double total = subTotal + 10;
                tvTotalAmount.setText(String.valueOf(total));
            } else {
                ll_checkout.setVisibility(View.GONE);
            }
        } else {
            rv_CartList.setVisibility(View.GONE);
            ll_checkout.setVisibility(View.GONE);
            tvNoItemFound.setVisibility(View.VISIBLE);
        }
    }

    public void successProductsListFromFirestore(ArrayList<Product> products){
        //hide progress dialog
        productList = products;

        getCartItemList();
    }

    private void getProductList(){
        //show progress dialog
        new FirestoreManager().getAllProductsList(CartListActivity.this);
    }


    private void getCartItemList(){
        new FirestoreManager().getCartList(CartListActivity.this);
    }

    public void itemUpdateSuccess(){
        //hide progress dialog
        getCartItemList();
    }

    public void itemRemovedSuccess(){
        //hide progress dialog
        Toast.makeText(CartListActivity.this, getResources().getString(R.string.msg_item_removed_successfully),
                Toast.LENGTH_SHORT).show();

        //update the list after the delete method
        getCartItemList();
    }

    private void setupActionBar(){
        setSupportActionBar(tb_cart);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp);
        }

        tb_cart.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
       // getCartItemList();
        getProductList();
    }
}
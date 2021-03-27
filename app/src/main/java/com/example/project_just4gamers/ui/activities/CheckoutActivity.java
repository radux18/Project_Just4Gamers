package com.example.project_just4gamers.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.example.project_just4gamers.models.Address;
import com.example.project_just4gamers.models.CartItem;
import com.example.project_just4gamers.models.DiscountCoupon;
import com.example.project_just4gamers.models.Order;
import com.example.project_just4gamers.models.Product;
import com.example.project_just4gamers.models.User;
import com.example.project_just4gamers.ui.adapters.CartItemsAdapter;
import com.example.project_just4gamers.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;

public class CheckoutActivity extends AppCompatActivity {
    private RecyclerView rvCheckout;
    private Toolbar tbCheckout;
    private Intent intent;
    private Address addressDetails = null;
    private Button btnPlaceOrder;
    private TextView tvAddress;
    private TextView tvAddressType;
    private TextView tvAdditionalNote;
    private TextView tvFullName;
    private TextView tvOtherDetails;
    private TextView tvMobileNumber;
    private TextView tvSubtotal;
    private TextView tvTotal;
    private TextView tvShipCharge;
    private LinearLayout ll_checkoutPlaceOrder;
    private LinearLayout ll_discountActive;
    private Button btnAddCoupon;

    private double subtotal = 0.0;
    private double totalAmount = 0.0;
    private Order orderDetails = null;

    private double discountProcent0 = 0.1;
    private double discountProcent1 = 0.15;
    private double discountProcent2 = 0.25;

    private ArrayList<Product> products;
    private ArrayList<CartItem> cartItems;

    private User currentUser = null;
    private int points;

    private DiscountCoupon discountCoupon = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        intent = getIntent();
        initComp();
        setupActionBar();

        if (intent.hasExtra(Constants.getExtraSelectedAddress())){
            addressDetails = intent.getParcelableExtra(Constants.getExtraSelectedAddress());
        }


        if (addressDetails != null){
            tvAddressType.setText(addressDetails.getType());
            tvAddress.setText(getString(R.string.address_details, addressDetails.getAddress(), addressDetails.getZipCode()));
            tvFullName.setText(addressDetails.getName());
            tvAdditionalNote.setText(addressDetails.getAdditionalNote());
            tvMobileNumber.setText(addressDetails.getMobileNumber());

            if (!addressDetails.getOtherDetails().isEmpty()){
                tvOtherDetails.setText(addressDetails.getOtherDetails());
            } else {
                tvOtherDetails.setVisibility(View.GONE);
            }

        }
        getProductList();
        getUserDetails();

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeAnOrder();
            }
        });

        btnAddCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivateCouponActivity.class);
                startActivityForResult(intent, 999);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 999){
                discountCoupon = data.getParcelableExtra(Constants.getExtraCoupon());
                System.out.println(discountCoupon);
            }
        }
    }

    public void getUserDetails() {
        new FirestoreManager().getCurrentUserDetails(CheckoutActivity.this);
    }

    public void getUserDetailsSuccess(User user){
        currentUser = user;
        points = currentUser.getPoints();
    }



    private void initComp() {
        tbCheckout = findViewById(R.id.toolbar_checkout);
        tvAdditionalNote = findViewById(R.id.tv_checkout_additionalNote);
        tvAddress = findViewById(R.id.tv_checkout_address);
        tvFullName = findViewById(R.id.tv_checkout_fullName);
        tvAddressType = findViewById(R.id.tv_checkout_addressType);
        tvOtherDetails = findViewById(R.id.tv_checkout_otherDetails);
        tvMobileNumber = findViewById(R.id.tv_checkout_mobileNumber);
        rvCheckout = findViewById(R.id.rv_cartList_items);
        tvSubtotal = findViewById(R.id.tv_checkout_subtotal);
        tvTotal = findViewById(R.id.tv_checkout_total_amount);
        tvShipCharge = findViewById(R.id.tv_checkout_shippingCharge);
        ll_checkoutPlaceOrder = findViewById(R.id.ll_checkout_place_order);
        btnPlaceOrder = findViewById(R.id.btn_place_order);
        ll_discountActive = findViewById(R.id.ll_discountActive);
        btnAddCoupon = findViewById(R.id.btn_activateCoupon);
    }

    private void getProductList(){
        //show progress dialog
        new FirestoreManager().getAllProductsList(CheckoutActivity.this);
    }

    public void successProductsListFromFirestore(ArrayList<Product> productList){
        products = productList;
        getCartItemsList();
    }

    private void getCartItemsList(){
        new FirestoreManager().getCartList(CheckoutActivity.this);
    }

    private void placeAnOrder(){
        //show progress dialog
        if (addressDetails != null){
             orderDetails = new Order(new FirestoreManager().getCurrentUserID(),
                    cartItems, addressDetails,
                    "OrderNo. " + System.currentTimeMillis(),
                    cartItems.get(0).getImage(),
                    String.valueOf(subtotal), String.valueOf(totalAmount),
                    "10.0",
                    System.currentTimeMillis());

            new FirestoreManager().placeOrder(CheckoutActivity.this, orderDetails);
        }
    }

    public void successCartItemsList(ArrayList<CartItem> cartList){
        //hide progress dialog
        //check-method if we have the right amount of stock.
        for (Product product : products){
            for (CartItem cartItem : cartList){
                if (product.getProduct_id().equals(cartItem.getProduct_id())){
                    cartItem.setStock_quantity(product.getStock_quantity());
                }
            }
        }
        cartItems = cartList;

        rvCheckout.setLayoutManager(new LinearLayoutManager(CheckoutActivity.this));
        rvCheckout.setHasFixedSize(true);

        CartItemsAdapter adapter = new CartItemsAdapter(CheckoutActivity.this, cartItems, false);
        rvCheckout.setAdapter(adapter);

        for (CartItem item : cartItems) {
            int availableQuantity =Integer.parseInt(item.getStock_quantity());
            if (availableQuantity > 0){
                double price = Double.parseDouble( item.getPrice());
                int quantity =Integer.parseInt(item.getCart_quantity());

                subtotal += (price * quantity);
            }
        }


        if (discountCoupon != null) {
            ll_discountActive.setVisibility(View.VISIBLE);
            double subtotal0 = 0;
            double total0;
            switch (discountCoupon.getType()) {
                case "0":
                        subtotal0 = subtotal0 * (1 - discountProcent0 * subtotal0);
                        subtotal = subtotal0;
                        total0 = subtotal0 + 10;
                        totalAmount = total0;
                        tvSubtotal.setText(String.valueOf(subtotal));
                        tvTotal.setText(String.valueOf(totalAmount));
                    break;
                case "1":
                    subtotal0 = subtotal0 * (1 - discountProcent1 * subtotal0);
                    subtotal = subtotal0;
                    total0 = subtotal0 + 10;
                    totalAmount = total0;
                    tvSubtotal.setText(String.valueOf(subtotal));
                    tvTotal.setText(String.valueOf(totalAmount));
                    break;
                case "2":
                    subtotal0 = subtotal0 * (1 - discountProcent2 * subtotal0);
                    subtotal = subtotal0;
                    total0 = subtotal0 + 10;
                    totalAmount = total0;
                    tvSubtotal.setText(String.valueOf(subtotal));
                    tvTotal.setText(String.valueOf(totalAmount));
                    break;
            }
        } else {
            ll_discountActive.setVisibility(View.GONE);
        }


     //   tvSubtotal.setText(String.valueOf(subtotal));
        tvShipCharge.setText("$10.0");

        if (subtotal > 0){
            ll_checkoutPlaceOrder.setVisibility(View.VISIBLE);

          //  totalAmount = subtotal + 10;
         //   tvTotal.setText(String.valueOf(totalAmount));
        } else {
            ll_checkoutPlaceOrder.setVisibility(View.GONE);
        }

    }

    private void setupActionBar(){
        setSupportActionBar(tbCheckout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp);
        }

        tbCheckout.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void allDetailsUpdatedSuccessfully(){
        //hide progress dialog
        Toast.makeText(getApplicationContext(), "Order was placed successfully!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void orderPlacedSuccess() {
        HashMap<String, Object> userHashMap = new HashMap<>();
        for (CartItem items : cartItems){
            switch (items.getAge()) {
                case "~5 years":
                    points += 10 * Integer.parseInt(items.getCart_quantity());

                    break;
                case "~1 years":
                    points += 5 * Integer.parseInt(items.getCart_quantity());

                    break;
                case "~(5–10) years":
                    points += 15 * Integer.parseInt(items.getCart_quantity());

                    break;
                case "10+ years":
                    points += 20 * Integer.parseInt(items.getCart_quantity());

                    break;
            }
            userHashMap.put(Constants.getPOINTS(), points);
            new FirestoreManager().setPointForCurrentUser(CheckoutActivity.this, userHashMap);
        }



        new FirestoreManager().updateAllDetails(CheckoutActivity.this, cartItems, orderDetails);
    }
}
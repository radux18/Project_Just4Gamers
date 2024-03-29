package com.example.project_just4gamers.userInterface.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.CloudFirestoreManager;
import com.example.project_just4gamers.models.Address;
import com.example.project_just4gamers.models.CartItem;
import com.example.project_just4gamers.models.DiscountCoupon;
import com.example.project_just4gamers.models.Order;
import com.example.project_just4gamers.models.Product;
import com.example.project_just4gamers.models.User;
import com.example.project_just4gamers.userInterface.adapters.CartItemsAdapter;
import com.example.project_just4gamers.utilities.Constants;

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

    private double subtotal = 0.0;
    private double totalAmount = 0.0;
    private Order orderDetails = null;

    private double discountProcent0 = 0.9;
    private double discountProcent1 = 0.85;
    private double discountProcent2 = 0.75;

    private ArrayList<Product> products;
    private ArrayList<CartItem> cartItems;
    private ArrayList<User> allUsers;

    private User currentUser = null;
    private int points;
    private int diffPoints;

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

        if (intent.hasExtra(Constants.getExtraCoupon())){
            discountCoupon = intent.getParcelableExtra(Constants.getExtraCoupon());
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
        getAllUsers();

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeAnOrder();
            }
        });
    }

    private void getAllUsers() {
        new CloudFirestoreManager().getAllUsers(CheckoutActivity.this);
    }


    public void getUserDetails() {
        new CloudFirestoreManager().getCurrentUserDetails(CheckoutActivity.this);
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

    }

    private void getProductList(){
        //show progress dialog
        new CloudFirestoreManager().getAllProductsList(CheckoutActivity.this);
    }

    public void successProductsListFromFirestore(ArrayList<Product> productList){
        products = productList;
        getCartItemsList();
    }

    private void getCartItemsList(){
        new CloudFirestoreManager().getCartList(CheckoutActivity.this);
    }

    public void successGetUsersFromFirestore(ArrayList<User> users) {
            allUsers = users;
    }

    private void placeAnOrder(){
        if (addressDetails != null){
             orderDetails = new Order(new CloudFirestoreManager().getCurrentUserID(),
                    cartItems, addressDetails,
                    "nr." + System.currentTimeMillis(),
                    cartItems.get(0).getImage(),
                    String.valueOf(subtotal + " LEI"), String.valueOf(totalAmount + " LEI"),
                    "10.0 LEI",
                    System.currentTimeMillis());

            new CloudFirestoreManager().placeOrder(CheckoutActivity.this, orderDetails);
        }
    }

    public void successCartItemsList(ArrayList<CartItem> cartList){
        //hide progress dialog
        for (Product product : products){
            for (CartItem cartItem : cartList){
                if (product.getProduct_id().equals(cartItem.getProduct_id())){
                    cartItem.setStock_quantity(product.getStock_quantity());
                }
            }
        }
        cartItems = cartList;
        successGetUsersFromFirestore(allUsers);

        rvCheckout.setLayoutManager(new LinearLayoutManager(CheckoutActivity.this));
        rvCheckout.setHasFixedSize(true);

        CartItemsAdapter adapter = new CartItemsAdapter(CheckoutActivity.this, cartItems, false);
        rvCheckout.setAdapter(adapter);

        for (CartItem item : cartItems) {
            int availableQuantity =Integer.parseInt(item.getStock_quantity());
            if (availableQuantity > 0){
                int price = item.getPrice();
                int quantity =Integer.parseInt(item.getCart_quantity());

                subtotal += (price * quantity);
            }
        }


        if (discountCoupon != null) {
            ll_discountActive.setVisibility(View.VISIBLE);

            switch (discountCoupon.getType()) {
                case "bronze":
                        subtotal = subtotal * discountProcent0;
                        totalAmount = subtotal + 10;
                        tvSubtotal.setText(String.valueOf(subtotal + " LEI"));
                        tvTotal.setText(String.valueOf(totalAmount + " LEI"));
                    break;
                case "silver":
                    subtotal = subtotal * discountProcent1;
                    totalAmount = subtotal + 10;
                    tvSubtotal.setText(String.valueOf(subtotal  + " LEI"));
                    tvTotal.setText(String.valueOf(totalAmount  + " LEI"));
                    break;
                case "gold":
                    subtotal = subtotal * discountProcent2;
                    totalAmount = subtotal + 10;
                    tvSubtotal.setText(String.valueOf(subtotal  + " LEI"));
                    tvTotal.setText(String.valueOf(totalAmount  + " LEI"));
                    break;
            }
        } else {
            ll_discountActive.setVisibility(View.GONE);
        }

        tvSubtotal.setText(String.valueOf(subtotal  + " LEI"));
        tvShipCharge.setText("10.0 LEI");

        if (subtotal > 0){
            ll_checkoutPlaceOrder.setVisibility(View.VISIBLE);

            totalAmount = subtotal + 10;
            tvTotal.setText(String.valueOf(totalAmount  + " LEI"));
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

    public void allDetailsUpdatedSuccessfully(int points){
        Toast.makeText(getApplicationContext(), "Comanda a fost plasata cu succes!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        intent.putExtra("extraPoints", points);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void orderPlacedSuccess() {
        HashMap<String, Object> userHashMap = new HashMap<>();
        for (CartItem items : cartItems){
            switch (items.getAge()) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    points += 5 * Integer.parseInt(items.getCart_quantity());
                    break;
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                    points += 10 * Integer.parseInt(items.getCart_quantity());
                    break;
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                    points += 15 * Integer.parseInt(items.getCart_quantity());
                    break;
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                    points += 30 * Integer.parseInt(items.getCart_quantity());
                    break;
            }
            userHashMap.put(Constants.getPOINTS(), points);
            new CloudFirestoreManager().setPointForCurrentUser(CheckoutActivity.this, userHashMap);
        }

        if (discountCoupon != null){
            new CloudFirestoreManager().removeCouponPerUse(discountCoupon);
        }

        HashMap<String, Object> productOwnerUserHashMap = new HashMap<>();
        for (CartItem item : cartItems){
            for (User user : allUsers){
                if (item.getProduct_ownerId().equals(user.getId())){
                    diffPoints = user.getPoints();
                    switch (item.getAge()) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                            diffPoints += 5 * Integer.parseInt(item.getCart_quantity());
                            break;
                        case 10:
                        case 11:
                        case 12:
                        case 13:
                        case 14:
                            diffPoints += 10 * Integer.parseInt(item.getCart_quantity());
                            break;
                        case 15:
                        case 16:
                        case 17:
                        case 18:
                        case 19:
                            diffPoints += 15 * Integer.parseInt(item.getCart_quantity());
                            break;
                        case 20:
                        case 21:
                        case 22:
                        case 23:
                        case 24:
                        case 25:
                            diffPoints += 30 * Integer.parseInt(item.getCart_quantity());
                            break;
                    }
                    productOwnerUserHashMap.put(Constants.getPOINTS(), diffPoints);

                    new CloudFirestoreManager().setPointsForDifferentUsers(productOwnerUserHashMap, user);
                }
            }
        }
            int wonPoints = points - currentUser.getPoints();
        new CloudFirestoreManager().updateAllDetails(CheckoutActivity.this, cartItems, orderDetails, wonPoints);
    }
}
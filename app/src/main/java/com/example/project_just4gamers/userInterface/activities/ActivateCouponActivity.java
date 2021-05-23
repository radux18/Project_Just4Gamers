package com.example.project_just4gamers.userInterface.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.CloudFirestoreManager;
import com.example.project_just4gamers.models.Address;
import com.example.project_just4gamers.models.DiscountCoupon;
import com.example.project_just4gamers.userInterface.adapters.CouponsListAdapter;
import com.example.project_just4gamers.utilities.Constants;

import java.util.ArrayList;

public class ActivateCouponActivity extends AppCompatActivity {

    private Toolbar tbDiscounts;
    private RecyclerView rvCouponsList;
    private TextView tvNoDiscountsFound;
    private ImageView ivForward;
    private Intent intent;
    private Address address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_coupon);
        intent = getIntent();
        initComp();
        setupActionBar();

        if (intent.hasExtra(Constants.getExtraSelectedAddress())){
            address = intent.getParcelableExtra(Constants.getExtraSelectedAddress());
        }

        getDiscountCouponListFromFirestore();

        ivForward.setOnClickListener(skipActivityEventListener());
    }

    private View.OnClickListener skipActivityEventListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(ActivateCouponActivity.this, CheckoutActivity.class);
                    intent.putExtra(Constants.getExtraSelectedAddress(), address);
                    startActivity(intent);
            }
        };
    }

    private void getDiscountCouponListFromFirestore() {
        new CloudFirestoreManager().getAllDiscountCoupons(ActivateCouponActivity.this);
    }

    public void successGetDiscountsFromFirestore(ArrayList<DiscountCoupon> discountCoupons){
        if (discountCoupons.size() > 0){
            rvCouponsList.setVisibility(View.VISIBLE);
            tvNoDiscountsFound.setVisibility(View.GONE);

            rvCouponsList.setLayoutManager(new LinearLayoutManager(ActivateCouponActivity.this));
            rvCouponsList.setHasFixedSize(true);

            CouponsListAdapter adapter = new CouponsListAdapter(getApplicationContext(), discountCoupons,ActivateCouponActivity.this, address);
            rvCouponsList.setAdapter(adapter);


        } else {
            rvCouponsList.setVisibility(View.GONE);
            tvNoDiscountsFound.setVisibility(View.VISIBLE);
        }

    }

    private void initComp() {
        tbDiscounts = findViewById(R.id.toolbar_discounts);
        rvCouponsList = findViewById(R.id.rv_coupons_list);
        tvNoDiscountsFound = findViewById(R.id.tv_no_coupon_found);
        ivForward = findViewById(R.id.iv_forward);
    }

    private void setupActionBar(){
        setSupportActionBar(tbDiscounts);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp);
        }

        tbDiscounts.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }
}
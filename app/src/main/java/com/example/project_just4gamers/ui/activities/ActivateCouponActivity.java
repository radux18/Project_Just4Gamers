package com.example.project_just4gamers.ui.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.FirestoreManager;
import com.example.project_just4gamers.models.DiscountCoupon;
import com.example.project_just4gamers.ui.adapters.AddressListAdapter;
import com.example.project_just4gamers.ui.adapters.CouponsListAdapter;

import java.util.ArrayList;

public class ActivateCouponActivity extends AppCompatActivity {

    private Toolbar tbDiscounts;
    private RecyclerView rvCouponsList;
    private TextView tvNoDiscountsFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_coupon);

        initComp();
        setupActionBar();

        getDiscountCouponListFromFirestore();
    }

    private void getDiscountCouponListFromFirestore() {
        new FirestoreManager().getAllDiscountCoupons(ActivateCouponActivity.this);
    }

    public void successGetDiscountsFromFirestore(ArrayList<DiscountCoupon> discountCoupons){
        if (discountCoupons.size() > 0){
            rvCouponsList.setVisibility(View.VISIBLE);
            tvNoDiscountsFound.setVisibility(View.GONE);


            rvCouponsList.setLayoutManager(new LinearLayoutManager(ActivateCouponActivity.this));
            rvCouponsList.setHasFixedSize(true);

            CouponsListAdapter adapter = new CouponsListAdapter(getApplicationContext(), discountCoupons,ActivateCouponActivity.this);
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
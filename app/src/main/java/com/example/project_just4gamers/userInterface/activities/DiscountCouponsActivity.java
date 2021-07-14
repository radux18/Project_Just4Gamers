package com.example.project_just4gamers.userInterface.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.CloudFirestoreManager;
import com.example.project_just4gamers.models.DiscountCoupon;
import com.example.project_just4gamers.models.User;
import com.example.project_just4gamers.utilities.Constants;

import java.util.HashMap;

public class DiscountCouponsActivity extends AppCompatActivity {

    private Toolbar toolbarDiscounts;
    private Button btnDiscount0;
    private Button btnDiscount1;
    private Button btnDiscount2;
    private TextView tvPoints;

    private User currentUser = null;
    private int points;
    private HashMap<String, Object> userHashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_coupons);
        initComp();

        setupActionBar();

        btnDiscount0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (points >= 500){
                    points -= 500;

                    userHashMap.put(Constants.getPOINTS(), points);
                    new CloudFirestoreManager().setPointForCurrentUser(DiscountCouponsActivity.this, userHashMap);

                    DiscountCoupon discountCoupon = new DiscountCoupon(new CloudFirestoreManager().getCurrentUserID(), "bronze");
                    new CloudFirestoreManager().setDiscountCouponForCurrentUser(discountCoupon, getApplicationContext());
                    tvPoints.setText(String.valueOf(points));
                } else {
                    Toast.makeText(getApplicationContext(), "Nu deții destule puncte in-game!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnDiscount1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (points >= 1000){
                    points -= 1000;

                    userHashMap.put(Constants.getPOINTS(), points);
                    new CloudFirestoreManager().setPointForCurrentUser(DiscountCouponsActivity.this, userHashMap);

                    DiscountCoupon discountCoupon = new DiscountCoupon(new CloudFirestoreManager().getCurrentUserID(), "silver");
                    new CloudFirestoreManager().setDiscountCouponForCurrentUser(discountCoupon, getApplicationContext());
                    tvPoints.setText(String.valueOf(points));
                } else {
                    Toast.makeText(getApplicationContext(), "Nu deții destule puncte in-game!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDiscount2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (points >= 2000){
                    points -= 2000;

                    userHashMap.put(Constants.getPOINTS(), points);
                    new CloudFirestoreManager().setPointForCurrentUser(DiscountCouponsActivity.this, userHashMap);

                    DiscountCoupon discountCoupon = new DiscountCoupon(new CloudFirestoreManager().getCurrentUserID(), "gold");
                    new CloudFirestoreManager().setDiscountCouponForCurrentUser(discountCoupon, getApplicationContext());
                    tvPoints.setText(String.valueOf(points));
                } else {
                    Toast.makeText(getApplicationContext(), "Nu deții destule puncte in-game!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurrentUserDetails();
    }

    private void getCurrentUserDetails() {
        new CloudFirestoreManager().getCurrentUserDetails(DiscountCouponsActivity.this);
    }

    public void successDetailsFromFirestore(User user){
        currentUser = user;
        points = currentUser.getPoints();
        tvPoints.setText(String.valueOf(points));
    }


    private void initComp() {
        toolbarDiscounts = findViewById(R.id.toolbar_discount_coupons);
        btnDiscount0 = findViewById(R.id.btn_discountType0);
        btnDiscount1 = findViewById(R.id.btn_discountType1);
        btnDiscount2 = findViewById(R.id.btn_discountType2);
        tvPoints = findViewById(R.id.tv_points);
    }

    private void setupActionBar(){
        setSupportActionBar(toolbarDiscounts);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp);
        }

        toolbarDiscounts.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
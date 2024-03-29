package com.example.project_just4gamers.userInterface.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.CloudFirestoreManager;
import com.example.project_just4gamers.models.Review;
import com.example.project_just4gamers.models.User;
import com.example.project_just4gamers.userInterface.adapters.ReviewListAdapter;
import com.example.project_just4gamers.utilities.Constants;
import com.example.project_just4gamers.utilities.glideLoader.GlideLoader;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Comparator;

public class ProductOwnerProfileActivity extends AppCompatActivity {

    private ImageView iv_userPhoto;
    private TextView tv_name;
    private TextView tv_gender;
    private TextView tv_mobile;
    private TextView tv_points;
    private TextView tv_email;
    private Toolbar tb_profile;
    private FloatingActionButton fabAddMsg;

    private Intent intent;
    private User productOwner;
    private String userId;

    private RatingBar rbReviews;
    private Button addReview;
    private LinearLayout llReviews;
    private RecyclerView rvReviewList;

    private float ratingUser;
    private int nrRatings;

    private SupportMapFragment supportMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_owner_profile);
        intent = getIntent();
        initComp();
        setupActionBar();

        if (intent.hasExtra(Constants.getExtraProfileDetailsv2()) ){
            userId = intent.getStringExtra(Constants.getExtraProfileDetailsv2());
            new CloudFirestoreManager().getUserFromId(ProductOwnerProfileActivity.this, userId);
        }

        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddReviewActivity.class);
                intent.putExtra(Constants.getExtraProfileDetailsVisitor(), userId);
                startActivity(intent);
            }
        });

        fabAddMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddMessageActivity.class);
                intent.putExtra(Constants.getExtraDetailsMessage(), productOwner);
                startActivity(intent);
            }
        });
    }

    private void initComp() {
        iv_userPhoto = findViewById(R.id.iv_userProfile_photo);
        tv_name = findViewById(R.id.tv_profile_name);
        tv_email = findViewById(R.id.tv_profile_email);
        tv_gender = findViewById(R.id.tv_profile_gender);
        tv_mobile = findViewById(R.id.tv_profile_mobile);
        tv_points = findViewById(R.id.tv_profile_points);
        tb_profile = findViewById(R.id.toolbar_profile);
        rbReviews = findViewById(R.id.rb_profile);
        addReview = findViewById(R.id.btn_addReview);
        llReviews = findViewById(R.id.ll_reviews);
        rvReviewList = findViewById(R.id.rv_reviews);
        fabAddMsg = findViewById(R.id.fab_add_message_profile);
    }


    public void successGetUser(User user) {
        productOwner = user;
        new CloudFirestoreManager().getUserVisitorDetails(ProductOwnerProfileActivity.this, productOwner.getId());

         new GlideLoader(ProductOwnerProfileActivity.this).loadUserPicture(productOwner.getImage(), iv_userPhoto);
        tv_points.setText(String.valueOf(productOwner.getPoints()));
        tv_name.setText(getString(R.string.tv_settings_name, productOwner.getFirstName(), productOwner.getLastName()));
        tv_mobile.setText(getString(R.string.mobile_format, productOwner.getMobile()));
        tv_gender.setText(productOwner.getGender());
        tv_email.setText(productOwner.getEmail());

        //update coordonates
        setupGPSUI(productOwner);
    }

    private void setupGPSUI(User productOwner) {
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_map);

        if (ActivityCompat.checkSelfPermission(ProductOwnerProfileActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull GoogleMap googleMap) {
                    LatLng latLng = new LatLng(productOwner.getLatitude(), productOwner.getLongitude());
                        MarkerOptions options = new MarkerOptions().position(latLng)
                                .title("Pozitia lui " + getString(R.string.tv_settings_name, productOwner.getFirstName(), productOwner.getLastName()));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                        googleMap.addMarker(options);
                }
            });
        } else {
            ActivityCompat.requestPermissions(ProductOwnerProfileActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void successGetReviews(ArrayList<Review> reviews) {
        if (reviews.size() > 0){
            rvReviewList.setVisibility(View.VISIBLE);

            rvReviewList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rvReviewList.setHasFixedSize(true);

            reviews.sort(new Comparator<Review>() {
                @Override
                public int compare(Review o1, Review o2) {
                    if (o1.getDate() == null || o2.getDate() == null)
                        return 0;
                    return o1.getDate().compareTo(o2.getDate());
                }
            });

            ReviewListAdapter adapter = new ReviewListAdapter(getApplicationContext(), reviews);
            rvReviewList.setAdapter(adapter);

            float rating;
            for (Review review : reviews){
                if (review.getUserProfile_id().equals(productOwner.getId())){
                    ratingUser += review.getScore();
                    nrRatings ++ ;
                }
            }
            rating = ratingUser / nrRatings;
            rbReviews.setRating(rating);
        } else {
            rvReviewList.setVisibility(View.GONE);
        }
    }

    private void setupActionBar(){
        setSupportActionBar(tb_profile);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp);
        }

        tb_profile.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


}
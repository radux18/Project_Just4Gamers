package com.example.project_just4gamers.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.FirestoreManager;
import com.example.project_just4gamers.models.Review;
import com.example.project_just4gamers.models.User;
import com.example.project_just4gamers.ui.adapters.ReviewListAdapter;
import com.example.project_just4gamers.utils.Constants;
import com.example.project_just4gamers.utils.GlideLoader;

import com.facebook.login.LoginManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


import java.util.ArrayList;

public class SettingsActivity extends ProgressDialogActivity {
    private User userDetails;
    private TextView tv_edit;
    private Toolbar tb_settings;
    private Button btnLogOut;
    private ImageView iv_userPhoto;
    private TextView tv_name;
    private TextView tv_gender;
    private TextView tv_mobile;
    private TextView tv_points;
    private TextView tv_email;
    private RatingBar rbRating;
    private FirestoreManager fStoreM = new FirestoreManager();
    private GlideLoader loader = new GlideLoader(SettingsActivity.this);
    private LinearLayout ll_address;
    private float ratingUser;
    private int nrRatings;
    private RecyclerView rvReviews;

    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initComp();
        setupActionBar();

        getUserDetails();

        btnLogOut.setOnClickListener(logoutListener());
        tv_edit.setOnClickListener(editProfileListener());
        ll_address.setOnClickListener(editAddressListener());

    }

    private void setupGPSUI(User userDetails) {
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_map);

        client = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        if (ActivityCompat.checkSelfPermission(SettingsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(@NonNull GoogleMap googleMap) {
                                LatLng latLng = new LatLng(userDetails.getLatitude(), userDetails.getLongitude());
                                MarkerOptions options = new MarkerOptions().position(latLng)
                                        .title("Pozitia mea");
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                                googleMap.addMarker(options);
                            }
                        });
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(SettingsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }


    private View.OnClickListener editAddressListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddressListActivity.class));

            }
        };
    }

    private View.OnClickListener editProfileListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                intent.putExtra(Constants.getExtraUserDetails(), userDetails);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener logoutListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        };
    }

    private void initComp() {
        tb_settings = findViewById(R.id.toolbar_settings_activity);
        iv_userPhoto = findViewById(R.id.iv_user_photo);
        tv_name = findViewById(R.id.tv_settings_name);
        tv_email = findViewById(R.id.tv_settings_email);
        tv_gender = findViewById(R.id.tv_settings_gender);
        tv_mobile = findViewById(R.id.tv_settings_mobile);
        btnLogOut = findViewById(R.id.btn_settings_logout);
        tv_edit = findViewById(R.id.tv_edit);
        ll_address = findViewById(R.id.ll_address);
        tv_points = findViewById(R.id.tv_settings_points);
        rbRating = findViewById(R.id.rb_settings);
        rvReviews = findViewById(R.id.rv_ratings);
    }

    private void setupActionBar() {
        setSupportActionBar(tb_settings);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp);
        }

        tb_settings.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getUserDetails() {
        fStoreM.getUserDetails(SettingsActivity.this);
    }

    public void userDetailsSuccess(User user) {
        userDetails = user;
        loader.loadUserPicture(user.getImage(), iv_userPhoto);
        tv_points.setText(String.valueOf(user.getPoints()));
        tv_name.setText(getString(R.string.tv_settings_name, user.getFirstName(), user.getLastName()));
        tv_mobile.setText(getString(R.string.mobile_format, user.getMobile()));
        tv_gender.setText(user.getGender());
        tv_email.setText(user.getEmail());

        setupGPSUI(userDetails);

        new FirestoreManager().getReviewsForUser(SettingsActivity.this, userDetails.getId());
    }


    public void successGetReviews(ArrayList<Review> reviews) {
        float rating;
        for (Review review : reviews) {
            ratingUser += review.getScore();
            nrRatings++;
        }

        rating = ratingUser / nrRatings;
        rbRating.setRating(rating);

        if (reviews.size() > 0) {
            rvReviews.setVisibility(View.VISIBLE);

            rvReviews.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rvReviews.setHasFixedSize(true);

            ReviewListAdapter adapter = new ReviewListAdapter(getApplicationContext(), reviews);
            rvReviews.setAdapter(adapter);
        } else {
            rvReviews.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Task<Location> task = client.getLastLocation();
                    task.addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null){
                                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(@NonNull GoogleMap googleMap) {
                                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                        MarkerOptions options = new MarkerOptions().position(latLng)
                                                .title("Pozitia mea");
                                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                                        googleMap.addMarker(options);
                                    }
                                });
                            }
                        }
                    });
                }
            }
        }
    }
}
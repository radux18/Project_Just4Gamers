package com.example.project_just4gamers.userInterface.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.CloudFirestoreManager;
import com.example.project_just4gamers.models.Review;
import com.example.project_just4gamers.models.User;
import com.example.project_just4gamers.utilities.Constants;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddReviewActivity extends AppCompatActivity {

    private Toolbar tbReview;
    private TextInputEditText tietTitle;
    private TextInputEditText tietDescription;
    private RatingBar rbReviewScore;
    private Button btnAddReview;

    private Intent intent;
    private ArrayList<Review> reviews = new ArrayList<>();
    private String userId;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        intent = getIntent();
        initComp();
        setupActionBar();

        if (intent.hasExtra(Constants.getExtraProfileDetailsVisitor())){
            userId = intent.getStringExtra(Constants.getExtraProfileDetailsVisitor());
        }

        new CloudFirestoreManager().getCurrentUserDetails(AddReviewActivity.this);

        btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReviewToFirestore(currentUser);
            }
        });
    }

    private void initComp() {
        tbReview = findViewById(R.id.toolbar_addReview);
        tietDescription = findViewById(R.id.tiet_addReview_description);
        tietTitle = findViewById(R.id.tiet_addReview_title);
        rbReviewScore = findViewById(R.id.rb_reviewScore);
        btnAddReview = findViewById(R.id.btn_sendReview);
    }

    private void saveReviewToFirestore(User currentUser) {
        String title = tietTitle.getText().toString();
        String description = tietDescription.getText().toString();
        float score = rbReviewScore.getRating();

        String dateFormat = "dd/MM/yyyy HH:mm";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        String date = formatter.format(calendar.getTime());

        Review review = new Review(userId, currentUser, description, title, score, date);
        new CloudFirestoreManager().addReviewToFirestore(AddReviewActivity.this, review);

    }

    public void successGetUser(User user){
        currentUser = user;
    }

    private void setupActionBar(){
        setSupportActionBar(tbReview);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp);
        }

        tbReview.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void successAddReview() {
        Toast.makeText(getApplicationContext(), "Ai adăugat cu succes o recenzie!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
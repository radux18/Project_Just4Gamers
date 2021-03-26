package com.example.project_just4gamers.ui.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.FirestoreManager;
import com.example.project_just4gamers.models.User;
import com.example.project_just4gamers.utils.Constants;
import com.example.project_just4gamers.utils.GlideLoader;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    private User userDetails;
    private TextView tv_edit;
    private Toolbar tb_settings;
    private Button btnLogOut;
    private ImageView iv_userPhoto;
    private TextView tv_name;
    private TextView tv_gender;
    private TextView tv_mobile;
    private TextView tv_email;
    private FirestoreManager fStoreM = new FirestoreManager();
    private GlideLoader loader = new GlideLoader(SettingsActivity.this);
    private LinearLayout ll_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initComp();
        setupActionBar();

        btnLogOut.setOnClickListener(logoutListener());
        tv_edit.setOnClickListener(editProfileListener());
        ll_address.setOnClickListener(editAddressListener());
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
    }

    private void setupActionBar(){
        setSupportActionBar(tb_settings);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
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

    private void getUserDetails(){
        fStoreM.getUserDetails(SettingsActivity.this);
    }


    public void userDetailsSuccess(User user){
        userDetails = user;
        loader.loadUserPicture(user.getImage(),iv_userPhoto);

        tv_name.setText(getString(R.string.tv_settings_name, user.getFirstName(),user.getLastName()));
        tv_mobile.setText(getString(R.string.mobile_format, user.getMobile()));
        tv_gender.setText(user.getGender());
        tv_email.setText(user.getEmail());
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserDetails();
    }
}
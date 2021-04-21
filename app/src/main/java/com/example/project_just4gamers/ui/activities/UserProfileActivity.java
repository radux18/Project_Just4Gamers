package com.example.project_just4gamers.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.project_just4gamers.firestore.FirestoreManager;
import com.example.project_just4gamers.R;
import com.example.project_just4gamers.models.User;
import com.example.project_just4gamers.utils.Constants;
import com.example.project_just4gamers.utils.GlideLoader;
import com.google.android.material.textfield.TextInputEditText;


import java.util.HashMap;

public class UserProfileActivity extends AppCompatActivity {

    private GlideLoader photoLoader = new GlideLoader(UserProfileActivity.this);
    private FirestoreManager fManager = new FirestoreManager();
    private User userDetails = new User();
    private Uri selectedImageFileUri = null;
    private String userProfileURL = "";
    private Intent intent;
    private TextInputEditText tiet_firstName;
    private TextInputEditText tiet_lastName;
    private TextInputEditText tiet_email;
    private TextInputEditText tiet_mobile;
    private ImageView iv_userProfilePhoto;
    private TextView tvTitle;
    private Button btn_submit;
    private Toolbar tb_settings;
    private RadioButton rb_male;
    private RadioButton rb_female;
    private String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        intent = getIntent();
        initComponents();
        setupActionBar();

        if (intent.hasExtra(Constants.getExtraUserDetails())){
            userDetails = intent.getParcelableExtra(Constants.getExtraUserDetails());
        }

        setUserDetailsMethod();

        iv_userProfilePhoto.setOnClickListener(setProfilePhotoListener());

        btn_submit.setOnClickListener(submitProfileListener());
    }

    private View.OnClickListener submitProfileListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    //show progress dialog
                    if (selectedImageFileUri != null){
                        fManager.uploadImageToCloudStorage(UserProfileActivity.this,selectedImageFileUri, Constants.getUserProfileImage());
                    } else {
                       updateUserProfileDetails();
                    }
                }
            }
        };
    }

    private boolean validate() {
        String mobile = tiet_mobile.getText().toString().trim();
        if (TextUtils.isEmpty(mobile) || mobile.length() != 10){
            tiet_mobile.setError("Mobile number must have 10 characters.");
            return false;
        }
        return true;
    }

    private View.OnClickListener setProfilePhotoListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(UserProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    new Constants().showImageChooser(UserProfileActivity.this);
                } else {
                   //Requests permissions to be granted.
                    ActivityCompat.requestPermissions(UserProfileActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}
                    ,Constants.getReadStoragePermissionCode());
                }
            }
        };
    }

    private void initComponents() {
        tiet_firstName = findViewById(R.id.tiet_profile_firstName);
        tiet_lastName = findViewById(R.id.tiet_profile_lastName);
        tiet_email = findViewById(R.id.tiet_profile_email);
        iv_userProfilePhoto = findViewById(R.id.iv_profile_photo);
        tiet_mobile = findViewById(R.id.tiet_profile_mobileNr);
        rb_male = findViewById(R.id.rb_male);
        rb_female = findViewById(R.id.rb_female);
        btn_submit = findViewById(R.id.btn_profile_submit);
        tb_settings = findViewById(R.id.tb_profile);
        tvTitle = findViewById(R.id.tv_title_address);
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

    private void setUserDetailsMethod() {
        tiet_firstName.setText(userDetails.getFirstName());
        tiet_lastName.setText(userDetails.getLastName());
        tiet_email.setEnabled(false);
        tiet_email.setText(userDetails.getEmail());

        if (userDetails.getProfileCompleted() == 0){
            tvTitle.setText(getString(R.string.tv_complete_profile));
            tiet_firstName.setEnabled(false);
            tiet_firstName.setTextColor(Color.GRAY);

            tiet_lastName.setEnabled(false);
            tiet_lastName.setTextColor(Color.GRAY);

            tiet_email.setTextColor(Color.GRAY);
        } else {
            //setupActionBar();
            tvTitle.setText(getString(R.string.tv_edit_profile));
            photoLoader.loadUserPicture(userDetails.getImage(),iv_userProfilePhoto);

            if (userDetails.getMobile() != 0L){
                tiet_mobile.setText(String.valueOf("0" + userDetails.getMobile()));
            }

            if (userDetails.getGender().equals(Constants.getMALE())){
                rb_male.setChecked(true);
            } else {
                rb_female.setChecked(true);
            }
        }
    }

    private void updateUserProfileDetails(){
        String mobile = tiet_mobile.getText().toString().trim();
        String firstName = tiet_firstName.getText().toString().trim();
        String lastName = tiet_lastName.getText().toString().trim();
        HashMap<String, Object> userHashMap = new HashMap<>();

        if (!firstName.equals(userDetails.getFirstName())){
            userHashMap.put(Constants.getFirstName(),firstName);
        }

        if (rb_male.isChecked()){
            gender = Constants.getMALE();
        } else {
            gender = Constants.getFEMALE();
        }

        if (!userProfileURL.isEmpty()){
            userHashMap.put(Constants.getIMAGE(),userProfileURL);
        }

        if (!lastName.equals(userDetails.getLastName())){
            userHashMap.put(Constants.getLastName(),lastName);
        }

        if (!gender.isEmpty() && !gender.equals(userDetails.getGender())){
            userHashMap.put(Constants.getGENDER(),gender);
        }

        if (!mobile.isEmpty() && !mobile.equals(String.valueOf(userDetails.getMobile()))){
            userHashMap.put(Constants.getMOBILE(),Long.parseLong(mobile));
        }
        userHashMap.put(Constants.getCompleteProfile(), 1);

        fManager.updateUserProfileData(UserProfileActivity.this, userHashMap);
    }

    public void userProfileUpdateSuccess(){
        Toast.makeText(getApplicationContext(),"Profile update succeed!",Toast.LENGTH_LONG).show();
        startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Constants.getReadStoragePermissionCode()){
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                new Constants().showImageChooser(this);
            }
        } else {
            Toast.makeText(getApplicationContext(),"Oops, you just denied the permission for storage. You can also allow it from settings.",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == Constants.getPickImageRequestCode()){
                if (data != null){
                    try {
                         selectedImageFileUri = data.getData();
                        photoLoader.loadUserPicture(selectedImageFileUri,iv_userProfilePhoto);
                    } catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Image selection failed",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }
    }

    public void imageUploadSuccess(String imageURL){
        userProfileURL = imageURL;
        updateUserProfileDetails();
    }
}
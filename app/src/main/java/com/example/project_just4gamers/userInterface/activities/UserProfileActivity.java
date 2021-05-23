package com.example.project_just4gamers.userInterface.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
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
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.project_just4gamers.firestore.CloudFirestoreManager;
import com.example.project_just4gamers.R;
import com.example.project_just4gamers.models.User;
import com.example.project_just4gamers.utilities.Constants;
import com.example.project_just4gamers.utilities.glideLoader.GlideLoader;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;


import java.util.HashMap;

public class UserProfileActivity extends ProgressDialogActivity {

    private GlideLoader photoLoader = new GlideLoader(UserProfileActivity.this);
    private CloudFirestoreManager fManager = new CloudFirestoreManager();
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

    private FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        intent = getIntent();
        initComponents();
        setupActionBar();

        if (intent.hasExtra(Constants.getExtraUserDetails())) {
            userDetails = intent.getParcelableExtra(Constants.getExtraUserDetails());
        }

        setUserDetailsMethod();

        iv_userProfilePhoto.setOnClickListener(setProfilePhotoListener());
        client = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        btn_submit.setOnClickListener(submitProfileListener());
    }

    private View.OnClickListener submitProfileListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    //show progress dialog
                    if (selectedImageFileUri != null) {
                        fManager.uploadImageToCloudStorage(UserProfileActivity.this, selectedImageFileUri, Constants.getUserProfileImage());
                    } else {
                        updateUserProfileDetails();
                    }
                    //update the gps location for user
                    if (ActivityCompat.checkSelfPermission(UserProfileActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(UserProfileActivity.this,
                                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ){
                        getCoordonates();
                    } else {
                        ActivityCompat.requestPermissions(UserProfileActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                    }
                }
            }
        };
    }

    @SuppressLint("MissingPermission")
    private void getCoordonates() {
        HashMap<String, Object> hashMap = new HashMap<>();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        double latitude = location.getLatitude();
                        hashMap.put(Constants.getLATITUDE(), latitude);
                        double longitude = location.getLongitude();
                        hashMap.put(Constants.getLONGITUDE(), longitude);

                        new CloudFirestoreManager().setCoordonatesForUser(hashMap);
                    } else {
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(1000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();

                                double latitude = location1.getLatitude();
                                hashMap.put(Constants.getLATITUDE(), latitude);
                                double longitude = location1.getLongitude();
                                hashMap.put(Constants.getLONGITUDE(), longitude);
                                new CloudFirestoreManager().setCoordonatesForUser(hashMap);
                            }
                        };
                            client.requestLocationUpdates(locationRequest,
                                    locationCallback, Looper.myLooper());
                        }
                    }
            });
        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
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

        if (userDetails.getImage() != null){
            photoLoader.loadUserPicture(userDetails.getImage(),iv_userProfilePhoto);
        }

        if (userDetails.getProfileCompleted() == 0){
            tvTitle.setText(getString(R.string.tv_complete_profile));
            tiet_firstName.setEnabled(false);
            tiet_firstName.setTextColor(Color.DKGRAY);
            tiet_mobile.setTextColor(Color.BLACK);

            tiet_lastName.setEnabled(false);
            tiet_lastName.setTextColor(Color.DKGRAY);
            tiet_mobile.setHintTextColor(Color.DKGRAY);
            tiet_email.setTextColor(Color.DKGRAY);

        } else {
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
        Toast.makeText(getApplicationContext(),"Success!",Toast.LENGTH_LONG).show();
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
        } else if (requestCode == 100 && grantResults.length > 0 && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)){
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
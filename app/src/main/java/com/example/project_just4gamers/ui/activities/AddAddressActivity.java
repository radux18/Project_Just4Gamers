package com.example.project_just4gamers.ui.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.FirestoreManager;
import com.example.project_just4gamers.models.Address;
import com.example.project_just4gamers.utils.Constants;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddAddressActivity extends AppCompatActivity {
    private Toolbar tbAddAddress;
    private TextInputEditText tietFullName;
    private TextInputEditText tietPhoneNumber;
    private TextInputEditText tietZipCode;
    private TextInputEditText tietAddress;
    private TextInputEditText tietAdditionalNote;
    private TextInputEditText tietOtherDetails;
    private TextView tvTitle;
    private RadioButton rb_Home;
    private RadioButton rb_Other;
    private RadioButton rb_Office;
    private Button btnSubmit;
    private String addressType;
    private RadioGroup rg_type;
    private TextInputLayout tilOtherDetails;
    private Address addressDetails = null;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        intent = getIntent();

        initComp();
        setupActionBar();

        if (intent.hasExtra(Constants.getExtraAddressDetails())){
            addressDetails = intent.getParcelableExtra(Constants.getExtraAddressDetails());
        }

        if (addressDetails != null){
            if (!addressDetails.getId().isEmpty()){
                tvTitle.setText(getString(R.string.title_edit_address));
                btnSubmit.setText(getString(R.string.btn_update));

                tietFullName.setText(addressDetails.getName());
                tietPhoneNumber.setText(addressDetails.getMobileNumber());
                tietAddress.setText(addressDetails.getAddress());
                tietZipCode.setText(addressDetails.getZipCode());
                tietAdditionalNote.setText(addressDetails.getAdditionalNote());

                if (addressDetails.getType().equals(Constants.getHOME())){
                    rb_Home.setChecked(true);
                } else if (addressDetails.getType().equals(Constants.getOFFICE())){
                    rb_Office.setChecked(true);
                } else {
                    rb_Other.setChecked(true);
                    tilOtherDetails.setVisibility(View.VISIBLE);

                    if (addressDetails.getOtherDetails() != null)
                    tietOtherDetails.setText(addressDetails.getOtherDetails());
                }
            }
        }

        rg_type.clearCheck();
        rg_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1){
                   if (checkedId == R.id.rb_other){
                       tilOtherDetails.setVisibility(View.VISIBLE);
                   } else {
                       tilOtherDetails.setVisibility(View.GONE);
                   }
                }

            }
        });
        btnSubmit.setOnClickListener(createAddressListener());
    }

    private View.OnClickListener createAddressListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAddressToFirestore();
            }
        };
    }

    private void initComp() {
        tbAddAddress = findViewById(R.id.toolbar_add_address);
        tietFullName = findViewById(R.id.tiet_addAddress_fullName);
        tietPhoneNumber = findViewById(R.id.tiet_addAddress_phoneNumber);
        tietZipCode = findViewById(R.id.tiet_addAddress_zipCode);
        tietAddress = findViewById(R.id.tiet_addAddress_address);
        tietAdditionalNote = findViewById(R.id.tiet_addAddress_additionalNote);
        tietOtherDetails = findViewById(R.id.tiet_addAddress_otherDetails);
        tilOtherDetails = findViewById(R.id.til_addAddress_otherDetails);
        tvTitle = findViewById(R.id.tv_title_address);
        rb_Home = findViewById(R.id.rb_home);
        rb_Office = findViewById(R.id.rb_office);
        rb_Other = findViewById(R.id.rb_other);
        rg_type = findViewById(R.id.rg_type);
        btnSubmit = findViewById(R.id.btn_addAddress_submit);
    }

    private void saveAddressToFirestore(){
        String fullName = tietFullName.getText().toString();
        String phoneNumber = tietPhoneNumber.getText().toString().trim();
        String zipCode = tietZipCode.getText().toString().trim();
        String address = tietAddress.getText().toString();
        String additionalNote = tietAdditionalNote.getText().toString();
        String otherDetails = tietOtherDetails.getText().toString();

        if (validate()){
            //show progress dialog
               if (rb_Home.isChecked()){
                   addressType = Constants.getHOME();
               }
               if (rb_Office.isChecked()){
                   addressType = Constants.getOFFICE();
               }

               if (rb_Other.isChecked()){
                   addressType = Constants.getOTHER();

               }

            Address addressModel = new Address(new FirestoreManager().getCurrentUserID(),
                    fullName, phoneNumber, address, zipCode
                    , additionalNote, addressType, otherDetails);

               if (addressDetails != null && !addressDetails.getId().isEmpty()){
                   //update
                   new FirestoreManager().updateAddress(AddAddressActivity.this, addressModel, addressDetails.getId());
               } else {
                   //add
                   new FirestoreManager().addAddress(AddAddressActivity.this, addressModel);

               }
        }
    }

    private boolean validate() {
        String fullName = tietFullName.getText().toString();
        String phoneNumber = tietPhoneNumber.getText().toString().trim();
        String zipCode = tietZipCode.getText().toString().trim();
        String address = tietAddress.getText().toString();

        if (TextUtils.isEmpty(fullName)) {
            tietFullName.setError("Please enter full name.");
            return false;
        }

        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() != 10) {
            tietPhoneNumber.setError("Phone number must have 10 characters.");
            return false;
        }

        if (TextUtils.isEmpty(zipCode)){
            tietZipCode.setError("Please enter zip code.");
            return false;
        }

        if (TextUtils.isEmpty(address)){
            tietZipCode.setError("Please enter address.");
            return false;
        }
        return true;
    }

    private void setupActionBar(){
        setSupportActionBar(tbAddAddress);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp);
        }

        tbAddAddress.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void successAddAddress() {
        //hide progress dialog
        Toast.makeText(getApplicationContext(), "Address has been added successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void addUpdateAddressSuccess() {
        //hide progress dialog
        String notifySuccessMessage;
        if (addressDetails != null && !addressDetails.getId().isEmpty()){
            notifySuccessMessage = getString(R.string.address_update_success);
        } else {
            notifySuccessMessage = getString(R.string.err_update_success);
        }
        Toast.makeText(AddAddressActivity.this, notifySuccessMessage, Toast.LENGTH_SHORT).show();

        setResult(RESULT_OK);
        finish();
    }
}
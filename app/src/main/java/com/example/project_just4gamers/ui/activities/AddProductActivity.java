package com.example.project_just4gamers.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.FirestoreManager;
import com.example.project_just4gamers.models.Product;
import com.example.project_just4gamers.utils.Constants;
import com.example.project_just4gamers.utils.GlideLoader;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;

public class AddProductActivity extends AppCompatActivity {

    private Uri selectedImageFileURI = null;
    private String productImageURL = "";

    private  GlideLoader loader = new GlideLoader(AddProductActivity.this);
    private Toolbar tb_product;
    private Button btnSubmit;
    private TextInputEditText tiet_title;
    private TextInputEditText tiet_price;
    private TextInputEditText tiet_description;
    private TextInputEditText tiet_quantity;
    private ImageView iv_updateProduct;
    private ImageView iv_imageProduct;
    private RadioButton rbNew;
    private RadioButton rbUsed;
    private RadioGroup rgType;
    private Spinner spnProductAge;
    private String type;
    private LinearLayout llProductAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        initComponents();
        setupActionBar();

        iv_updateProduct.setOnClickListener(updatePhotoListener());
        btnSubmit.setOnClickListener(setProductListener());

        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1){
                    if (checkedId == R.id.rb_used){
                        llProductAge.setVisibility(View.VISIBLE);
                    } else if (checkedId == R.id.rb_new){
                        llProductAge.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private View.OnClickListener setProductListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    uploadProductImage();
                }
            }
        };
    }

    private boolean validate() {
        String title = tiet_title.getText().toString().trim();
        String price = tiet_price.getText().toString().trim();
        String quantity = tiet_quantity.getText().toString().trim();
        String description = tiet_description.getText().toString();

        if (TextUtils.isEmpty(title)) {
            tiet_title.setError("Title is required.");
            return false;
        }
        if (TextUtils.isEmpty(price) || Float.parseFloat(price) <= 0) {
            tiet_price.setError("Price is required.");
            return false;
        }

        if (TextUtils.isEmpty(quantity) || Integer.parseInt(quantity) <= 0 ) {
            tiet_quantity.setError("Quantity is required.");
            return false;
        }
        if (TextUtils.isEmpty(description)) {
            tiet_description.setError("Description is required.");
            return false;
        }
        return true;
    }

    private View.OnClickListener updatePhotoListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AddProductActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    new Constants().showImageChooser(AddProductActivity.this);
                } else {
                    //Requests permissions to be granted.
                    ActivityCompat.requestPermissions(AddProductActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}
                            ,Constants.getReadStoragePermissionCode());
                }
            }
        };
    }

    private void initComponents() {
        tb_product = findViewById(R.id.toolbar_add_product_activity);
        btnSubmit = findViewById(R.id.btn_add_product_submit);
        tiet_title = findViewById(R.id.tiet_add_product_title);
        tiet_price = findViewById(R.id.tiet_add_product_price);
        tiet_description = findViewById(R.id.tiet_add_product_description);
        tiet_quantity = findViewById(R.id.tiet_add_product_quantity);
        iv_updateProduct = findViewById(R.id.iv_add_update_product);
        iv_imageProduct = findViewById(R.id.iv_product_image);
        spnProductAge = findViewById(R.id.spn_add_product_age);
        rbNew = findViewById(R.id.rb_new);
        rbUsed = findViewById(R.id.rb_used);
        llProductAge = findViewById(R.id.ll_product_age);
        rgType = findViewById(R.id.rg_add_product);
        addSpinnerAdapter();
    }

    private void setupActionBar(){
        setSupportActionBar(tb_product);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp);
        }

        tb_product.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void uploadProductImage(){
        //show progress dialog
     new FirestoreManager().uploadImageToCloudStorage(AddProductActivity.this, selectedImageFileURI, Constants.getProductImage());
    }

    public void productUploadSuccess(){
        //hide progress dialog
        Toast.makeText(getApplicationContext(), "Product uploaded success!", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void imageUploadSuccess(String imageURL){
        //hide the progress dialog
        productImageURL = imageURL;
        uploadProductDetails();
    }

    private void uploadProductDetails(){
        String title = tiet_title.getText().toString().trim();
        int price = Integer.parseInt(tiet_price.getText().toString().trim());
        String description = tiet_description.getText().toString().trim();
        String quantity = tiet_quantity.getText().toString().trim();
        String age = spnProductAge.getSelectedItem().toString();

        if (rbUsed.isChecked()){
            type = Constants.getUSED();

        } else {
            type = Constants.getNEW();
        }
        String userName = this.getSharedPreferences(Constants.getPREFS(), Context.MODE_PRIVATE).getString(Constants.getUSERNAME(),"");

            if (type.equals(Constants.getNEW())){
                    Product product = new Product(new FirestoreManager().getCurrentUserID(), userName, "",
                            title,price,description,quantity, type, "N/A", productImageURL);
                    new FirestoreManager().uploadProductDetails(AddProductActivity.this, product);
            } else if (type.equals(Constants.getUSED())){
                    Product product = new Product(new FirestoreManager().getCurrentUserID(),userName, "",
                            title,price,description,quantity, type, age, productImageURL);
                    new FirestoreManager().uploadProductDetails(AddProductActivity.this, product);
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Constants.getReadStoragePermissionCode()){
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                new Constants().showImageChooser(this);
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Oops, ai refuzat sa incarci poza.",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == Constants.getPickImageRequestCode()){
                if (data != null){
                   iv_updateProduct.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_vector_edit));

                   selectedImageFileURI = data.getData();
                    try {
                        loader.loadUserPicture(selectedImageFileURI,iv_imageProduct);
                    } catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Image selection failed",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void addSpinnerAdapter() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.add_product_age_values,
                android.R.layout.simple_spinner_dropdown_item);
        spnProductAge.setAdapter(adapter);
    }

}
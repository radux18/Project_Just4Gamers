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

public class UpdateProductActivity extends AppCompatActivity {

    private Uri selectedImageFileURI = null;
    private String productImageURL = "";

    private GlideLoader loader = new GlideLoader(UpdateProductActivity.this);
    private Toolbar tb_product;
    private Button btnEdit;
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

    private Product productDetails = new Product();

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        intent = getIntent();
        initComp();
        setupActionBar();

        if (intent.hasExtra(Constants.getExtraProductDetails())){
            productDetails = intent.getParcelableExtra(Constants.getExtraProductDetails());
        }

        populateUI(productDetails);

        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1){
                    if (checkedId == R.id.rb_editUsed){
                        llProductAge.setVisibility(View.VISIBLE);
                    } else if (checkedId == R.id.rb_editNew){
                        llProductAge.setVisibility(View.GONE);
                    }
                }
            }
        });

        iv_updateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(UpdateProductActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    new Constants().showImageChooser(UpdateProductActivity.this);
                } else {
                    //Requests permissions to be granted.
                    ActivityCompat.requestPermissions(UpdateProductActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}
                            ,Constants.getReadStoragePermissionCode());
                }
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    if (selectedImageFileURI != null){
                        new FirestoreManager().uploadImageToCloudStorage(UpdateProductActivity.this,selectedImageFileURI, Constants.getProductImage());
                    } else {
                        updateProduct();
                    }
                }
            }
        });
    }

    private void setupActionBar() {
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

    private void updateProduct() {
        String title = tiet_title.getText().toString().trim();
        int price = Integer.parseInt(tiet_price.getText().toString().trim());
        String quantity = tiet_quantity.getText().toString().trim();
        String description = tiet_description.getText().toString();
        int age = Integer.parseInt(spnProductAge.getSelectedItem().toString());

        HashMap<String, Object> productHashMap = new HashMap<>();

        if (!description.equals(productDetails.getDescription())){
            productHashMap.put(Constants.getDESCRIPTION(), description);
        }
        if (!productImageURL.isEmpty()){
            productHashMap.put(Constants.getIMAGE(), productImageURL);
        }
        if (price != productDetails.getPrice()){
            productHashMap.put(Constants.getPRICE(), price);
        }
        if (!quantity.equals(productDetails.getStock_quantity())){
            productHashMap.put(Constants.getStockQuantity(), quantity);
        }
        if (!title.equals(productDetails.getTitle())){
            productHashMap.put(Constants.getTITLE(), title);
        }

        if (rbNew.isChecked()){
            type = Constants.getNEW();
            productHashMap.put(Constants.getAGE(), 0);
            productHashMap.put(Constants.getTYPE(), type);
        } else {
            type = Constants.getUSED();
            productHashMap.put(Constants.getAGE(), age);
            productHashMap.put(Constants.getTYPE(), type);
        }

        new FirestoreManager().updateProduct(UpdateProductActivity.this, productHashMap, productDetails.getProduct_id());
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

    private void populateUI(Product productDetails) {
        tiet_title.setText(productDetails.getTitle());
        tiet_description.setText(productDetails.getDescription());
        tiet_price.setText(String.valueOf(productDetails.getPrice()));
        tiet_quantity.setText(productDetails.getStock_quantity());
        loader.loadProductPicture(productDetails.getImage(),iv_imageProduct);
    }

    private void initComp() {
        tb_product = findViewById(R.id.toolbar_updateProduct);
        btnEdit = findViewById(R.id.btn_editProduct_submit);
        tiet_title = findViewById(R.id.tiet_editProduct_title);
        tiet_price = findViewById(R.id.tiet_editProduct_price);
        tiet_description = findViewById(R.id.tiet_editProduct_description);
        tiet_quantity = findViewById(R.id.tiet_editProduct_quantity);
        iv_updateProduct = findViewById(R.id.iv_update_product);
        iv_imageProduct = findViewById(R.id.iv_editProduct_image);
        spnProductAge = findViewById(R.id.spn_editProduct_age);
        rbNew = findViewById(R.id.rb_editNew);
        rbUsed = findViewById(R.id.rb_editUsed);
        llProductAge = findViewById(R.id.ll_editProduct_age);
        rgType = findViewById(R.id.rg_editProduct);
        addSpinnerAdapter();
    }

    private void addSpinnerAdapter() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.add_product_age_values,
                android.R.layout.simple_spinner_dropdown_item);
        spnProductAge.setAdapter(adapter);
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
                        selectedImageFileURI = data.getData();
                        loader.loadUserPicture(selectedImageFileURI,iv_imageProduct);
                    } catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Image selection failed",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }
    }

    public void imageUploadSuccess(String imageUrl){
        productImageURL = imageUrl;
        updateProduct();
    }


    public void productUpdateSuccess() {
        Toast.makeText(getApplicationContext(), "Ai modificat cu succes produsul!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
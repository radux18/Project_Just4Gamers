package com.example.project_just4gamers.userInterface.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.models.SoldProduct;
import com.example.project_just4gamers.utilities.Constants;
import com.example.project_just4gamers.utilities.glideLoader.GlideLoader;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SoldProductDetailsActivity extends AppCompatActivity {

    private SoldProduct productDetails = new SoldProduct();
    private Intent intent;
    private Toolbar tbSoldProducts;
    private TextView tvSoldProductId;
    private TextView tvSoldProductDate;
    private TextView tvSoldProductType;
    private ImageView ivSoldProductImage;
    private TextView tvSoldProductAddressName;
    private TextView tvSoldProductFullName;
    private TextView tvSoldProductPrice;
    private TextView tvSoldProductAddress;
    private TextView tvSoldProductAdditionalNote;
    private TextView tvSoldProductOtherDetails;
    private TextView tvSoldProductMobileNumber;
    private TextView tvSoldProductSubtotal;
    private TextView tvSoldProductTotalAmount;
    private TextView tvSoldProductShippingCharge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sold_product_details);
        intent = getIntent();

        initComponents();
        setupActionBar();

        if (intent.hasExtra(Constants.getExtraSoldProductDetails())){
            productDetails = intent.getParcelableExtra(Constants.getExtraSoldProductDetails());

        }
        setupUI(productDetails);
    }

    private void initComponents() {
        tbSoldProducts = findViewById(R.id.toolbar_sold_product_details_activity);
        tvSoldProductAdditionalNote = findViewById(R.id.tv_sold_details_additional_note);
        tvSoldProductAddress = findViewById(R.id.tv_sold_details_address);
        tvSoldProductId = findViewById(R.id.tv_sold_order_details_id);
        tvSoldProductDate = findViewById(R.id.tv_sold_order_details_date);
        tvSoldProductMobileNumber = findViewById(R.id.tv_sold_details_mobile_number);
        tvSoldProductAddressName = findViewById(R.id.tv_sold_details_full_name);
        tvSoldProductOtherDetails = findViewById(R.id.tv_sold_details_other_details);
        tvSoldProductPrice = findViewById(R.id.tv_product_item_price);
        tvSoldProductShippingCharge = findViewById(R.id.tv_sold_product_shipping_charge);
        tvSoldProductSubtotal = findViewById(R.id.tv_sold_product_sub_total);
        tvSoldProductType = findViewById(R.id.tv_sold_details_address_type);
        ivSoldProductImage = findViewById(R.id.iv_product_item_image);
        tvSoldProductTotalAmount = findViewById(R.id.tv_sold_product_total_amount);
        tvSoldProductFullName = findViewById(R.id.tv_product_item_name);

    }

    private void setupActionBar(){
        setSupportActionBar(tbSoldProducts);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp);
        }

        tbSoldProducts.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setupUI(SoldProduct productDetails){

        tvSoldProductId.setText(productDetails.getOrder_id());

        String dateFormat = "dd MM yyyy HH:mm";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(productDetails.getOrder_date());
        String orderDateTime = formatter.format(calendar.getTime());
        tvSoldProductDate.setText(orderDateTime);

        new GlideLoader(SoldProductDetailsActivity.this).loadProductPicture(
                productDetails.getImage(),
                ivSoldProductImage
        );

        tvSoldProductPrice.setText(getString(R.string.item_price_format,productDetails.getPrice()));
        tvSoldProductFullName.setText(productDetails.getTitle());
        tvSoldProductAddressName.setText(productDetails.getAddress().getName());
        tvSoldProductType.setText(productDetails.getAddress().getType());
        tvSoldProductAddress.setText(getResources().getString(R.string.address_details,
                productDetails.getAddress().getAddress(),
                productDetails.getAddress().getZipCode()));
        tvSoldProductAdditionalNote.setText(productDetails.getAddress().getAdditionalNote());

        if (!productDetails.getAddress().getOtherDetails().isEmpty()){
            tvSoldProductOtherDetails.setVisibility(View.VISIBLE);
            tvSoldProductOtherDetails.setText(productDetails.getAddress().getOtherDetails());
        } else {
            tvSoldProductOtherDetails.setVisibility(View.GONE);
        }

        tvSoldProductMobileNumber.setText(productDetails.getAddress().getMobileNumber());
        tvSoldProductSubtotal.setText(productDetails.getSubtotal());
        tvSoldProductTotalAmount.setText(productDetails.getTotalAmount());
        tvSoldProductShippingCharge.setText(productDetails.getShippingCharge());


    }



}
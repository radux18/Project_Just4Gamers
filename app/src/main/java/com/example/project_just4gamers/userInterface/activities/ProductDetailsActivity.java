package com.example.project_just4gamers.userInterface.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.CloudFirestoreManager;
import com.example.project_just4gamers.models.CartItem;
import com.example.project_just4gamers.models.Product;
import com.example.project_just4gamers.models.User;
import com.example.project_just4gamers.utilities.Constants;
import com.example.project_just4gamers.utilities.glideLoader.GlideLoader;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {
    private Toolbar tb_productDetails;
    private Intent intent;
    private String productId;
    private String productOwnerId = "";
    private ImageView iv_productDetailsImg;
    private TextView tvProductTitle;
    private TextView tvProductPrice;
    private TextView tvProductDescription;
    private TextView tvProductAge;
    private TextView tvProductQuantity;
    private Button btnAddToCart;
    private Button btnGoToCart;
    private FloatingActionButton fabAddMessage;
    private TextView tvUserName;
    private ImageView ivUserProfileImage;
    private LinearLayout llTriggerToProfile;
    private FloatingActionButton fabEdit;
    private CheckBox cbFavorite;
    private int isFavorite = 1;
    private int isUnfavorite = 0;

    private Product productDetails;

    private User productOwner = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        intent = getIntent();
        initComponents();
        setupActionBar();

        if (intent.hasExtra(Constants.getExtraProductId())){
            productId = intent.getStringExtra(Constants.getExtraProductId());
        }

        if (intent.hasExtra(Constants.getExtraProductOwnerId())){
            productOwnerId = intent.getStringExtra(Constants.getExtraProductOwnerId());
            new CloudFirestoreManager().getProductOwnerDetails(ProductDetailsActivity.this, productOwnerId);
        }

        if (new CloudFirestoreManager().getCurrentUserID().equals(productOwnerId)){
            btnAddToCart.setVisibility(View.GONE);
            btnGoToCart.setVisibility(View.GONE);
            fabAddMessage.setVisibility(View.GONE);
            fabEdit.setVisibility(View.VISIBLE);
        } else {
            fabEdit.setVisibility(View.GONE);
            btnAddToCart.setVisibility(View.VISIBLE);
            fabAddMessage.setVisibility(View.VISIBLE);
            btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v != null){
                        addToCart();
                    }
                }
            });
        }
        getProductDetails();

        btnGoToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductDetailsActivity.this, CartListActivity.class));
            }
        });

        fabAddMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddMessageActivity.class);
                intent.putExtra(Constants.getExtraDetailsMessage(), productOwner);
                startActivity(intent);
            }
        });

        llTriggerToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productDetails.getUser_id().equals(new CloudFirestoreManager().getCurrentUserID())){
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), ProductOwnerProfileActivity.class);
                    intent.putExtra(Constants.getExtraProfileDetailsv2(), productDetails.getUser_id());
                    startActivity(intent);
                }
            }
        });
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdateProductActivity.class);
                intent.putExtra(Constants.getExtraProductDetails(), productDetails);
                startActivity(intent);
            }
        });
    }

    private void initComponents() {
        tb_productDetails = findViewById(R.id.tb_productDetails);
        tvProductDescription = findViewById(R.id.tv_product_detailsDescription);
        tvProductPrice = findViewById(R.id.tv_productDetails_price);
        tvProductQuantity = findViewById(R.id.tv_product_details_stock_quantity);
        tvProductTitle = findViewById(R.id.tv_productDetails_title);
        iv_productDetailsImg = findViewById(R.id.iv_product_details_image);
        btnAddToCart = findViewById(R.id.btn_add_to_cart);
        btnGoToCart = findViewById(R.id.btn_go_to_cart);
        tvProductAge = findViewById(R.id.tv_productDetails_age);
        fabAddMessage = findViewById(R.id.fab_add_message);
        tvUserName = findViewById(R.id.tv_user_detail_name);
        ivUserProfileImage = findViewById(R.id.iv_product_user_image);
        llTriggerToProfile = findViewById(R.id.ll_userGoToProfile);
        fabEdit = findViewById(R.id.fab_edit_product);
        cbFavorite = findViewById(R.id.cb_product_favorite);
    }

    private void getProductDetails(){
        //show progress dialog
        new CloudFirestoreManager().getProductDetails(ProductDetailsActivity.this, productId);
    }

    public void getUserDetails(User user){
        productOwner = user;

        new GlideLoader(ProductDetailsActivity.this).loadProductPicture(
                productOwner.getImage(),
                ivUserProfileImage
        );
        tvUserName.setText(getString(R.string.tv_settings_name, productOwner.getFirstName(), productOwner.getLastName()));
    }

    public void productExistsInCart(){
        //hide progress dialog
        btnGoToCart.setVisibility(View.VISIBLE);
        btnAddToCart.setVisibility(View.GONE);
    }

    public void productDetailsSuccess(Product product){
        productDetails = product;

        new GlideLoader(ProductDetailsActivity.this)
                .loadUserPicture(product.getImage(), iv_productDetailsImg);

        tvProductTitle.setText(product.getTitle());
        tvProductPrice.setText(getString(R.string.item_price_format, String.valueOf(product.getPrice())));

        if (product.getAge() == 0){
            tvProductAge.setText(R.string.rb_newT);
        } else {
            tvProductAge.setText(getString(R.string.item_age_format, String.valueOf(product.getAge())));
        }

        tvProductDescription.setText(product.getDescription());
        tvProductQuantity.setText(product.getStock_quantity());

        cbFavorite.setChecked(!productDetails.getFavorite().equals("") && !productDetails.getFavorite().equals("0"));

        if (productDetails.getUser_id().equals(new CloudFirestoreManager().getCurrentUserID())){
            cbFavorite.setVisibility(View.GONE);
        } else {
            cbFavorite.setVisibility(View.VISIBLE);
        }

        if (Integer.parseInt(product.getStock_quantity()) == 0){
            //hide progress dialog
            btnAddToCart.setVisibility(View.GONE);
            tvProductQuantity.setText(getResources().getString(R.string.lbl_out_of_stock));
            tvProductQuantity.setTextColor(ContextCompat.getColor(
                    ProductDetailsActivity.this, R.color.redError
            ));
        } else {
            if (new CloudFirestoreManager().getCurrentUserID().equals(product.getUser_id())){
                //hide progress dialog
                setProgressBarVisibility(false);
            } else {
                new CloudFirestoreManager().checkIfItemExistInCart(ProductDetailsActivity.this, productId);
            }
        }

        cbFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                HashMap<String, Object> productFavoriteHashMap = new HashMap<>();
                HashMap<String, Object> productHashMap = new HashMap<>();
                if (isChecked){
                    productFavoriteHashMap.put(Constants.getFAVORITE(), String.valueOf(isFavorite));
                    productHashMap.put(Constants.getUserfavoriteId(), new CloudFirestoreManager().getCurrentUserID());

                    new CloudFirestoreManager().updateFavoriteProduct(ProductDetailsActivity.this, productFavoriteHashMap, productDetails.getProduct_id());
                    new CloudFirestoreManager().updateProductFavoriteId(ProductDetailsActivity.this, productHashMap, productDetails.getProduct_id());

                } else {
                    productFavoriteHashMap.put(Constants.getFAVORITE(), String.valueOf(isUnfavorite));
                    productHashMap.put(Constants.getUserfavoriteId(), "");
                    new CloudFirestoreManager().updateFavoriteProduct(ProductDetailsActivity.this, productFavoriteHashMap, productDetails.getProduct_id());
                    new CloudFirestoreManager().updateProductFavoriteId(ProductDetailsActivity.this, productHashMap, productDetails.getProduct_id());
                }
            }
        });
    }

    private void addToCart(){
        CartItem cartItem = new CartItem(new CloudFirestoreManager().getCurrentUserID(),
                productOwnerId,
                productId, productDetails.getTitle(), productDetails.getPrice(), productDetails.getImage(), productDetails.getType(),
                productDetails.getAge(), Constants.getDefaultCartQuantity());
        new CloudFirestoreManager().addCartItems(ProductDetailsActivity.this, cartItem);
        //show progress dialog
    }

    public void addToCartSuccess(){
            //hideprogressdialog
        Toast.makeText(getApplicationContext(), "Produsul a fost adaugat in cos!", Toast.LENGTH_SHORT).show();

        btnAddToCart.setVisibility(View.GONE);
        btnGoToCart.setVisibility(View.VISIBLE);
    }

    private void setupActionBar(){
        setSupportActionBar(tb_productDetails);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp);
        }

        tb_productDetails.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void favoriteAddSuccess() {
        Toast.makeText(getApplicationContext(), "Ai adaugat cu success produsul la favorite!", Toast.LENGTH_SHORT).show();
    }

}
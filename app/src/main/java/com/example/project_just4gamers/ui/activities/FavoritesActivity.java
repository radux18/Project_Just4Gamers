package com.example.project_just4gamers.ui.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.FirestoreManager;
import com.example.project_just4gamers.models.Product;
import com.example.project_just4gamers.ui.adapters.FavoriteProductsListAdapter;
import com.example.project_just4gamers.ui.adapters.MessageListAdapter;
import com.example.project_just4gamers.ui.adapters.ProductListAdapter;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView rv_favoriteListItems;
    private TextView tvNoProductFound;
    private Toolbar tbFavs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        initComp();
        setupActionBar();
        getFavoriteProduct();
    }

    private void setupActionBar() {
        setSupportActionBar(tbFavs);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp);
        }

        tbFavs.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getFavoriteProduct() {
        new FirestoreManager().getFavoriteProducts(FavoritesActivity.this);
    }


    private void initComp() {
        rv_favoriteListItems = findViewById(R.id.rv_favorite_items);
        tvNoProductFound = findViewById(R.id.tv_no_favorites_found);
        tbFavs = findViewById(R.id.toolbar_favoriteProducts);
    }

    public void successGetFavProducts(ArrayList<Product> products) {
        if (products.size() > 0){
            rv_favoriteListItems.setVisibility(View.VISIBLE);
            tvNoProductFound.setVisibility(View.GONE);

            rv_favoriteListItems.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rv_favoriteListItems.setHasFixedSize(true);

            FavoriteProductsListAdapter adapter = new FavoriteProductsListAdapter(getApplicationContext(), products);
            rv_favoriteListItems.setAdapter(adapter);
        } else {
            rv_favoriteListItems.setVisibility(View.GONE);
            tvNoProductFound.setVisibility(View.VISIBLE);
        }
    }
}

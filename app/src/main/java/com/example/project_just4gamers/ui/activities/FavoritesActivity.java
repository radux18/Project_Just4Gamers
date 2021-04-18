package com.example.project_just4gamers.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        initComp();
        getFavoriteProduct();
    }

    private void getFavoriteProduct() {
        new FirestoreManager().getFavoriteProducts(FavoritesActivity.this);
    }


    private void initComp() {
        rv_favoriteListItems = findViewById(R.id.rv_favorite_items);
        tvNoProductFound = findViewById(R.id.tv_no_favorites_found);
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

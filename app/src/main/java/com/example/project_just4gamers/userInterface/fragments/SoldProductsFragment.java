package com.example.project_just4gamers.userInterface.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.CloudFirestoreManager;
import com.example.project_just4gamers.models.SoldProduct;
import com.example.project_just4gamers.userInterface.adapters.SoldProductsListAdapter;

import java.util.ArrayList;

public class SoldProductsFragment extends Fragment {

    private TextView tvNoSoldProducts;
    private RecyclerView rv_soldProductsList;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sold_products, container, false);
        tvNoSoldProducts = view.findViewById(R.id.tv_no_sold_products_found);
        rv_soldProductsList = view.findViewById(R.id.rv_sold_product_items);
        return  view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getSoldProductList();
    }

    private void getSoldProductList(){
        //show progress dialog

        new CloudFirestoreManager().getSoldProductsList(SoldProductsFragment.this);
    }

    public void successSoldProductsList(ArrayList<SoldProduct> soldProducts){
        if (soldProducts.size() > 0){
            tvNoSoldProducts.setVisibility(View.GONE);
            rv_soldProductsList.setVisibility(View.VISIBLE);

            rv_soldProductsList.setLayoutManager(new LinearLayoutManager(getActivity()));
            rv_soldProductsList.setHasFixedSize(true);

            SoldProductsListAdapter adapter = new SoldProductsListAdapter(requireActivity(), soldProducts);
            rv_soldProductsList.setAdapter(adapter);

        } else {
            tvNoSoldProducts.setVisibility(View.VISIBLE);
            rv_soldProductsList.setVisibility(View.GONE);
        }



    }


}
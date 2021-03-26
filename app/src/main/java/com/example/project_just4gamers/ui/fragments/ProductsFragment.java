package com.example.project_just4gamers.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.FirestoreManager;
import com.example.project_just4gamers.models.Product;
import com.example.project_just4gamers.models.User;
import com.example.project_just4gamers.ui.activities.AddProductActivity;
import com.example.project_just4gamers.ui.activities.SettingsActivity;
import com.example.project_just4gamers.ui.adapters.ProductListAdapter;

import java.util.ArrayList;

public class ProductsFragment extends Fragment {

    private RecyclerView rv_productListItems;
    private TextView tvNoProductFound;
    private ProductListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void deleteProduct(String productID){
      showAlertDialogDeleteProduct(productID);
    }

    public void productDeleteSuccess(){
        //hide progress dialog
        Toast.makeText(requireActivity(), "Product deleted on success!", Toast.LENGTH_SHORT).show();

        getProductListFromFirestore();
    }

    private void showAlertDialogDeleteProduct(String productID){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(getResources().getString(R.string.delete_dialog_title));
        builder.setMessage(getResources().getString(R.string.delete_dialog_message));
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //show progress dialog
                new FirestoreManager().deleteProduct(ProductsFragment.this, productID);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void successProductListFromFirestore(ArrayList<Product> products){
        //hide progress dialog
       if (products.size() > 0){
           rv_productListItems.setVisibility(View.VISIBLE);
           tvNoProductFound.setVisibility(View.GONE);

           rv_productListItems.setLayoutManager(new LinearLayoutManager(getActivity()));
           rv_productListItems.setHasFixedSize(true);

           adapter = new ProductListAdapter(requireActivity(), products, ProductsFragment.this);
           rv_productListItems.setAdapter(adapter);
       } else {
           rv_productListItems.setVisibility(View.GONE);
           tvNoProductFound.setVisibility(View.VISIBLE);
       }
    }

    private void getProductListFromFirestore(){
        //show progress dialog
        new FirestoreManager().getProductList(ProductsFragment.this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        rv_productListItems = view.findViewById(R.id.rv_product_items);
        tvNoProductFound = view.findViewById(R.id.tv_no_products_found);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        getProductListFromFirestore();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.add_product_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(getActivity().getApplicationContext(), AddProductActivity.class));
                return true;
            }
        });
        return super.onOptionsItemSelected(item);
    }
}
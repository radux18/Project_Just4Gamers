package com.example.project_just4gamers.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.FirestoreManager;
import com.example.project_just4gamers.models.Product;
import com.example.project_just4gamers.ui.activities.CartListActivity;
import com.example.project_just4gamers.ui.activities.DiscountCouponsActivity;
import com.example.project_just4gamers.ui.activities.ProductDetailsActivity;
import com.example.project_just4gamers.ui.activities.SettingsActivity;
import com.example.project_just4gamers.ui.adapters.DashboardListAdapter;
import com.example.project_just4gamers.ui.adapters.ProductListAdapter;
import com.example.project_just4gamers.utils.Constants;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {
    private RecyclerView rv_dashboardListItems;
    private TextView tvNoProductFound;
    private DashboardListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //If he want to use the option menu in fragment we need to add it.
        setHasOptionsMenu(true);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        rv_dashboardListItems = view.findViewById(R.id.rv_dashboard_items);
        tvNoProductFound = view.findViewById(R.id.tv_no_dashboard_items_found);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.dashboard_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_setting){
                    startActivity(new Intent(getActivity().getApplicationContext(), SettingsActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.action_cart){
                    startActivity(new Intent(getActivity().getApplicationContext(), CartListActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.action_coupons){
                    startActivity(new Intent(getActivity().getApplicationContext(), DiscountCouponsActivity.class));
                    return true;
                }
              return false;
            }
        });
      return super.onOptionsItemSelected(item);
    }

    public void successDashboardItemsList(ArrayList<Product> dashboardItemsList){
        //hide progress dialog
        if (dashboardItemsList.size() > 0 ){
            rv_dashboardListItems.setVisibility(View.VISIBLE);
            tvNoProductFound.setVisibility(View.GONE);

            rv_dashboardListItems.setLayoutManager(new GridLayoutManager(getActivity(),2));
            rv_dashboardListItems.setHasFixedSize(true);

            adapter = new DashboardListAdapter(requireActivity(), dashboardItemsList);
            rv_dashboardListItems.setAdapter(adapter);

        } else {
            rv_dashboardListItems.setVisibility(View.GONE);
            tvNoProductFound.setVisibility(View.VISIBLE);
        }
    }

    private void getDashboardItemsList(){
        //show progress dialog
        new FirestoreManager().getDashboardItemsList(DashboardFragment.this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getDashboardItemsList();
    }
}
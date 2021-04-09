package com.example.project_just4gamers.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.FirestoreManager;
import com.example.project_just4gamers.models.Product;
import com.example.project_just4gamers.ui.activities.CartListActivity;
import com.example.project_just4gamers.ui.activities.DiscountCouponsActivity;
import com.example.project_just4gamers.ui.activities.InboxActivity;
import com.example.project_just4gamers.ui.activities.SettingsActivity;
import com.example.project_just4gamers.ui.adapters.DashboardListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {
    private RecyclerView rv_dashboardListItems;
    private TextView tvNoProductFound;
    private LinearLayout llSearch;
    private EditText etSearch;
    private Spinner spnFilter;
    private FloatingActionButton fabSearch;
    private DashboardListAdapter adapter;
    private ImageView ivResetBtn;

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
        llSearch = view.findViewById(R.id.ll_search);
        etSearch = view.findViewById(R.id.etSearch);
        fabSearch = view.findViewById(R.id.fab_search);
        ivResetBtn = view.findViewById(R.id.iv_resetBtn);
        spnFilter = view.findViewById(R.id.spn_filter);
        addSpinnerAdapter();
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
                } else if (item.getItemId() == R.id.action_inbox){
                    startActivity(new Intent(getActivity().getApplicationContext(), InboxActivity.class));
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
            fabSearch.setVisibility(View.VISIBLE);

            rv_dashboardListItems.setLayoutManager(new GridLayoutManager(getActivity(),2));
            rv_dashboardListItems.setHasFixedSize(true);

            adapter = new DashboardListAdapter(requireActivity(), dashboardItemsList);
            rv_dashboardListItems.setAdapter(adapter);

            fabSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llSearch.setVisibility(View.VISIBLE);
                }
            });
            ivResetBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter = new DashboardListAdapter(requireActivity(), dashboardItemsList);
                    rv_dashboardListItems.setAdapter(adapter);
                    etSearch.setText("");
                }
            });

            etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    DashboardFragment.this.adapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        } else {
            rv_dashboardListItems.setVisibility(View.GONE);
            tvNoProductFound.setVisibility(View.VISIBLE);
            fabSearch.setVisibility(View.GONE);
            llSearch.setVisibility(View.GONE);
        }


//        spnFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Object item = parent.getItemAtPosition(position);
//                if (position == 0){
//
//                }
//            }
//        });

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

    private void addSpinnerAdapter() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.filter_items,
                android.R.layout.simple_spinner_dropdown_item);
        spnFilter.setAdapter(adapter);
    }


}
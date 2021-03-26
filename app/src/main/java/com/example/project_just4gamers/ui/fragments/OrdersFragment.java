package com.example.project_just4gamers.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.FirestoreManager;
import com.example.project_just4gamers.models.Order;
import com.example.project_just4gamers.ui.adapters.OrdersListAdapter;

import java.util.ArrayList;

public class OrdersFragment extends Fragment {

    private RecyclerView rv_orderItemsList;
    private TextView tvNoOrdersFound;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        rv_orderItemsList = view.findViewById(R.id.rv_orderItems);
        tvNoOrdersFound = view.findViewById(R.id.tv_no_order_placed);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
        getOrderList();
    }

    public void getOrderList() {
        //show progress dialog
        new FirestoreManager().getOrdersList(OrdersFragment.this);
    }

    public void successOrderPlaced(ArrayList<Order> ordersList) {
        if (ordersList.size() > 0){
            rv_orderItemsList.setVisibility(View.VISIBLE);
            tvNoOrdersFound.setVisibility(View.GONE);

            rv_orderItemsList.setLayoutManager(new LinearLayoutManager(getActivity()));
            rv_orderItemsList.setHasFixedSize(true);

            OrdersListAdapter adapter = new OrdersListAdapter(ordersList, requireActivity());
            rv_orderItemsList.setAdapter(adapter);
        } else {
            rv_orderItemsList.setVisibility(View.GONE);
            tvNoOrdersFound.setVisibility(View.VISIBLE);
        }
    }





}
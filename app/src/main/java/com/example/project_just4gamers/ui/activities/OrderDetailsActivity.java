package com.example.project_just4gamers.ui.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.models.Order;
import com.example.project_just4gamers.ui.adapters.CartItemsAdapter;
import com.example.project_just4gamers.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class OrderDetailsActivity extends AppCompatActivity {

    private Intent intent;
    private Order orderDetails = new Order();
    private Toolbar tbOrderDetails;
    private TextView tvOrderId;
    private TextView tvOrderDate;
    private TextView tvOrderStatus;
    private TextView tvOrderAddressType;
    private TextView tvOrderFullName;
    private TextView tvOrderAddress;
    private TextView tvOrderAdditionalNote;
    private TextView tvOrderOtherDetails;
    private TextView tvOrderMobileNumber;
    private TextView tvOrderSubtotal;
    private TextView tvOrderTotalAmount;
    private TextView tvOrderShippingCharge;

    private RecyclerView rvOrderDetailsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        intent=getIntent();

        initComponents();
        setupActionBar();

        if (intent.hasExtra(Constants.getExtraMyOrderDetails())){
            orderDetails = intent.getParcelableExtra(Constants.getExtraMyOrderDetails());
        }

        setupUI(orderDetails);
    }

    private void initComponents() {
        tbOrderDetails = findViewById(R.id.toolbar_order_details_activity);
        tvOrderId = findViewById(R.id.tv_order_details_id);
        tvOrderDate = findViewById(R.id.tv_order_details_date);
        tvOrderStatus = findViewById(R.id.tv_order_status);
        tvOrderAdditionalNote = findViewById(R.id.tv_order_details_additional_note);
        tvOrderAddress = findViewById(R.id.tv_order_details_address);
        tvOrderAddressType = findViewById(R.id.tv_order_details_address_type);
        tvOrderFullName = findViewById(R.id.tv_order_details_full_name);
        tvOrderOtherDetails = findViewById(R.id.tv_order_details_other_details);
        tvOrderMobileNumber = findViewById(R.id.tv_order_details_mobile_number);
        tvOrderSubtotal = findViewById(R.id.tv_order_details_sub_total);
        tvOrderShippingCharge = findViewById(R.id.tv_order_details_shipping_charge);
        tvOrderTotalAmount = findViewById(R.id.tv_order_details_total_amount);
        rvOrderDetailsList = findViewById(R.id.rv_order_items_list);
    }

    private void setupActionBar(){
        setSupportActionBar(tbOrderDetails);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp);
        }

        tbOrderDetails.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setupUI(Order orderDetails){
        //setup the order interface with order details
        tvOrderId.setText(orderDetails.getTitle());

        String dateFormat = "dd MM yyyy HH:mm";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(orderDetails.getOrder_dateTime());
        String orderDateTime = formatter.format(calendar.getTime());
        tvOrderDate.setText(orderDateTime);

        long diffInMilliSeconds = System.currentTimeMillis() - orderDetails.getOrder_dateTime();
        long diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMilliSeconds);

        // if the order has been made less than 1 hour -> status = pending
        if (diffInHours < 1){
            tvOrderStatus.setText(getResources().getString(R.string.order_status_pending));
            tvOrderStatus.setTextColor(ContextCompat.getColor(OrderDetailsActivity.this, R.color.redError));
        } else if (diffInHours > 1 && diffInHours < 4){   // if the order has been made less than 4 hour -> status = in progress
            tvOrderStatus.setText(getResources().getString(R.string.order_status_inProcess));
            tvOrderStatus.setTextColor(ContextCompat.getColor(OrderDetailsActivity.this, R.color.orderStatusInProcess));
        } else {   // if the order has been made for more than 4 hours -> status = delivered
            tvOrderStatus.setText(getResources().getString(R.string.order_status_delivered));
            tvOrderStatus.setTextColor(ContextCompat.getColor(OrderDetailsActivity.this, R.color.orderStatusDelivered));
        }

        //setting up the recycler view
        rvOrderDetailsList.setLayoutManager(new LinearLayoutManager(OrderDetailsActivity.this));
        rvOrderDetailsList.setHasFixedSize(true);

        //re-use the cart adapter
        //we pass with this adapter the items list
        CartItemsAdapter adapter = new CartItemsAdapter(OrderDetailsActivity.this, orderDetails.getItems(), false);
        rvOrderDetailsList.setAdapter(adapter);

        tvOrderFullName.setText(orderDetails.getAddress().getName());
        tvOrderAddressType.setText(orderDetails.getAddress().getType());
        tvOrderAddress.setText(getResources().getString(R.string.address_details,
                                                    orderDetails.getAddress().getAddress(),
                                                    orderDetails.getAddress().getZipCode()));
        tvOrderAdditionalNote.setText(orderDetails.getAddress().getAdditionalNote());

        if (!orderDetails.getAddress().getOtherDetails().isEmpty()){
            tvOrderOtherDetails.setVisibility(View.VISIBLE);
            tvOrderOtherDetails.setText(orderDetails.getAddress().getOtherDetails());
        } else {
            tvOrderOtherDetails.setVisibility(View.GONE);
        }

        tvOrderMobileNumber.setText(orderDetails.getAddress().getMobileNumber());
        tvOrderSubtotal.setText(orderDetails.getSubtotal());
        tvOrderTotalAmount.setText(orderDetails.getTotalAmount());
        tvOrderShippingCharge.setText(orderDetails.getShippingCharge());


    }
}
package com.example.project_just4gamers.userInterface.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.CloudFirestoreManager;
import com.example.project_just4gamers.models.Address;
import com.example.project_just4gamers.userInterface.adapters.AddressListAdapter;
import com.example.project_just4gamers.utilities.Constants;
import com.example.project_just4gamers.utilities.glideLoader.SwipeToEditCallback;
import com.example.project_just4gamers.utilities.glideLoader.SwipeToDeleteCallback;

import java.util.ArrayList;
import java.util.List;

public class AddressListActivity extends AppCompatActivity {
    private boolean selectAddress = false;
    private Toolbar tbAddress;
    private TextView tvAddAddress;
    private RecyclerView rvAddressList;
    private TextView tv_noAddressFound;
    private Intent intent;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        intent = getIntent();
        initComp();
        setupActionBar();

        tvAddAddress.setOnClickListener(addAddressListener());

        if (intent.hasExtra(Constants.getExtraSelectAddress())){
            selectAddress = intent.getBooleanExtra(Constants.getExtraSelectAddress(), false);
        }

        if (selectAddress){
            tvTitle.setText(getString(R.string.title_select_address));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            getAddressList();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddressList();
    }

    private View.OnClickListener addAddressListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), AddAddressActivity.class),Constants.getAddAddressRequestCode());
            }
        };
    }

    private void initComp() {
        tbAddress = findViewById(R.id.toolbar_address_list);
        tvAddAddress = findViewById(R.id.tv_add_address);
        rvAddressList = findViewById(R.id.rv_address_list);
        tv_noAddressFound = findViewById(R.id.tv_no_address_found);
        tvTitle = findViewById(R.id.tv_title_address);
    }

    private void getAddressList(){
        //show progress dialog
        new CloudFirestoreManager().getAddressesList(AddressListActivity.this);
    }

    public void successAddressListFromFirestore(ArrayList<Address> addressList){
        //hide progress dialog
        if (addressList.size() > 0){
            rvAddressList.setVisibility(View.VISIBLE);
            tv_noAddressFound.setVisibility(View.GONE);

            rvAddressList.setLayoutManager(new LinearLayoutManager(AddressListActivity.this));
            rvAddressList.setHasFixedSize(true);

            AddressListAdapter addressListAdapter = new AddressListAdapter(AddressListActivity.this, addressList, selectAddress);
            rvAddressList.setAdapter(addressListAdapter);

            if (!selectAddress){
                SwipeToEditCallback editSwipeHandler = new SwipeToEditCallback(AddressListActivity.this, rvAddressList) {
                    AddressListAdapter adapter = (AddressListAdapter) rvAddressList.getAdapter();
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        adapter.notifyEditItem(AddressListActivity.this,
                                viewHolder.getAdapterPosition()
                        );
                    }
                };

                ItemTouchHelper editItemTouchHelper = new ItemTouchHelper(editSwipeHandler);
                editItemTouchHelper.attachToRecyclerView(rvAddressList);


                SwipeToDeleteCallback deleteSwipeHandler = new SwipeToDeleteCallback(AddressListActivity.this, rvAddressList) {
                    AddressListAdapter adapter = (AddressListAdapter) rvAddressList.getAdapter();
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        adapter.notifyRemoveItem(viewHolder.getAdapterPosition());
                    }
                };
                ItemTouchHelper delete = new ItemTouchHelper(deleteSwipeHandler);
                delete.attachToRecyclerView(rvAddressList);
            }

        } else {
            rvAddressList.setVisibility(View.GONE);
            tv_noAddressFound.setVisibility(View.VISIBLE);
        }


    }

    private void setupActionBar(){
        setSupportActionBar(tbAddress);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp);
        }

        tbAddress.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void itemRemovedSuccess() {
        Toast.makeText(getApplicationContext(), "Adresa a fost stearsa cu succes!", Toast.LENGTH_SHORT).show();
        getAddressList();
    }
}
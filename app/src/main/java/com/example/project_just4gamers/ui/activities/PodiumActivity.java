package com.example.project_just4gamers.ui.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.FirestoreManager;
import com.example.project_just4gamers.models.SoldProduct;
import com.example.project_just4gamers.models.User;
import com.example.project_just4gamers.ui.adapters.PodiumListAdapter;
import com.example.project_just4gamers.ui.adapters.PodiumListAdapterV2;
import com.example.project_just4gamers.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PodiumActivity extends AppCompatActivity {

    private Toolbar tbPodium;
    private RecyclerView rvPodiumList;
    private Spinner spnPodium;
    private Intent intent;
    private ArrayList<SoldProduct> soldProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podium);
        initComp();
        setActionBar();
        intent = getIntent();

        if (intent.hasExtra(Constants.getExtraSoldProductDetails())){
            soldProducts = intent.getParcelableArrayListExtra(Constants.getExtraSoldProductDetails());
        }

        spnPodium.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                   getAllUser();
                } else if (position == 1){
                    getAllUserV2();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getAllUserV2() {
        new FirestoreManager().getAllUsersV2(PodiumActivity.this);
    }


    private void getAllUser() {
        new FirestoreManager().getAllUsers(PodiumActivity.this);
    }


    public void successGetUsers(ArrayList<User> users){
        rvPodiumList.setLayoutManager(new LinearLayoutManager(PodiumActivity.this));
        rvPodiumList.setHasFixedSize(true);

        PodiumListAdapter adapter = new PodiumListAdapter(getApplicationContext(), users);
        rvPodiumList.setAdapter(adapter);

       String item = spnPodium.getSelectedItem().toString();
       if (item.equals("Cele mai multe puncte")){
           Collections.sort(users, new Comparator<User>() {
               @Override
               public int compare(User o1, User o2) {
                   return o2.getPoints() - o1.getPoints();
               }
           });
           adapter.notifyDataSetChanged();
       }
    }

    private void initComp() {
        tbPodium = findViewById(R.id.tb_podium);
        spnPodium = findViewById(R.id.spn_category);
        rvPodiumList = findViewById(R.id.rv_podiumList);
        addAdapter();
    }

    private void addAdapter() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.podium_category_items,
                android.R.layout.simple_spinner_dropdown_item);
        spnPodium.setAdapter(adapter);
        spnPodium.setSelection(0);
    }

    private void setActionBar() {
        setSupportActionBar(tbPodium);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp);
        }

        tbPodium.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void successGetUsersV2(ArrayList<User> users){
        rvPodiumList.setLayoutManager(new LinearLayoutManager(PodiumActivity.this));
        rvPodiumList.setHasFixedSize(true);

        PodiumListAdapterV2 adapter = new PodiumListAdapterV2(getApplicationContext(), users, soldProducts);
        rvPodiumList.setAdapter(adapter);

        String item = spnPodium.getSelectedItem().toString();
        if (item.equals("Cele mai multe produse vandute")){



            adapter.notifyDataSetChanged();
        }
    }

}
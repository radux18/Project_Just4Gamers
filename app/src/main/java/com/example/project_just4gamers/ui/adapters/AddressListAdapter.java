package com.example.project_just4gamers.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project_just4gamers.R;
import com.example.project_just4gamers.firestore.FirestoreManager;
import com.example.project_just4gamers.models.Address;
import com.example.project_just4gamers.ui.activities.ActivateCouponActivity;
import com.example.project_just4gamers.ui.activities.AddAddressActivity;
import com.example.project_just4gamers.ui.activities.CheckoutActivity;
import com.example.project_just4gamers.utils.Constants;

import java.util.ArrayList;

public class AddressListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private ArrayList<Address> addressList;
    private boolean selectAddress;

    public AddressListAdapter(@NonNull Context context, @NonNull ArrayList<Address> addressList, boolean selectedAddress) {
        this.context = context;
        this.addressList = addressList;
        this.selectAddress = selectedAddress;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddressListAdapter.ViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.item_address_layout,
                        parent,
                        false
                )
        );
    }

    public void notifyEditItem(Activity activity, int position){
        Intent intent = new Intent(context, AddAddressActivity.class);
        intent.putExtra(Constants.getExtraAddressDetails(), addressList.get(position));
        activity.startActivityForResult(intent, Constants.getAddAddressRequestCode());
        notifyItemChanged(position);
    }


    public void notifyRemoveItem(int position){
        new FirestoreManager().deleteAddress(context,addressList.get(position).getId());
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder item, int position) {
        Address model = addressList.get(position);

        if (item instanceof ViewHolder){
            ((ViewHolder) item).tvName.setText(model.getName());
            ((ViewHolder) item).tvAddressDetails.setText(context.getResources()
                                                    .getString(R.string.address_details, model.getAddress(), model.getZipCode()));
            ((ViewHolder) item).tvMobileNumber.setText(model.getMobileNumber());
            ((ViewHolder) item).tvType.setText(model.getType());

            if (selectAddress){
                item.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ActivateCouponActivity.class);
                        intent.putExtra(Constants.getExtraSelectedAddress(), model);
                        context.startActivity(intent);

                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvType;
        private TextView tvMobileNumber;
        private TextView tvAddressDetails;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_address_fullName);
            tvAddressDetails = itemView.findViewById(R.id.tv_address_details);
            tvMobileNumber = itemView.findViewById(R.id.tv_address_mobile_number);
            tvType = itemView.findViewById(R.id.tv_address_type);
        }
    }
}

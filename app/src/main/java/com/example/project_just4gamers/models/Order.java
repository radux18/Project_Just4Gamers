package com.example.project_just4gamers.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Order implements Parcelable {
    private String id = "";
    private String user_id = "";
    private ArrayList<CartItem> items;
    private Address address = new Address();
    private String title = "";
    private String image = "";
    private String subtotal = "";
    private String totalAmount = "";
    private String shippingCharge = "";
    private long order_dateTime = 0L;

    public Order() {
    }

    public Order(String user_id, ArrayList<CartItem> items, Address address, String title, String image, String subtotal, String totalAmount, String shippingCharge, long order_dateTime) {
        this.user_id = user_id;
        this.items = items;
        this.address = address;
        this.title = title;
        this.image = image;
        this.subtotal = subtotal;
        this.totalAmount = totalAmount;
        this.shippingCharge = shippingCharge;
        this.order_dateTime = order_dateTime;
    }

    protected Order(Parcel in) {
        id = in.readString();
        user_id = in.readString();
        items = in.createTypedArrayList(CartItem.CREATOR);
        address = in.readParcelable(Address.class.getClassLoader());
        title = in.readString();
        image = in.readString();
        subtotal = in.readString();
        totalAmount = in.readString();
        shippingCharge = in.readString();
        order_dateTime = in.readLong();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public long getOrder_dateTime() {
        return order_dateTime;
    }

    public void setOrder_dateTime(long order_dateTime) {
        this.order_dateTime = order_dateTime;
    }

    public static Creator<Order> getCREATOR() {
        return CREATOR;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public ArrayList<CartItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<CartItem> items) {
        this.items = items;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getShippingCharge() {
        return shippingCharge;
    }

    public void setShippingCharge(String shippingCharge) {
        this.shippingCharge = shippingCharge;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user_id);
        dest.writeTypedList(items);
        dest.writeParcelable(address, flags);
        dest.writeString(title);
        dest.writeString(image);
        dest.writeString(subtotal);
        dest.writeString(totalAmount);
        dest.writeString(shippingCharge);
        dest.writeLong(order_dateTime);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", items=" + items +
                ", address=" + address +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", subtotal='" + subtotal + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", shippingCharge='" + shippingCharge + '\'' +
                ", order_dateTime=" + order_dateTime +
                '}';
    }
}

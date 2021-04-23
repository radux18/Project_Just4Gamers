package com.example.project_just4gamers.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class SoldProduct implements Parcelable {

    private String id = "";
    private String user_id = "";
    private String title = "";
    private int price = 0;
    private String sold_quantity = "";
    private String image = "";
    private String order_id = "";
    private long order_date = 0L;
    private String subtotal = "";
    private String shippingCharge = "";
    private String totalAmount = "";
    private Address address = new Address();

    public SoldProduct() {
    }

    public SoldProduct(String user_id, String title, int price, String sold_quantity, String image, String order_id, long order_date, String subtotal, String shippingCharge, String totalAmount, Address address) {
        this.user_id = user_id;
        this.title = title;
        this.price = price;
        this.sold_quantity = sold_quantity;
        this.image = image;
        this.order_id = order_id;
        this.order_date = order_date;
        this.subtotal = subtotal;
        this.shippingCharge = shippingCharge;
        this.totalAmount = totalAmount;
        this.address = address;
    }


    protected SoldProduct(Parcel in) {
        id = in.readString();
        user_id = in.readString();
        title = in.readString();
        price = in.readInt();
        sold_quantity = in.readString();
        image = in.readString();
        order_id = in.readString();
        order_date = in.readLong();
        subtotal = in.readString();
        shippingCharge = in.readString();
        totalAmount = in.readString();
        address = in.readParcelable(Address.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user_id);
        dest.writeString(title);
        dest.writeInt(price);
        dest.writeString(sold_quantity);
        dest.writeString(image);
        dest.writeString(order_id);
        dest.writeLong(order_date);
        dest.writeString(subtotal);
        dest.writeString(shippingCharge);
        dest.writeString(totalAmount);
        dest.writeParcelable(address, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SoldProduct> CREATOR = new Creator<SoldProduct>() {
        @Override
        public SoldProduct createFromParcel(Parcel in) {
            return new SoldProduct(in);
        }

        @Override
        public SoldProduct[] newArray(int size) {
            return new SoldProduct[size];
        }
    };

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSold_quantity() {
        return sold_quantity;
    }

    public void setSold_quantity(String sold_quantity) {
        this.sold_quantity = sold_quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public long getOrder_date() {
        return order_date;
    }

    public void setOrder_date(long order_date) {
        this.order_date = order_date;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getShippingCharge() {
        return shippingCharge;
    }

    public void setShippingCharge(String shippingCharge) {
        this.shippingCharge = shippingCharge;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public static Creator<SoldProduct> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "SoldProduct{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", sold_quantity='" + sold_quantity + '\'' +
                ", image='" + image + '\'' +
                ", order_id='" + order_id + '\'' +
                ", order_date=" + order_date +
                ", subtotal='" + subtotal + '\'' +
                ", shippingCharge='" + shippingCharge + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", address=" + address +
                '}';
    }
}

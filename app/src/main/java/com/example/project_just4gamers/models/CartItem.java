package com.example.project_just4gamers.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CartItem implements Parcelable {

    private String id = "";
    private String user_id = "";
    private String product_ownerId = "";
    private String product_id = "";
    private String title = "";
    private int price = 0;
    private String image = "";
    private String type = "";
    private String age = "";
    private String cart_quantity = "";
    private String stock_quantity = "";

    public CartItem() {
    }

    public CartItem(String user_id, String product_ownerId, String product_id, String title, int price, String image, String type, String age, String cart_quantity) {
        this.user_id = user_id;
        this.product_ownerId = product_ownerId;
        this.product_id = product_id;
        this.title = title;
        this.price = price;
        this.image = image;
        this.type = type;
        this.age = age;
        this.cart_quantity = cart_quantity;
    }


    protected CartItem(Parcel in) {
        id = in.readString();
        user_id = in.readString();
        product_ownerId = in.readString();
        product_id = in.readString();
        title = in.readString();
        price = in.readInt();
        image = in.readString();
        type = in.readString();
        age = in.readString();
        cart_quantity = in.readString();
        stock_quantity = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user_id);
        dest.writeString(product_ownerId);
        dest.writeString(product_id);
        dest.writeString(title);
        dest.writeInt(price);
        dest.writeString(image);
        dest.writeString(type);
        dest.writeString(age);
        dest.writeString(cart_quantity);
        dest.writeString(stock_quantity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };

    public static Creator<CartItem> getCREATOR() {
        return CREATOR;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }


    public String getProduct_ownerId() {
        return product_ownerId;
    }

    public void setProduct_ownerId(String product_ownerId) {
        this.product_ownerId = product_ownerId;
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

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCart_quantity() {
        return cart_quantity;
    }

    public void setCart_quantity(String cart_quantity) {
        this.cart_quantity = cart_quantity;
    }

    public String getStock_quantity() {
        return stock_quantity;
    }

    public void setStock_quantity(String stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", product_ownerId='" + product_ownerId + '\'' +
                ", product_id='" + product_id + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", type='" + type + '\'' +
                ", age='" + age + '\'' +
                ", cart_quantity='" + cart_quantity + '\'' +
                ", stock_quantity='" + stock_quantity + '\'' +
                '}';
    }
}

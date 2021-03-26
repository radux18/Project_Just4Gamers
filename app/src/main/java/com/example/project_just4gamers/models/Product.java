package com.example.project_just4gamers.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {

    private String product_id;
    private String user_id;
    private String user_name;
    private String title;
    private String price;
    private String description;
    private String stock_quantity;
    private String type;
    private String age;
    private String image;

    public Product() {
        this.product_id = "";
        this.user_id = "";
        this.user_name = "";
        this.title = "";
        this.price = "";
        this.description = "";
        this.stock_quantity = "";
        this.type = "";
        this.age = "";
        this.image = "";
    }

    public Product(String user_id, String user_name, String title, String price, String description, String quantity, String type, String age, String image) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.title = title;
        this.price = price;
        this.description = description;
        this.stock_quantity = quantity;
        this.type = type;
        this.age = age;
        this.image = image;
        this.product_id = "";
    }

    public Product(String id, String user_id, String user_name, String title, String price, String description, String quantity, String type, String age, String image) {
        this.product_id = id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.title = title;
        this.price = price;
        this.description = description;
        this.stock_quantity = quantity;
        this.type = type;
        this.age = age;
        this.image = image;
    }

    protected Product(Parcel in) {
        product_id = in.readString();
        user_id = in.readString();
        user_name = in.readString();
        title = in.readString();
        price = in.readString();
        description = in.readString();
        stock_quantity = in.readString();
        type = in.readString();
        age = in.readString();
        image = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(product_id);
        dest.writeString(user_id);
        dest.writeString(user_name);
        dest.writeString(title);
        dest.writeString(price);
        dest.writeString(description);
        dest.writeString(stock_quantity);
        dest.writeString(type);
        dest.writeString(age);
        dest.writeString(image);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStock_quantity() {
        return stock_quantity;
    }

    public void setStock_quantity(String stock_quantity) {
        this.stock_quantity = stock_quantity;
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

    @Override
    public String toString() {
        return "Product{" +
                "product_id='" + product_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                ", stock_quantity ='" + stock_quantity + '\'' +
                ", type='" + type + '\'' +
                ", age='" + age + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}

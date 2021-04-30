package com.example.project_just4gamers.models;


import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String id = "";
    private String firstName = "";
    private String lastName = "";
    private String email = "";
    private String image = "";
    private int points = 0;
    private String gender = "";
    private long mobile = 0;
    private int profileCompleted = 0;
    private int soldProducts = 0;
    private int orders = 0;
    private double latitude = 0.0;
    private double longitude = 0.0;

    public User() {
    }

    public User(String id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.points = 0;
        this.image = "";
        this.gender = "";
        this.mobile = 0;
        this.profileCompleted = 0;
        this.soldProducts = 0;
        this.orders = 0;
        this.latitude = 0.0;
        this.longitude = 0.0;
    }


    protected User(Parcel in) {
        id = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        image = in.readString();
        points = in.readInt();
        gender = in.readString();
        mobile = in.readLong();
        profileCompleted = in.readInt();
        soldProducts = in.readInt();
        orders = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(image);
        dest.writeInt(points);
        dest.writeString(gender);
        dest.writeLong(mobile);
        dest.writeInt(profileCompleted);
        dest.writeInt(soldProducts);
        dest.writeInt(orders);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public long getMobile() {
        return mobile;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }

    public int getProfileCompleted() {
        return profileCompleted;
    }

    public void setProfileCompleted(int profileCompleted) {
        this.profileCompleted = profileCompleted;
    }

    public int getSoldProducts() {
        return soldProducts;
    }

    public void setSoldProducts(int soldProducts) {
        this.soldProducts = soldProducts;
    }

    public static Creator<User> getCREATOR() {
        return CREATOR;
    }

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", image='" + image + '\'' +
                ", points=" + points +
                ", gender='" + gender + '\'' +
                ", mobile=" + mobile +
                ", profileCompleted=" + profileCompleted +
                ", soldProducts=" + soldProducts +
                ", orders=" + orders +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}

package com.example.project_just4gamers.models;

import android.os.Parcel;
import android.os.Parcelable;

public class DiscountCoupon implements Parcelable {

    private String id = "";
    private String user_id = "";
    private String type = "";

    public DiscountCoupon() {
    }

    public DiscountCoupon(String user_id, String type) {
        this.user_id = user_id;
        this.type = type;
    }

    protected DiscountCoupon(Parcel in) {
        id = in.readString();
        user_id = in.readString();
        type = in.readString();
    }

    public static final Creator<DiscountCoupon> CREATOR = new Creator<DiscountCoupon>() {
        @Override
        public DiscountCoupon createFromParcel(Parcel in) {
            return new DiscountCoupon(in);
        }

        @Override
        public DiscountCoupon[] newArray(int size) {
            return new DiscountCoupon[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user_id);
        dest.writeString(type);
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static Creator<DiscountCoupon> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "DiscountCoupon{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

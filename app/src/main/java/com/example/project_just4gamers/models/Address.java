package com.example.project_just4gamers.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {
    private String id = "";

    private String user_id = "";
    private String name = "";
    private String mobileNumber = "";

    private String address = "";
    private String zipCode = "";
    private String additionalNote = "";

    private String type = "";
    private String otherDetails = "";

    public Address() {
    }

    public Address(String user_id, String name, String mobileNumber, String address, String zipCode, String additionalNote, String type, String otherDetails) {
        this.user_id = user_id;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.address = address;
        this.zipCode = zipCode;
        this.additionalNote = additionalNote;
        this.type = type;
        this.otherDetails = otherDetails;
    }

    protected Address(Parcel in) {
        id = in.readString();
        user_id = in.readString();
        name = in.readString();
        mobileNumber = in.readString();
        address = in.readString();
        zipCode = in.readString();
        additionalNote = in.readString();
        type = in.readString();
        otherDetails = in.readString();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
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
        dest.writeString(name);
        dest.writeString(mobileNumber);
        dest.writeString(address);
        dest.writeString(zipCode);
        dest.writeString(additionalNote);
        dest.writeString(type);
        dest.writeString(otherDetails);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAdditionalNote() {
        return additionalNote;
    }

    public void setAdditionalNote(String additionalNote) {
        this.additionalNote = additionalNote;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(String otherDetails) {
        this.otherDetails = otherDetails;
    }

    public static Creator<Address> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", address='" + address + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", additionalNote='" + additionalNote + '\'' +
                ", type='" + type + '\'' +
                ", otherDetails='" + otherDetails + '\'' +
                '}';
    }
}

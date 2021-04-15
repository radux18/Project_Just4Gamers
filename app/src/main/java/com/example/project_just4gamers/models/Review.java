package com.example.project_just4gamers.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {

    private String id = "";
    private String userProfile_id = "";
    private User user = null;
    private String description = "";
    private String title = "";
    private float score = 0;
    private String date = "";

    public Review() {
    }

    public Review(String userProfile_id, User user, String description, String title, float score, String date) {
        this.userProfile_id = userProfile_id;
        this.user = user;
        this.description = description;
        this.title = title;
        this.score = score;
        this.date = date;
    }

    protected Review(Parcel in) {
        id = in.readString();
        userProfile_id = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        description = in.readString();
        title = in.readString();
        score = in.readFloat();
        date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userProfile_id);
        dest.writeParcelable(user, flags);
        dest.writeString(description);
        dest.writeString(title);
        dest.writeFloat(score);
        dest.writeString(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public static Creator<Review> getCREATOR() {
        return CREATOR;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserProfile_id() {
        return userProfile_id;
    }

    public void setUserProfile_id(String userProfile_id) {
        this.userProfile_id = userProfile_id;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id='" + id + '\'' +
                ", userProfile_id='" + userProfile_id + '\'' +
                ", user=" + user +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", score=" + score +
                ", date='" + date + '\'' +
                '}';
    }
}

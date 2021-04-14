package com.example.project_just4gamers.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {

    private String id = "";
    private String user_id = "";
    private String description = "";
    private String title = "";
    private int score = 0;

    public Review(String user_id, String description, String title, int score) {
        this.user_id = user_id;
        this.description = description;
        this.title = title;
        this.score = score;
    }

    protected Review(Parcel in) {
        id = in.readString();
        user_id = in.readString();
        description = in.readString();
        title = in.readString();
        score = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user_id);
        dest.writeString(description);
        dest.writeString(title);
        dest.writeInt(score);
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", score=" + score +
                '}';
    }
}

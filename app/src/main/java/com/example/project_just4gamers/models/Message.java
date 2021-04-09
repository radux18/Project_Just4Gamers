package com.example.project_just4gamers.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable {

    private String id = "";
    private String sender_id = "";
    private String receiver_id = "";
    private String description = "";
    private String title = "";
    private String date = "";

    public Message() {
    }

    public Message(String sender_id, String receiver_id, String description, String title, String date) {
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.description = description;
        this.title = title;
        this.date = date;
    }


    protected Message(Parcel in) {
        id = in.readString();
        sender_id = in.readString();
        receiver_id = in.readString();
        description = in.readString();
        title = in.readString();
        date = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public static Creator<Message> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", sender_id='" + sender_id + '\'' +
                ", receiver_id='" + receiver_id + '\'' +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(sender_id);
        dest.writeString(receiver_id);
        dest.writeString(description);
        dest.writeString(title);
        dest.writeString(date);
    }
}

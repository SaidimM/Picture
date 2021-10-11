package com.example.picture.main.data.bean;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Download implements Parcelable {
    public static int IMAGE = 1;
    public static int VIDEO = 2;
    public static int AUDIO = 3;
    public static int STARTED = 1;
    public static int STOPPED = 2;
    public static int DOWNLOADING = 3;
    public static int ERROR = 4;
    public static int FINISH = 5;

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int type;
    private String link;
    private int state;
    private String capacity;
    private String path;
    @ColumnInfo(name = "user_id")
    private int userID;
    @ColumnInfo(name = "file_id")
    private String fileID;
    @ColumnInfo(name = "created_time")
    private String createdTime;
    public Download(){}
    protected Download(Parcel in) {
        id = in.readInt();
        type = in.readInt();
        link = in.readString();
        state = in.readInt();
        capacity = in.readString();
        path = in.readString();
        userID = in.readInt();
        fileID = in.readString();
        createdTime = in.readString();
    }

    public static final Creator<Download> CREATOR = new Creator<Download>() {
        @Override
        public Download createFromParcel(Parcel in) {
            return new Download(in);
        }

        @Override
        public Download[] newArray(int size) {
            return new Download[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFileID() {
        return fileID;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(type);
        dest.writeString(link);
        dest.writeInt(state);
        dest.writeString(capacity);
        dest.writeString(path);
        dest.writeInt(userID);
        dest.writeString(fileID);
        dest.writeString(createdTime);
    }
}

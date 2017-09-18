package com.example.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by plnc on 2017-05-31.
 */

public class UserEntity {

    @SerializedName("id")
    private int userId;

    @SerializedName("cover_url")
    private String coverUrl;

    @SerializedName("full_name")
    private String fullname;

    @SerializedName("description")
    private String description;

    @SerializedName("followers")
    private int followers;

    @SerializedName("email")
    private String email;

    public UserEntity() {

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCoverUrl() {
        return coverUrl;
    }


    public String getFullname() {
        return fullname;
    }

    public String getDescription() {
        return description;
    }

    public int getFollowers() {
        return followers;
    }

    public String getEmail() {
        return email;
    }
}

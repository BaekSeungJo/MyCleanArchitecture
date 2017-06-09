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

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserEntity{");
        sb.append("userId=").append(userId);
        sb.append(", coverUrl='").append(coverUrl).append('\'');
        sb.append(", fullname='").append(fullname).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", followers=").append(followers);
        sb.append(", email='").append(email).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

package com.itheamc.meatprocessing.models.external;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;

public class Users {
    private String userId;
    private String userName;
    private String userProfile;
    private String userPhone;
    private String userEmail;
    private String userAddress;
    private Double locLat;
    private Double locLong;

    // Constructor
    public Users() {
    }

    // Constructor with parameters
    public Users(String userId, String userName, String userProfile, String userPhone, String userEmail, String userAddress, Double locLat, Double locLong) {
        this.userId = userId;
        this.userName = userName;
        this.userProfile = userProfile;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userAddress = userAddress;
        this.locLat = locLat;
        this.locLong = locLong;
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public Double getLocLat() {
        return locLat;
    }

    public Double getLocLong() {
        return locLong;
    }

    // Overriding toString() method
    @Override
    public String toString() {
        return "Users{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userProfile='" + userProfile + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", locLat=" + locLat +
                ", locLong=" + locLong +
                '}';
    }

    @BindingAdapter("android:setUserImage")
    public static void loadImage(ImageView imageView, String imageUrl) {
        Picasso.get()
                .load(imageUrl)
                .into(imageView);
    }
}

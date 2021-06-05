package com.itheamc.meatprocessing.models.external;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class Distributions {
    private String distributionId;
    private String distributionName;
    private String distributionAddress;
    private String locLat;
    private String locLong;
    private String phoneNumber;
    private String emailAddress;
    private String managerName;
    private String managerPhone;
    private String managerEmail;
    private String managerAddress;

    // Empty Constructor
    public Distributions() {
    }

    // Constructor with parameters
    public Distributions(String distributionId, String distributionName, String distributionAddress, String locLat, String locLong, String phoneNumber, String emailAddress, String managerName, String managerPhone, String managerEmail, String managerAddress) {
        this.distributionId = distributionId;
        this.distributionName = distributionName;
        this.distributionAddress = distributionAddress;
        this.locLat = locLat;
        this.locLong = locLong;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.managerName = managerName;
        this.managerPhone = managerPhone;
        this.managerEmail = managerEmail;
        this.managerAddress = managerAddress;
    }

    // Getters

    public String getDistributionId() {
        return distributionId;
    }

    public String getDistributionName() {
        return distributionName;
    }

    public String getDistributionAddress() {
        return distributionAddress;
    }

    public String getLocLat() {
        return locLat;
    }

    public String getLocLong() {
        return locLong;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getManagerName() {
        return managerName;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public String getManagerAddress() {
        return managerAddress;
    }


    // To String Method
    @Override
    public String toString() {
        return "Distributions{" +
                "distributionId='" + distributionId + '\'' +
                ", distributionName='" + distributionName + '\'' +
                ", distributionAddress='" + distributionAddress + '\'' +
                ", locLat='" + locLat + '\'' +
                ", locLong='" + locLong + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", managerName='" + managerName + '\'' +
                ", managerPhone='" + managerPhone + '\'' +
                ", managerEmail='" + managerEmail + '\'' +
                ", managerAddress='" + managerAddress + '\'' +
                '}';
    }

    // equals() Method
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distributions that = (Distributions) o;
        return Objects.equals(getDistributionId(), that.getDistributionId()) &&
                Objects.equals(getDistributionName(), that.getDistributionName()) &&
                Objects.equals(getDistributionAddress(), that.getDistributionAddress()) &&
                Objects.equals(getLocLat(), that.getLocLat()) &&
                Objects.equals(getLocLong(), that.getLocLong()) &&
                Objects.equals(getPhoneNumber(), that.getPhoneNumber()) &&
                Objects.equals(getEmailAddress(), that.getEmailAddress()) &&
                Objects.equals(getManagerName(), that.getManagerName()) &&
                Objects.equals(getManagerPhone(), that.getManagerPhone()) &&
                Objects.equals(getManagerEmail(), that.getManagerEmail()) &&
                Objects.equals(getManagerAddress(), that.getManagerAddress());
    }


    // Creating DiffUtil.ItemCallBack object
    public static DiffUtil.ItemCallback<Distributions> distributionsItemCallback = new DiffUtil.ItemCallback<Distributions>() {
        @Override
        public boolean areItemsTheSame(@NonNull Distributions oldItem, @NonNull Distributions newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Distributions oldItem, @NonNull Distributions newItem) {
            return false;
        }
    };
}

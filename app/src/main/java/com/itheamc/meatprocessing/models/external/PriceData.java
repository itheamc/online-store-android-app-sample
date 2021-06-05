package com.itheamc.meatprocessing.models.external;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class PriceData {
    private String date;
    private String productId;
    private Double productPrice;
    private Double changePercentage;

    // Constructor
    public PriceData() {
    }


    // Constructor with parameters
    public PriceData(String date, String productId, Double productPrice, Double changePercentage) {
        this.date = date;
        this.productId = productId;
        this.productPrice = productPrice;
        this.changePercentage = changePercentage;
    }


    // Getters
    public String getDate() {
        return date;
    }

    public String getProductId() {
        return productId;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public Double getChangePercentage() {
        return changePercentage;
    }


    // Overriding toString() Method
    @Override
    public String toString() {
        return "PriceData{" +
                "date='" + date + '\'' +
                ", productId='" + productId + '\'' +
                ", productPrice='" + productPrice + '\'' +
                ", changePercentage=" + changePercentage +
                '}';
    }


    // Overriding equals() method
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceData priceData = (PriceData) o;
        return Objects.equals(getDate(), priceData.getDate()) &&
                Objects.equals(getProductId(), priceData.getProductId()) &&
                Objects.equals(getProductPrice(), priceData.getProductPrice()) &&
                Objects.equals(getChangePercentage(), priceData.getChangePercentage());
    }


    // Creating DiffUtils.ItemCallBack object for the Price Data
    public static DiffUtil.ItemCallback<PriceData> priceDataItemCallback = new DiffUtil.ItemCallback<PriceData>() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public boolean areItemsTheSame(@NonNull PriceData oldItem, @NonNull PriceData newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull PriceData oldItem, @NonNull PriceData newItem) {
            return false;
        }
    };

}

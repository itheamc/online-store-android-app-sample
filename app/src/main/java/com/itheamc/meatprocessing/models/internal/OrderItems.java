package com.itheamc.meatprocessing.models.internal;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;
/**
 * This class is made for confirmed and underprocess order item
  */

public class OrderItems {
    private String itemId;
    private String itemName;
    private double itemPrice;
    private double itemQuantity;
    private double itemAmount;

    // Constructor without parameters
    public OrderItems() {
    }

    // Constructor with parameters
    public OrderItems(String itemId, String itemName, double itemPrice, double itemQuantity, double itemAmount) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.itemAmount = itemAmount;
    }

    // Getters and Setters
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public double getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(double itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public double getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(double itemAmount) {
        this.itemAmount = itemAmount;
    }

    // Overriding tostring() method for debugging purpose
    @Override
    public String toString() {
        return "OrderItems{" +
                "itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemPrice=" + itemPrice +
                ", itemQuantity=" + itemQuantity +
                ", itemAmount=" + itemAmount +
                '}';
    }

    //Overriding equals() method
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItems that = (OrderItems) o;
        return Double.compare(that.getItemPrice(), getItemPrice()) == 0 &&
                Double.compare(that.getItemQuantity(), getItemQuantity()) == 0 &&
                Double.compare(that.getItemAmount(), getItemAmount()) == 0 &&
                Objects.equals(getItemId(), that.getItemId()) &&
                Objects.equals(getItemName(), that.getItemName());
    }

    // Creating DiffUtils.ItemCallBack object
    public static DiffUtil.ItemCallback<OrderItems> orderItemsItemCallback = new DiffUtil.ItemCallback<OrderItems>() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public boolean areItemsTheSame(@NonNull OrderItems oldItem, @NonNull OrderItems newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull OrderItems oldItem, @NonNull OrderItems newItem) {
            return oldItem.getItemId().equals(newItem.getItemId()) &&
                    oldItem.getItemName().equals(newItem.getItemName()) &&
                    oldItem.getItemPrice() == newItem.getItemPrice() &&
                    oldItem.getItemQuantity() == newItem.getItemQuantity() &&
                    oldItem.getItemAmount() == newItem.getItemAmount();
        }
    };
}

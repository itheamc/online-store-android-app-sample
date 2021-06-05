package com.itheamc.meatprocessing.models.internal;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;

import com.itheamc.meatprocessing.models.external.Products;

import java.util.Objects;

public class CartItems {
    private String cartItemId;
    private Products product;
    private double orderedQuantity;

    // Empty Constructors
    public CartItems() {
    }


    // Constructor with parameters
    public CartItems(String cartItemId, Products product, double orderedQuantity) {
        this.cartItemId = cartItemId;
        this.product = product;
        this.orderedQuantity = orderedQuantity;
    }


    // Getters and Setters
    public String getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(String cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public double getOrderedQuantity() {
        return orderedQuantity;
    }

    public void setOrderedQuantity(double orderedQuantity) {
        this.orderedQuantity = orderedQuantity;
    }

    // Overriding toString() method
    @Override
    public String toString() {
        return "CartItems{" +
                "cartItemId='" + cartItemId + '\'' +
                ", product=" + product +
                ", orderedQuantity=" + orderedQuantity +
                '}';
    }


    // Overriding equals() method
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItems cartItems = (CartItems) o;
        return Double.compare(cartItems.getOrderedQuantity(), getOrderedQuantity()) == 0 &&
                Objects.equals(getCartItemId(), cartItems.getCartItemId()) &&
                Objects.equals(getProduct(), cartItems.getProduct());
    }


    // Creating DiffUtils.ItemCallBack object for CartItem
    public static DiffUtil.ItemCallback<CartItems> cartItemsItemCallback = new DiffUtil.ItemCallback<CartItems>() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public boolean areItemsTheSame(@NonNull CartItems oldItem, @NonNull CartItems newItem) {
            return oldItem.equals(newItem);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public boolean areContentsTheSame(@NonNull CartItems oldItem, @NonNull CartItems newItem) {
            return oldItem.getCartItemId().equals(newItem.getCartItemId()) &&
                    oldItem.getProduct().getProductId().equals(newItem.getProduct().getProductId()) &&
                    oldItem.getOrderedQuantity() == newItem.getOrderedQuantity();
        }
    };
}

package com.itheamc.meatprocessing.models.external;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;
import java.util.Objects;

/**
 * This model class is created to store order data on the FirebaseFirestore
 * To handle the unconfirmed, underprocess, ontheway and history fragment
 * another model classses have been created which usess the data from this class also:
 * they are: OrderItems and
 */
public class Order {
    private String orderId;
    private List<String> itemsId;
    private List<Double> orderedAmount;
    private List<Double> orderedQuantity;
    private String userId;
    private String paymentMethod;
    private String orderStatus;
    private double locLat;
    private double locLong;
    private boolean paid;
    private String deliverdBy;
    private String rejectionReason;

    // Constructor
    public Order() {
    }

    // Constructor with parameters


    public Order(String orderId, List<String> itemsId, List<Double> orderedAmount, List<Double> orderedQuantity, String userId, String paymentMethod, String orderStatus, double locLat, double locLong, boolean paid, String deliverdBy, String rejectionReason) {
        this.orderId = orderId;
        this.itemsId = itemsId;
        this.orderedAmount = orderedAmount;
        this.orderedQuantity = orderedQuantity;
        this.userId = userId;
        this.paymentMethod = paymentMethod;
        this.orderStatus = orderStatus;
        this.locLat = locLat;
        this.locLong = locLong;
        this.paid = paid;
        this.deliverdBy = deliverdBy;
        this.rejectionReason = rejectionReason;
    }

    // Getters


    public String getOrderId() {
        return orderId;
    }

    public List<String> getItemsId() {
        return itemsId;
    }

    public List<Double> getOrderedAmount() {
        return orderedAmount;
    }

    public List<Double> getOrderedQuantity() {
        return orderedQuantity;
    }

    public String getUserId() {
        return userId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public double getLocLat() {
        return locLat;
    }

    public double getLocLong() {
        return locLong;
    }

    public boolean isPaid() {
        return paid;
    }

    public String getDeliverdBy() {
        return deliverdBy;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    // Overriding toString() method
    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", itemsId=" + itemsId +
                ", orderedAmount=" + orderedAmount +
                ", orderedQuantity=" + orderedQuantity +
                ", userId='" + userId + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", locLat=" + locLat +
                ", locLong=" + locLong +
                ", paid=" + paid +
                ", deliverdBy='" + deliverdBy + '\'' +
                ", rejectionReason='" + rejectionReason + '\'' +
                '}';
    }

    // Overriding equals() method
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Double.compare(order.getLocLat(), getLocLat()) == 0 &&
                Double.compare(order.getLocLong(), getLocLong()) == 0 &&
                isPaid() == order.isPaid() &&
                getOrderId().equals(order.getOrderId()) &&
                getItemsId().equals(order.getItemsId()) &&
                getOrderedAmount().equals(order.getOrderedAmount()) &&
                getOrderedQuantity().equals(order.getOrderedQuantity()) &&
                getUserId().equals(order.getUserId()) &&
                Objects.equals(getPaymentMethod(), order.getPaymentMethod()) &&
                getOrderStatus().equals(order.getOrderStatus()) &&
                Objects.equals(getDeliverdBy(), order.getDeliverdBy()) &&
                Objects.equals(getRejectionReason(), order.getRejectionReason());
    }


    // Creating DiffUtils.ItemCallBack object for Order class items
    public static DiffUtil.ItemCallback<Order> orderItemCallback = new DiffUtil.ItemCallback<Order>() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public boolean areItemsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
            return oldItem.getOrderId().equals(newItem.getOrderId()) &&
                    oldItem.getItemsId().equals(newItem.getItemsId()) &&
                    oldItem.getUserId().equals(newItem.getUserId()) &&
                    oldItem.getPaymentMethod().equals(newItem.getPaymentMethod()) &&
                    oldItem.getOrderStatus().equals(newItem.getOrderStatus()) &&
                    oldItem.getLocLat() == newItem.getLocLat() &&
                    oldItem.getLocLong() == newItem.getLocLong() &&
                    oldItem.getOrderedAmount().equals(newItem.getOrderedAmount()) &&
                    oldItem.getOrderedQuantity().equals(newItem.getOrderedQuantity()) &&
                    oldItem.isPaid() == newItem.isPaid() &&
                    oldItem.getDeliverdBy().equals(newItem.getDeliverdBy()) &&
                    oldItem.getRejectionReason().equals(newItem.getRejectionReason());
        }
    };


}

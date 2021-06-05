package com.itheamc.meatprocessing.models.external;

import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class Products {
    private String productId;
    private String productName;
    private List<String> productImages;
    private List<String> availableQuantities;
    private String productCategory;
    private double productPrice;
    private double productQuantity;
    private double totalSold;
    private boolean available;
    private boolean prebooking;
    private String productUnit;

    // Empty Constructor
    public Products() {
    }

    // Constructor with parameters
    public Products(String productId, String productName, List<String> productImages, List<String> availableQuantities, String productCategory, double productPrice, double productQuantity, double totalSold, boolean available, boolean prebooking, String productUnit) {
        this.productId = productId;
        this.productName = productName;
        this.productImages = productImages;
        this.availableQuantities = availableQuantities;
        this.productCategory = productCategory;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.totalSold = totalSold;
        this.available = available;
        this.prebooking = prebooking;
        this.productUnit = productUnit;
    }

    // Getters
    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public List<String> getProductImages() {
        return productImages;
    }

    public List<String> getAvailableQuantities() {
        return availableQuantities;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public double getProductQuantity() {
        return productQuantity;
    }

    public double getTotalSold() {
        return totalSold;
    }

    public boolean isAvailable() {
        return available;
    }

    public boolean isPrebooking() {
        return prebooking;
    }

    public String getProductUnit() {
        return productUnit;
    }

    // Overriding toString() method just for debugging purpose

    @Override
    public String toString() {
        return "Products{" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", productImages=" + productImages +
                ", availableQuantities=" + availableQuantities +
                ", productCategory='" + productCategory + '\'' +
                ", productPrice=" + productPrice +
                ", productQuantity=" + productQuantity +
                ", totalSold=" + totalSold +
                ", isAvailable=" + available +
                ", isPrebooking=" + prebooking +
                ", productUnit='" + productUnit + '\'' +
                '}';
    }

    // Overriding equals() method
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Products products = (Products) o;
        return Double.compare(products.getProductPrice(), getProductPrice()) == 0 &&
                Double.compare(products.getProductQuantity(), getProductQuantity()) == 0 &&
                Double.compare(products.getTotalSold(), getTotalSold()) == 0 &&
                isAvailable() == products.isAvailable() &&
                isPrebooking() == products.isPrebooking() &&
                Objects.equals(getProductId(), products.getProductId()) &&
                Objects.equals(getProductName(), products.getProductName()) &&
                Objects.equals(getProductImages(), products.getProductImages()) &&
                Objects.equals(getAvailableQuantities(), products.getAvailableQuantities()) &&
                Objects.equals(getProductCategory(), products.getProductCategory()) &&
                Objects.equals(getProductUnit(), products.getProductUnit());
    }


    // Creating DiffUtil.ItemCallBack object
    public static DiffUtil.ItemCallback<Products> productsItemCallback = new DiffUtil.ItemCallback<Products>() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public boolean areItemsTheSame(@NonNull Products oldItem, @NonNull Products newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Products oldItem, @NonNull Products newItem) {
            return oldItem.getProductId().equals(newItem.getProductId()) &&
                    oldItem.getProductName().equals(newItem.getProductName()) &&
                    oldItem.getProductImages().size() == newItem.getProductImages().size() &&
                    oldItem.getProductCategory().equals(newItem.getProductCategory()) &&
                    oldItem.getProductPrice() == newItem.getProductPrice() &&
                    oldItem.getProductQuantity() == newItem.getProductQuantity() &&
                    oldItem.getTotalSold() == newItem.getTotalSold() &&
                    oldItem.isAvailable() == newItem.isAvailable() &&
                    oldItem.isPrebooking() == newItem.isPrebooking() &&
                    oldItem.getProductUnit().equals(newItem.getProductUnit()) &&
                    oldItem.getAvailableQuantities().equals(newItem.getAvailableQuantities());
        }
    };

    // Image Binding Adapter
    @BindingAdapter("android:imageSource")
    public static void setImage(ImageView imageView, String imageUrl) {
        Picasso.get()
                .load(imageUrl)
                .into(imageView);
    }
}

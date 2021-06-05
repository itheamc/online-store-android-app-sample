package com.itheamc.meatprocessing.models.external;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class Categories {
    private String categoryId;
    private String categoryName;
    private String categoryImage;

    // Empty Constructor
    public Categories() {
    }

    // Constructor with parameters
    public Categories(String categoryId, String categoryName, String categoryImage) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
    }


    // Getters
    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    // Overriding toString() method
    @Override
    public String toString() {
        return "Categories{" +
                "categoryId='" + categoryId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", categoryImage='" + categoryImage + '\'' +
                '}';
    }

    // Overriding equals() method
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categories that = (Categories) o;
        return Objects.equals(getCategoryId(), that.getCategoryId()) &&
                Objects.equals(getCategoryName(), that.getCategoryName()) &&
                Objects.equals(getCategoryImage(), that.getCategoryImage());
    }

    // Creating DiffUtil.ItemCallBack object for recyclerview
    public static DiffUtil.ItemCallback<Categories> categoriesItemCallback = new DiffUtil.ItemCallback<Categories>() {
        @Override
        public boolean areItemsTheSame(@NonNull Categories oldItem, @NonNull Categories newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Categories oldItem, @NonNull Categories newItem) {
            return false;
        }
    };
}

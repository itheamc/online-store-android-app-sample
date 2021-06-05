package com.itheamc.meatprocessing.models.internal;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;
import java.util.Objects;

public class HomeItems {
    private int itemId;
    private String itemName;
    private List<?> itemList;

    // Empty Constructor
    public HomeItems() {
    }


    // Constructor with parameters
    public HomeItems(int itemId, String itemName, List<?> itemList) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemList = itemList;
    }


    // Getters
    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public List<?> getItemList() {
        return itemList;
    }


    // Overriding toString() method
    @Override
    public String toString() {
        return "HomeItems{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", itemList=" + itemList +
                '}';
    }

    // Overriding equals() method
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HomeItems homeItems = (HomeItems) o;
        return getItemId() == homeItems.getItemId() &&
                Objects.equals(getItemName(), homeItems.getItemName()) &&
                Objects.equals(getItemList(), homeItems.getItemList());
    }


    // Creating DiffUtils.ItemCallBack object for the class HomeItems
    public static DiffUtil.ItemCallback<HomeItems> homeItemsItemCallback = new DiffUtil.ItemCallback<HomeItems>() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public boolean areItemsTheSame(@NonNull HomeItems oldItem, @NonNull HomeItems newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull HomeItems oldItem, @NonNull HomeItems newItem) {
            return oldItem.getItemId() == newItem.getItemId() &&
                    oldItem.getItemList().equals(newItem.getItemList()) &&
                    oldItem.getItemName().equals(newItem.getItemName());
        }
    };
}

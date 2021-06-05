package com.itheamc.meatprocessing.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itheamc.meatprocessing.databinding.ProductImageSliderViewBinding;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class ImageSliderAdapter extends SliderViewAdapter<ImageSliderAdapter.ImageViewHolder> {
    private List<String> imageList;

    // Constructor
    public ImageSliderAdapter(List<String> imageList) {
        this.imageList = imageList;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ProductImageSliderViewBinding imageSliderViewBinding = ProductImageSliderViewBinding.inflate(inflater, parent, false);
        return new ImageViewHolder(imageSliderViewBinding);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder viewHolder, int position) {
        String imageUrl = imageList.get(position);
        viewHolder.imageSliderViewBinding.setImage(imageUrl);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    public static class ImageViewHolder extends SliderViewAdapter.ViewHolder {
        ProductImageSliderViewBinding imageSliderViewBinding;

        public ImageViewHolder(ProductImageSliderViewBinding imageSliderViewBinding) {
            super(imageSliderViewBinding.getRoot());
            this.imageSliderViewBinding = imageSliderViewBinding;
        }
    }
}

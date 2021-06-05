package com.itheamc.meatprocessing.models.external;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;

public class Slider {
    private String sliderId;
    private String sliderImage;
    private boolean isVisible;

    // Empty Constructor
    public Slider() {
    }

    // Constructor with parameters

    public Slider(String sliderId, String sliderImage, boolean isVisible) {
        this.sliderId = sliderId;
        this.sliderImage = sliderImage;
        this.isVisible = isVisible;
    }


    // Getters and Setters
    public String getSliderId() {
        return sliderId;
    }

    public void setSliderId(String sliderId) {
        this.sliderId = sliderId;
    }

    public String getSliderImage() {
        return sliderImage;
    }

    public void setSliderImage(String sliderImage) {
        this.sliderImage = sliderImage;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    // Binding adapter for image setting
    @BindingAdapter("android:setSliderImage")
    public static void setSliderImage(ImageView imageView, String imageUrl) {
        Picasso.get()
                .load(imageUrl)
                .into(imageView);
    }
}

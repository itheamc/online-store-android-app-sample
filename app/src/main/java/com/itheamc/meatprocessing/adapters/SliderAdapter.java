package com.itheamc.meatprocessing.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.itheamc.meatprocessing.databinding.SliderItemViewBinding;
import com.itheamc.meatprocessing.models.external.Slider;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderVH> {
    private static final String TAG = "SliderAdapter";
    //    private List<Slider> sliderList;
    private List<?> sliderList;

    public SliderAdapter(List<?> sliderList) {
        this.sliderList = sliderList;
    }


    @Override
    public SliderVH onCreateViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SliderItemViewBinding sliderItemBinding = SliderItemViewBinding.inflate(inflater, parent, false);
        return new SliderVH(sliderItemBinding);
    }

    @Override
    public void onBindViewHolder(SliderVH viewHolder, int position) {
        Slider slider = (Slider) sliderList.get(position);
        viewHolder.sliderItemBinding.setSlider(slider);

    }

    @Override
    public int getCount() {
        return sliderList.size();
    }

    public static class SliderVH extends SliderViewAdapter.ViewHolder {
        SliderItemViewBinding sliderItemBinding;

        public SliderVH(SliderItemViewBinding sliderItemBinding) {
            super(sliderItemBinding.getRoot());
            this.sliderItemBinding = sliderItemBinding;
        }
    }
}

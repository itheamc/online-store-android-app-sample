package com.itheamc.meatprocessing.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.itheamc.meatprocessing.databinding.InfoViewBinding;
import com.itheamc.meatprocessing.databinding.ItemViewBinding;
import com.itheamc.meatprocessing.databinding.SliderViewBinding;
import com.itheamc.meatprocessing.interfaces.HomeAdapterInterface;
import com.itheamc.meatprocessing.models.internal.HomeItems;
import com.itheamc.meatprocessing.models.external.Info;
import com.itheamc.meatprocessing.models.external.Products;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;

import java.util.List;

import static com.itheamc.meatprocessing.models.internal.HomeItems.homeItemsItemCallback;
import static com.itheamc.meatprocessing.variables.Constants.INFO_CARD_VIEW;
import static com.itheamc.meatprocessing.variables.Constants.PREBOOKED_VIEW;
import static com.itheamc.meatprocessing.variables.Constants.PRODUCT_VIEW;
import static com.itheamc.meatprocessing.variables.Constants.SLIDER_VIEW;

public class HomeAdapter extends ListAdapter<HomeItems, RecyclerView.ViewHolder> {
    private final HomeAdapterInterface homeAdapterInterface;
    private Context hContext;

    public HomeAdapter(@NonNull HomeAdapterInterface homeAdapterInterface, Context context) {
        super(homeItemsItemCallback);
        this.homeAdapterInterface = homeAdapterInterface;
        this.hContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        HomeItems homeItem = getItem(position);
        int viewType = 0;
        switch (homeItem.getItemId()) {
            case SLIDER_VIEW:
                viewType = SLIDER_VIEW;
                break;
            case PREBOOKED_VIEW:
                viewType = PREBOOKED_VIEW;
                break;
            case INFO_CARD_VIEW:
                viewType = INFO_CARD_VIEW;
                break;
            case PRODUCT_VIEW:
                viewType = PRODUCT_VIEW;
                break;
        }

        return viewType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case SLIDER_VIEW:
                SliderViewBinding sliderViewBinding = SliderViewBinding.inflate(inflater, parent, false);
                return new SliderViewHolder(sliderViewBinding);

            case PREBOOKED_VIEW:
                ItemViewBinding itemViewBinding = ItemViewBinding.inflate(inflater, parent, false);
                return new PrebookViewHolder(itemViewBinding);

            case INFO_CARD_VIEW:
                InfoViewBinding infoViewBinding = InfoViewBinding.inflate(inflater, parent, false);
                return new InfoViewHolder(infoViewBinding);

            case PRODUCT_VIEW:
                ItemViewBinding productViewBinding = ItemViewBinding.inflate(inflater, parent, false);
                return new ProductViewHolder(productViewBinding);

            default:
                InfoViewBinding infoViewBinding1 = InfoViewBinding.inflate(inflater, parent, false);
                return new InfoViewHolder(infoViewBinding1);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HomeItems homeItem = getItem(position);
        switch (homeItem.getItemId()) {
            case SLIDER_VIEW:
                SliderAdapter sliderAdapter = new SliderAdapter(homeItem.getItemList());
                ((SliderViewHolder) holder).sliderViewBinding.imageSlider.setSliderAdapter(sliderAdapter);
                ((SliderViewHolder) holder).sliderViewBinding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM);
                ((SliderViewHolder) holder).sliderViewBinding.imageSlider.setSliderTransformAnimation(SliderAnimations.GATETRANSFORMATION);
                break;

            case PREBOOKED_VIEW:
                ((PrebookViewHolder) holder).itemViewBinding.setHomeitem(homeItem);
//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(hContext, LinearLayoutManager.HORIZONTAL, false);
//                ((PrebookViewHolder) holder).itemViewBinding.itemRecyclerView.setLayoutManager(layoutManager);
                PreBookAdapter prebookAdapter = new PreBookAdapter(homeAdapterInterface);
                ((PrebookViewHolder) holder).itemViewBinding.itemRecyclerView.setAdapter(prebookAdapter);
                prebookAdapter.submitList((List<Products>) homeItem.getItemList());
                break;

            case INFO_CARD_VIEW:
                if (!homeItem.getItemList().isEmpty()) {
                    ((InfoViewHolder) holder).infoViewBinding.setInfo((Info) homeItem.getItemList().get(0));
                }
                break;

            case PRODUCT_VIEW:
                ((ProductViewHolder) holder).itemViewBinding.setHomeitem(homeItem);
//                RecyclerView.LayoutManager verticleLayoutManager = new LinearLayoutManager(hContext, LinearLayoutManager.VERTICAL, false);
//                ((ProductViewHolder) holder).itemViewBinding.itemRecyclerView.setLayoutManager(verticleLayoutManager);
                ProductAdapter productAdapter = new ProductAdapter(homeAdapterInterface);
                ((ProductViewHolder) holder).itemViewBinding.itemRecyclerView.setAdapter(productAdapter);
                productAdapter.submitList((List<Products>) homeItem.getItemList());
                break;
        }

    }

    // ViewHolder class for the price view
    public static class PriceViewHolder extends RecyclerView.ViewHolder {
        ItemViewBinding itemViewBinding;

        public PriceViewHolder(@NonNull ItemViewBinding itemViewBinding) {
            super(itemViewBinding.getRoot());
            this.itemViewBinding = itemViewBinding;
        }
    }

    // ViewHolder class for the Product view
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ItemViewBinding itemViewBinding;

        public ProductViewHolder(@NonNull ItemViewBinding itemViewBinding) {
            super(itemViewBinding.getRoot());
            this.itemViewBinding = itemViewBinding;
        }
    }

    // ViewHolder class for the Slider view
    public static class SliderViewHolder extends RecyclerView.ViewHolder {
        SliderViewBinding sliderViewBinding;

        public SliderViewHolder(@NonNull SliderViewBinding sliderViewBinding) {
            super(sliderViewBinding.getRoot());
            this.sliderViewBinding = sliderViewBinding;
        }
    }

    // ViewHolder class for the Info view
    public static class InfoViewHolder extends RecyclerView.ViewHolder {
        InfoViewBinding infoViewBinding;

        public InfoViewHolder(@NonNull InfoViewBinding infoViewBinding) {
            super(infoViewBinding.getRoot());
            this.infoViewBinding = infoViewBinding;
        }
    }

    // ViewHolder class for the Prebook view
    public static class PrebookViewHolder extends RecyclerView.ViewHolder {
        ItemViewBinding itemViewBinding;

        public PrebookViewHolder(@NonNull ItemViewBinding itemViewBinding) {
            super(itemViewBinding.getRoot());
            this.itemViewBinding = itemViewBinding;
        }
    }
}

package com.itheamc.meatprocessing.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.itheamc.meatprocessing.databinding.PrebookViewBinding;
import com.itheamc.meatprocessing.interfaces.HomeAdapterInterface;
import com.itheamc.meatprocessing.models.external.Products;

import static com.itheamc.meatprocessing.models.external.Products.productsItemCallback;
import static com.itheamc.meatprocessing.variables.Constants.PREBOOK_TYPE;

public class PreBookAdapter extends ListAdapter<Products, PreBookAdapter.PrebookVH> {
    private HomeAdapterInterface homeAdapterInterface;

    public PreBookAdapter(@NonNull HomeAdapterInterface homeAdapterInterface) {
        super(productsItemCallback);
        this.homeAdapterInterface = homeAdapterInterface;
    }

    @NonNull
    @Override
    public PrebookVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        PrebookViewBinding prebookViewBinding = PrebookViewBinding.inflate(inflater, parent, false);
        return new PrebookVH(prebookViewBinding, homeAdapterInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull PrebookVH holder, int position) {
        Products product = getItem(position);
        holder.prebookViewBinding.setProduct(product);

    }

    public static class PrebookVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        PrebookViewBinding prebookViewBinding;
        HomeAdapterInterface homeAdapterInterface;

        public PrebookVH(@NonNull PrebookViewBinding prebookViewBinding, HomeAdapterInterface homeAdapterInterface) {
            super(prebookViewBinding.getRoot());
            this.prebookViewBinding = prebookViewBinding;
            this.homeAdapterInterface = homeAdapterInterface;
            this.prebookViewBinding.prebookButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            homeAdapterInterface.onItemClicked(getAdapterPosition(), PREBOOK_TYPE);
        }
    }
}

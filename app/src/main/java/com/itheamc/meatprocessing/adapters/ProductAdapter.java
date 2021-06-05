package com.itheamc.meatprocessing.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.itheamc.meatprocessing.databinding.ProductViewBinding;
import com.itheamc.meatprocessing.interfaces.HomeAdapterInterface;
import com.itheamc.meatprocessing.models.external.Products;

import static com.itheamc.meatprocessing.models.external.Products.productsItemCallback;
import static com.itheamc.meatprocessing.variables.Constants.REGULAR_TYPE;

public class ProductAdapter extends ListAdapter<Products, ProductAdapter.ProductViewHolder> {
    private static final String TAG = "ProductAdapter";
    private final HomeAdapterInterface adapterInterface;

    public ProductAdapter(HomeAdapterInterface homeAdapterInterface) {
        super(productsItemCallback);
        this.adapterInterface = homeAdapterInterface;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ProductViewBinding productViewBinding = ProductViewBinding.inflate(inflater, parent, false);
        return new ProductViewHolder(productViewBinding, adapterInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Binding data");
        Products product = getItem(position);
        holder.productViewBinding.setProduct(product);
        holder.productViewBinding.availabilityStatus.setVisibility(product.isAvailable() ? View.GONE : View.VISIBLE);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        HomeAdapterInterface adapterInterface;
        ProductViewBinding productViewBinding;

        public ProductViewHolder(@NonNull ProductViewBinding productViewBinding, HomeAdapterInterface homeAdapterInterface) {
            super(productViewBinding.getRoot());
            this.adapterInterface = homeAdapterInterface;
            this.productViewBinding = productViewBinding;
            this.productViewBinding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            adapterInterface.onItemClicked(getAdapterPosition(), REGULAR_TYPE);
        }
    }
}

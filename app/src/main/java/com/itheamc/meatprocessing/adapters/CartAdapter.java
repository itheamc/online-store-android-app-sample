package com.itheamc.meatprocessing.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.itheamc.meatprocessing.databinding.CartItemViewBinding;
import com.itheamc.meatprocessing.databinding.OrderItemViewBinding;
import com.itheamc.meatprocessing.interfaces.DialogBuilderInterface;
import com.itheamc.meatprocessing.models.internal.CartItems;

import static com.itheamc.meatprocessing.models.internal.CartItems.cartItemsItemCallback;
import static com.itheamc.meatprocessing.variables.Constants.CART_FRAGMENT;

/**
 * This adapter is used by the Cart Fragment and Order Fragment to set data in the recyclerview
 * Here we are taking the int callingFragment from the constructor to know that which
 * fragment is calling this adapter
 * Fragment type is defined in the Constraints.java class
 */

public class CartAdapter extends ListAdapter<CartItems, RecyclerView.ViewHolder> {
    private int fragment;
    private DialogBuilderInterface dialogBuilderInterface;

    public CartAdapter(int callingFragment) {
        super(cartItemsItemCallback);
        this.fragment = callingFragment;
    }

    // Constructor for cart fragment
    public CartAdapter(int callingFragment, DialogBuilderInterface dialogBuilderInterface) {
        super(cartItemsItemCallback);
        this.fragment = callingFragment;
        this.dialogBuilderInterface = dialogBuilderInterface;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (fragment == CART_FRAGMENT) {
            CartItemViewBinding cartItemBinding = CartItemViewBinding.inflate(inflater, parent, false);
            return new CartViewHolder(cartItemBinding, dialogBuilderInterface);
        } else {
            OrderItemViewBinding orderItemViewBinding = OrderItemViewBinding.inflate(inflater, parent, false);
            return new OrderViewHolder(orderItemViewBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CartItems cartItem = getItem(position);
        if (fragment == CART_FRAGMENT) {
            ((CartViewHolder) holder).cartItemViewBinding.setCartitem(cartItem);
        } else {
            ((OrderViewHolder) holder).orderItemViewBinding.setCartitem(cartItem);
        }

    }

    // Static viewholder class for cart_item_view.xml
    public static class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CartItemViewBinding cartItemViewBinding;
        DialogBuilderInterface builderInterface;

        public CartViewHolder(@NonNull CartItemViewBinding cartItemBinding, DialogBuilderInterface dialogBuilderInterface) {
            super(cartItemBinding.getRoot());
            this.cartItemViewBinding = cartItemBinding;
            this.builderInterface = dialogBuilderInterface;
            this.cartItemViewBinding.quantityTextview.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            builderInterface.onQtyClicked(getAdapterPosition());
        }
    }

    // Static viewholder class for confirmed_ item_view.xml
    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        OrderItemViewBinding orderItemViewBinding;

        public OrderViewHolder(@NonNull OrderItemViewBinding orderItemViewBinding) {
            super(orderItemViewBinding.getRoot());
            this.orderItemViewBinding = orderItemViewBinding;
        }
    }


}

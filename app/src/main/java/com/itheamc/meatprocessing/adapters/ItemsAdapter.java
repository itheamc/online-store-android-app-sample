package com.itheamc.meatprocessing.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.itheamc.meatprocessing.databinding.OrderItemViewBinding;
import com.itheamc.meatprocessing.models.internal.OrderItems;

import static com.itheamc.meatprocessing.models.internal.OrderItems.orderItemsItemCallback;

/**
 * This adapter is created to handle the user's order items in the
 * UnconfirmedOrderFragment
 * ConfirmedOrderFragment
 * DeliveringOrderFragment
 * OrderHistoryFragment
 * This adapter will be called by the OrderAdapter.class.
 */
public class ItemsAdapter extends ListAdapter<OrderItems, ItemsAdapter.ItemViewHolder> {

    protected ItemsAdapter() {
        super(orderItemsItemCallback);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        OrderItemViewBinding itemViewBinding = OrderItemViewBinding.inflate(inflater, parent, false);
        return new ItemViewHolder(itemViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        OrderItems orderItem = getItem(position);
//        holder.itemViewBinding.setOrderitem(orderItem);

    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        OrderItemViewBinding itemViewBinding;

        public ItemViewHolder(@NonNull OrderItemViewBinding itemViewBinding) {
            super(itemViewBinding.getRoot());
            this.itemViewBinding = itemViewBinding;

        }
    }
}

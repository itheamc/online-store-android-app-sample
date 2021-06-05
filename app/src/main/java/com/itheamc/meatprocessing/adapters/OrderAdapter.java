package com.itheamc.meatprocessing.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.itheamc.meatprocessing.R;
import com.itheamc.meatprocessing.databinding.OrderViewBinding;
import com.itheamc.meatprocessing.interfaces.OrderInterface;
import com.itheamc.meatprocessing.models.external.Order;
import com.itheamc.meatprocessing.models.external.Products;

import java.util.List;

import static com.itheamc.meatprocessing.models.external.Order.orderItemCallback;
import static com.itheamc.meatprocessing.variables.Constants.CONFIRMED_ORDER_FRAGMENT;
import static com.itheamc.meatprocessing.variables.Constants.DELIVERING_ORDER_FRAGMENT;
import static com.itheamc.meatprocessing.variables.Constants.UNCONFIRMED_ORDER_FRAGMENT;

public class OrderAdapter extends ListAdapter<Order, RecyclerView.ViewHolder> {
    private List<Products> productsList;
    private OrderInterface orderInterface;
    private String callingFragment;
    private Context context;

    public OrderAdapter(Context ctx, OrderInterface orderInterface, List<Products> productsList, String fragmentName) {
        super(orderItemCallback);
        this.productsList = productsList;
        this.orderInterface = orderInterface;
        this.callingFragment = fragmentName;
        this.context = ctx;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        OrderViewBinding orderViewBinding = OrderViewBinding.inflate(inflater, parent, false);
        if (callingFragment.equals(UNCONFIRMED_ORDER_FRAGMENT)) {
            return new UnconfirmedOrderVH(orderViewBinding, orderInterface);
        } else if (callingFragment.equals(CONFIRMED_ORDER_FRAGMENT)) {
            return new ConfirmedOrderVH(orderViewBinding, orderInterface);
        } else if (callingFragment.equals(DELIVERING_ORDER_FRAGMENT)) {
            return new DeliveringOrderVH(orderViewBinding, orderInterface);
        } else {
            return new DeliveredOrderVH(orderViewBinding, orderInterface);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Order order = getItem(position);

        switch (callingFragment) {
            case UNCONFIRMED_ORDER_FRAGMENT:
                ((UnconfirmedOrderVH) holder).orderViewBinding.setOrderid(order.getOrderId());
                ((UnconfirmedOrderVH) holder).orderViewBinding.setItemname(getItemsName(order));
                ((UnconfirmedOrderVH) holder).orderViewBinding.setAmount(String.valueOf(getTotalAmount(order)));
                ((UnconfirmedOrderVH) holder).orderViewBinding.setTime(generateTime(order));
                ((UnconfirmedOrderVH) holder).orderViewBinding.prebookOrRegular.setVisibility(order.getItemsId().size() == 1 && isPrebooking(order.getItemsId().get(0)) ? View.VISIBLE : View.GONE);
                ((UnconfirmedOrderVH) holder).orderViewBinding.button.setText(context.getResources().getString(R.string.cancel_order_btn_text));
                break;
            case CONFIRMED_ORDER_FRAGMENT:
                ((ConfirmedOrderVH) holder).orderViewBinding.setOrderid(order.getOrderId());
                ((ConfirmedOrderVH) holder).orderViewBinding.setItemname(getItemsName(order));
                ((ConfirmedOrderVH) holder).orderViewBinding.setAmount(String.valueOf(getTotalAmount(order)));
                ((ConfirmedOrderVH) holder).orderViewBinding.setTime(generateTime(order));
                ((ConfirmedOrderVH) holder).orderViewBinding.prebookOrRegular.setVisibility(order.getItemsId().size() == 1 && isPrebooking(order.getItemsId().get(0)) ? View.VISIBLE : View.GONE);
                ((ConfirmedOrderVH) holder).orderViewBinding.button.setVisibility(order.isPaid() ? View.INVISIBLE : View.VISIBLE);
                ((ConfirmedOrderVH) holder).orderViewBinding.button.setText(context.getResources().getString(R.string.pay_now));
                break;
            case DELIVERING_ORDER_FRAGMENT:
                ((DeliveringOrderVH) holder).orderViewBinding.setOrderid(order.getOrderId());
                ((DeliveringOrderVH) holder).orderViewBinding.setItemname(getItemsName(order));
                ((DeliveringOrderVH) holder).orderViewBinding.setAmount(String.valueOf(getTotalAmount(order)));
                ((DeliveringOrderVH) holder).orderViewBinding.setTime(generateTime(order));
                ((DeliveringOrderVH) holder).orderViewBinding.prebookOrRegular.setVisibility(order.getItemsId().size() == 1 && isPrebooking(order.getItemsId().get(0)) ? View.VISIBLE : View.GONE);
                ((DeliveringOrderVH) holder).orderViewBinding.button.setText(context.getResources().getString(R.string.track_now));
                break;
            default:
                ((DeliveredOrderVH) holder).orderViewBinding.setOrderid(order.getOrderId());
                ((DeliveredOrderVH) holder).orderViewBinding.setItemname(getItemsName(order));
                ((DeliveredOrderVH) holder).orderViewBinding.setAmount(String.valueOf(getTotalAmount(order)));
                ((DeliveredOrderVH) holder).orderViewBinding.setTime(generateTime(order));
                ((DeliveredOrderVH) holder).orderViewBinding.prebookOrRegular.setVisibility(order.getItemsId().size() == 1 && isPrebooking(order.getItemsId().get(0)) ? View.VISIBLE : View.GONE);
                ((DeliveredOrderVH) holder).orderViewBinding.button.setVisibility(View.VISIBLE);
                ((DeliveredOrderVH) holder).orderViewBinding.button.setText(context.getResources().getString(R.string.generate_invoices));
                break;
        }

    }


    // Method to get order item name from the product list
    private String getItemsName(Order order) {
        String tempNames = "";
        List<String> idsList = order.getItemsId();
        for (int i = 0; i < idsList.size(); i++) {
            for (Products product : productsList) {
                if (product.getProductId().toLowerCase().trim().equals(idsList.get(i).toLowerCase().trim())) {
                    if (idsList.size() > 1) {
                        if (i == 0) {
                            tempNames = product.getProductName() + " (" + order.getOrderedQuantity().get(i) + "KG)";
                        } else if (i == idsList.size() - 1) {
                            tempNames = tempNames + " & " + product.getProductName() + " (" + order.getOrderedQuantity().get(i) + "KG)";
                        } else {
                            tempNames = tempNames + ", " + product.getProductName() + "( " + order.getOrderedQuantity().get(i) + "KG)";
                        }
                    } else {
                        tempNames = product.getProductName() + " (" + order.getOrderedQuantity().get(i) + "KG)";
                    }

                    break;
                }

            }


        }

        return tempNames;
    }

    // Method to get item Price
    private double getTotalAmount(Order order) {
        double tempAmount = 0;
        List<Double> amountList = order.getOrderedAmount();

        for (Double amount : amountList) {
            tempAmount += amount;
        }

        return tempAmount;
    }

    // Method to generate order time
    private String generateTime(Order order) {
        String orderId = order.getOrderId();
        String rawTime = orderId.substring(5, orderId.length() - 6);
        StringBuilder finalTime = new StringBuilder();
        for (int i = 0; i < rawTime.length(); i++) {
            if (i == 3) {
                finalTime.append(" ").append(rawTime.charAt(i));
            } else if (i == rawTime.indexOf("02", 5) - 1) {
                finalTime.append(", ").append(rawTime.charAt(i));
            } else if (i == rawTime.indexOf("02", 5) + 3) {
                finalTime.append(" ").append(rawTime.charAt(i));
            } else if (i == rawTime.length() - 6 || i == rawTime.length() - 4) {
                finalTime.append(":").append(rawTime.charAt(i));
            } else if (i == rawTime.length() - 2) {
                finalTime.append(" ").append(rawTime.charAt(i));
            } else {
                finalTime.append(rawTime.charAt(i));
            }

        }

        return finalTime.toString();

    }

    // Function to checking order is prebooking or not
    private boolean isPrebooking(String itemId) {
        boolean isPrebook = false;
        for (Products product : productsList) {
            if (product.getProductId().toLowerCase().trim().equals(itemId.toLowerCase().trim())) {
                isPrebook = product.isPrebooking();
                break;
            }
        }

        return isPrebook;
    }


    // Viewholder class for unconfirmed order fragment
    public static class UnconfirmedOrderVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        OrderViewBinding orderViewBinding;
        OrderInterface orderInterface;

        public UnconfirmedOrderVH(@NonNull OrderViewBinding orderViewBinding, OrderInterface orderInterface) {
            super(orderViewBinding.getRoot());
            this.orderViewBinding = orderViewBinding;
            this.orderInterface = orderInterface;
            this.orderViewBinding.button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            orderInterface.cancelOrder(getAdapterPosition());
        }
    }


    // Viewholder class for confirmed order fragment
    public static class ConfirmedOrderVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        OrderViewBinding orderViewBinding;
        OrderInterface orderInterface;

        public ConfirmedOrderVH(@NonNull OrderViewBinding orderViewBinding, OrderInterface orderInterface) {
            super(orderViewBinding.getRoot());
            this.orderViewBinding = orderViewBinding;
            this.orderInterface = orderInterface;
            this.orderViewBinding.button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            orderInterface.payNow(getAdapterPosition());
        }
    }


    // Viewholder class for delivering order fragment
    public static class DeliveringOrderVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        OrderViewBinding orderViewBinding;
        OrderInterface orderInterface;

        public DeliveringOrderVH(@NonNull OrderViewBinding orderViewBinding, OrderInterface orderInterface) {
            super(orderViewBinding.getRoot());
            this.orderViewBinding = orderViewBinding;
            this.orderInterface = orderInterface;
            this.orderViewBinding.button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            orderInterface.navigateLocation(getAdapterPosition());
        }
    }


    // Viewholder class for unconfirmed order fragment
    public static class DeliveredOrderVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        OrderViewBinding orderViewBinding;
        OrderInterface orderInterface;

        public DeliveredOrderVH(@NonNull OrderViewBinding orderViewBinding, OrderInterface orderInterface) {
            super(orderViewBinding.getRoot());
            this.orderViewBinding = orderViewBinding;
            this.orderInterface = orderInterface;
            this.orderViewBinding.button.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            orderInterface.generateInvoice(getAdapterPosition());
        }
    }
}

package com.itheamc.meatprocessing.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itheamc.meatprocessing.R;
import com.itheamc.meatprocessing.adapters.CartAdapter;
import com.itheamc.meatprocessing.databinding.FragmentOrderBinding;
import com.itheamc.meatprocessing.models.external.Users;
import com.itheamc.meatprocessing.models.internal.CartItems;
import com.itheamc.meatprocessing.models.external.Order;
import com.itheamc.meatprocessing.repositories.LocalRepo;
import com.itheamc.meatprocessing.utilities.NotifyUtils;
import com.itheamc.meatprocessing.viewmodel.SharedViewModel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.itheamc.meatprocessing.variables.Constants.CASH_ON_DELIVERY;
import static com.itheamc.meatprocessing.variables.Constants.ONLINE_PAYMENT;
import static com.itheamc.meatprocessing.variables.Constants.ORDER_FRAGMENT;
import static com.itheamc.meatprocessing.variables.Constants.ORDER_UNCONFIRMED;
import static com.itheamc.meatprocessing.variables.Constants.USER_ID;

public class OrderFragment extends Fragment {
    private static final String TAG = "OrderFragment";
    private FragmentOrderBinding orderBinding;
    private CartAdapter orderAdapter;
    private NavController navController;
    private SharedViewModel sharedViewModel;


    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        orderBinding = FragmentOrderBinding.inflate(inflater, container, false);
        return orderBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        orderAdapter = new CartAdapter(ORDER_FRAGMENT);
        orderAdapter.submitList(sharedViewModel.getCartItemsList());
        orderBinding.finalCartItemRecyclerView.setAdapter(orderAdapter);

        calcTotal();    // Calculating total amount
        handleButton();    // Calling function to handle button

    }

    // Method to disable or enable button on the basis of cartList
    private void handleButton() {
        orderBinding.confirmOrderButton.setEnabled(!sharedViewModel.getCartItemsList().isEmpty());
        orderBinding.confirmOrderButton.setText(sharedViewModel.getProduct().isPrebooking() ? getString(R.string.confirm_prebook) : getString(R.string.confirm_order));
        orderBinding.confirmOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRadioButton(v);
            }
        });
    }

    // Handle RadioButton
    private void handleRadioButton(View view) {
        if (orderBinding.cashOnDeliveryMethod.isChecked()) {
            sharedViewModel.setPaymentMethod(CASH_ON_DELIVERY);
//            view.setEnabled(false);     // Disabled the button
//            orderBinding.paymentMethodsRadioGroup.setEnabled(false);
            handleEnableFunction();
            placingOrder();

        } else if (orderBinding.onlinePaymentMethod.isChecked()) {
            sharedViewModel.setPaymentMethod(ONLINE_PAYMENT);
//            view.setEnabled(false);     // Disabled the button
//            orderBinding.paymentMethodsRadioGroup.setEnabled(false);
            handleEnableFunction();
            placingOrder();

        } else {
            NotifyUtils.showSnackBar(view, getString(R.string.please_choose_pmt_method));
        }

    }

    // Function to handle enable and disable of button and radio group
    private void handleEnableFunction() {
        orderBinding.confirmOrderButton.setEnabled(!orderBinding.confirmOrderButton.isEnabled());
        orderBinding.cashOnDeliveryMethod.setEnabled(!orderBinding.cashOnDeliveryMethod.isEnabled());
        orderBinding.onlinePaymentMethod.setEnabled(!orderBinding.onlinePaymentMethod.isEnabled());
    }

    // Calculate total amount
    private void calcTotal() {
        double total = 0;
        for (CartItems cartItem : sharedViewModel.getCartItemsList()) {
            total += cartItem.getOrderedQuantity() * cartItem.getProduct().getProductPrice();
        }
        orderBinding.totalAmount.setText(String.format("%s %s", getResources().getString(R.string.currency), String.valueOf(total)));
    }

    // Function to create orderList
    private void placingOrder() {
        List<CartItems> cartItemsList = new ArrayList<>();
        List<Double> amountList = new ArrayList<>();
        List<Double> quantityList = new ArrayList<>();
        List<String> itemsId = new ArrayList<>();
        boolean isPaid = false;
        // Getting user data from the local storage
        Users user = LocalRepo.getInstance(getContext()).getLocalUser();
        String userId = user.getUserId();

//        String userId = sharedViewModel.getUser().getUserId();
        String paymentMethod = sharedViewModel.getPaymentMethod();

        cartItemsList = sharedViewModel.getCartItemsList();

        for (CartItems cartItem : cartItemsList) {
            amountList.add(cartItem.getOrderedQuantity() * cartItem.getProduct().getProductPrice());
            quantityList.add(cartItem.getOrderedQuantity());
            itemsId.add(cartItem.getProduct().getProductId());
        }

        //isPaid
        if (paymentMethod.toLowerCase().equals(ONLINE_PAYMENT.toLowerCase())) {
            isPaid = true;
        }
        String orderId = generateOrderId(userId, amountList.size());
        Order order = new Order(orderId, itemsId, amountList, quantityList, userId, paymentMethod, ORDER_UNCONFIRMED, 27.8127560, 82.5329467, isPaid, "null", "null");
        placeOrder(order);
    }


    // Function to store order in the firestore database
    private void placeOrder(Order order) {
        FirebaseFirestore.getInstance()
                .collection("Orders")
                .document(order.getOrderId())
                .set(order)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        sharedViewModel.clearCart();
                        navController.navigate(R.id.action_orderFragment_to_confirmationFragment);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        NotifyUtils.logError(TAG, "placeOrder", e);
                        Toast.makeText(getContext(), "Unable to place order", Toast.LENGTH_SHORT).show();
                        handleEnableFunction();
                    }
                });
    }

    // Generating orderId
    private String generateOrderId(String userId, int totalItem) {
        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.ENGLISH);
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.DEFAULT, Locale.ENGLISH);
        String dateText = dateFormat.format(date);
        String timeText = timeFormat.format(date);
        String final_id = "ORDER" + dateText.replaceAll("[\\s,:]+", "") + timeText.replaceAll("[\\s,:]+", "") + "Q" + totalItem + userId.substring(0, 4);

        return final_id.toUpperCase(Locale.ENGLISH);
    }
}
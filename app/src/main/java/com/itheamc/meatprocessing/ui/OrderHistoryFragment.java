package com.itheamc.meatprocessing.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.itheamc.meatprocessing.adapters.OrderAdapter;
import com.itheamc.meatprocessing.databinding.FragmentOrderHistoryBinding;
import com.itheamc.meatprocessing.graphics.InvoiceGenerator;
import com.itheamc.meatprocessing.interfaces.OrderInterface;
import com.itheamc.meatprocessing.models.external.Order;
import com.itheamc.meatprocessing.models.external.Products;
import com.itheamc.meatprocessing.models.internal.OrderItems;
import com.itheamc.meatprocessing.utilities.NotifyUtils;
import com.itheamc.meatprocessing.viewmodel.SharedViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.itheamc.meatprocessing.variables.Constants.ORDERED_HISTORY_FRAGMENT;


public class OrderHistoryFragment extends Fragment implements OrderInterface {
    private static final String TAG = "OrderHistoryFragment";
    private FragmentOrderHistoryBinding orderHistoryBinding;
    private NavController navController;
    private SharedViewModel sharedViewModel;
    private OrderAdapter orderAdapter;


    public OrderHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        orderHistoryBinding = FragmentOrderHistoryBinding.inflate(inflater, container, false);
        return orderHistoryBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        orderAdapter = new OrderAdapter(getContext(), this, sharedViewModel.getProductsList(), ORDERED_HISTORY_FRAGMENT);
        orderHistoryBinding.completedOrderRecyclerView.setAdapter(orderAdapter);

        checkOrderList();

    }

    /**
     * function to check weather list is empty or not
     * and performing action accordingly i.e. showing empty message
     * and slso submit to recycler view if not empty
     */

    private void checkOrderList() {
        if (!sharedViewModel.getTempOrderList().isEmpty()) {
            orderAdapter.submitList(sharedViewModel.getTempOrderList());
            if (orderHistoryBinding.noOrderTextview.getVisibility() == View.VISIBLE) {
                orderHistoryBinding.noOrderTextview.setVisibility(View.GONE);
            }
        } else {
            orderHistoryBinding.noOrderTextview.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void cancelOrder(int position) {

    }

    @Override
    public void navigateLocation(int position) {

    }

    @Override
    public void payNow(int position) {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void generateInvoice(int position) {
        Order order = sharedViewModel.getTempOrderList().get(position);
        List<OrderItems> orderItems = new ArrayList<>();

        for (int i = 0; i < order.getItemsId().size(); i++) {
            orderItems.add(new OrderItems(order.getItemsId().get(i), getItemsName(order.getItemsId().get(i)),
                    getItemPrice(order.getItemsId().get(i)),
                    order.getOrderedQuantity().get(i),
                    order.getOrderedAmount().get(i)));
        }

        if (isPremissionGranted() || !isPremissionGranted()) {
            InvoiceGenerator
                    .getInstance(getContext(), 1240, 1860, orderItems, order.getOrderId())
                    .createPdf();
        }
    }



    // Method to get order item name from the product list
    private String getItemsName(String itemId) {
        String itemName = "";

        for (Products product : sharedViewModel.getProductsList()) {
            if (product.getProductId().toLowerCase().trim().equals(itemId.toLowerCase().trim())) {
                itemName = product.getProductName();
            }

        }

        return itemName;
    }

    // Method to get order item price from the product list
    private double getItemPrice(String itemId) {
        double itemPrice = 0;

        for (Products product : sharedViewModel.getProductsList()) {
            if (product.getProductId().toLowerCase().trim().equals(itemId.toLowerCase().trim())) {
                itemPrice = product.getProductPrice();
            }

        }

        return itemPrice;
    }


    // Checking whether permission is granted or not
    private boolean isPremissionGranted() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
            return false;
        } else {
            return true;
        }
    }

    // Requesting permission if not allowed
    private void requestPermission() {
        ActivityCompat
                .requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1011);
    }



    // Overriding OnRequestPermissionResult method
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1011) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                NotifyUtils.logDebug(TAG, "Location Permission Granted");
            } else {
                requestPermission();
            }
        }
    }
}
package com.itheamc.meatprocessing.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.itheamc.meatprocessing.adapters.OrderAdapter;
import com.itheamc.meatprocessing.databinding.FragmentConfirmedOrderBinding;
import com.itheamc.meatprocessing.interfaces.OrderInterface;
import com.itheamc.meatprocessing.viewmodel.SharedViewModel;

import static com.itheamc.meatprocessing.variables.Constants.CONFIRMED_ORDER_FRAGMENT;

public class ConfirmedOrderFragment extends Fragment implements OrderInterface {
    private static final String TAG = "ConfirmedOFragment";
    private FragmentConfirmedOrderBinding confirmedOrderBinding;
    private NavController navController;
    private SharedViewModel sharedViewModel;
    private OrderAdapter orderAdapter;


    public ConfirmedOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        confirmedOrderBinding = FragmentConfirmedOrderBinding.inflate(inflater, container, false);
        return confirmedOrderBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        orderAdapter = new OrderAdapter(getContext(), this, sharedViewModel.getProductsList(), CONFIRMED_ORDER_FRAGMENT);
        confirmedOrderBinding.confirmedRecyclerView.setAdapter(orderAdapter);
        checkOrderList();

    }

    /**
     *     function to check weather list is empty or not
     *     and performing action accordingly i.e. showing empty message
     *     and slso submit to recycler view if not empty
     */

    private void checkOrderList() {
        if (!sharedViewModel.getTempOrderList().isEmpty()) {
            orderAdapter.submitList(sharedViewModel.getTempOrderList());
            if (confirmedOrderBinding.noOrderTextview.getVisibility() == View.VISIBLE) {
                confirmedOrderBinding.noOrderTextview.setVisibility(View.GONE);
            }
        } else {
            confirmedOrderBinding.noOrderTextview.setVisibility(View.VISIBLE);
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

    @Override
    public void generateInvoice(int position) {

    }
}
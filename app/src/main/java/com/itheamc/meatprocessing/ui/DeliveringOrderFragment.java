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

import com.google.android.gms.maps.model.LatLng;
import com.itheamc.meatprocessing.R;
import com.itheamc.meatprocessing.adapters.OrderAdapter;
import com.itheamc.meatprocessing.databinding.FragmentDeliveringOrderBinding;
import com.itheamc.meatprocessing.interfaces.OrderInterface;
import com.itheamc.meatprocessing.models.external.Order;
import com.itheamc.meatprocessing.viewmodel.SharedViewModel;

import static com.itheamc.meatprocessing.variables.Constants.DELIVERING_ORDER_FRAGMENT;

public class DeliveringOrderFragment extends Fragment implements OrderInterface {
    private static final String TAG = "DeliveringOFragment";
    private FragmentDeliveringOrderBinding deliveringOrderBinding;
    private NavController navController;
    private SharedViewModel sharedViewModel;
    private OrderAdapter orderAdapter;


    public DeliveringOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        deliveringOrderBinding = FragmentDeliveringOrderBinding.inflate(inflater, container, false);
        return deliveringOrderBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        orderAdapter = new OrderAdapter(getContext(), this, sharedViewModel.getProductsList(), DELIVERING_ORDER_FRAGMENT);
        deliveringOrderBinding.onthewayRecyclerView.setAdapter(orderAdapter);

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
            if (deliveringOrderBinding.noOrderTextview.getVisibility() == View.VISIBLE) {
                deliveringOrderBinding.noOrderTextview.setVisibility(View.GONE);
            }
        } else {
            deliveringOrderBinding.noOrderTextview.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void cancelOrder(int position) {

    }

    @Override
    public void navigateLocation(int position) {
        Order order = sharedViewModel.getTempOrderList().get(position);
        LatLng latLng = new LatLng(order.getLocLat(), order.getLocLong());
        sharedViewModel.setLatLng(latLng);
        navController.navigate(R.id.action_deliveringOrderFragment_to_mapsFragment);
    }

    @Override
    public void payNow(int position) {

    }

    @Override
    public void generateInvoice(int position) {

    }
}
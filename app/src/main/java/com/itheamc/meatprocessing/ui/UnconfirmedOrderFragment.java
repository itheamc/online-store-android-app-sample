package com.itheamc.meatprocessing.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.itheamc.meatprocessing.R;
import com.itheamc.meatprocessing.adapters.OrderAdapter;
import com.itheamc.meatprocessing.databinding.FragmentUnconfirmedOrderBinding;
import com.itheamc.meatprocessing.interfaces.OrderInterface;
import com.itheamc.meatprocessing.models.external.Order;
import com.itheamc.meatprocessing.models.external.Users;
import com.itheamc.meatprocessing.repositories.LocalRepo;
import com.itheamc.meatprocessing.utilities.NotifyUtils;
import com.itheamc.meatprocessing.viewmodel.SharedViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.itheamc.meatprocessing.variables.Constants.ORDERS_COLLECTION;
import static com.itheamc.meatprocessing.variables.Constants.ORDER_CANCELLED;
import static com.itheamc.meatprocessing.variables.Constants.ORDER_UNCONFIRMED;
import static com.itheamc.meatprocessing.variables.Constants.UNCONFIRMED_ORDER_FRAGMENT;


public class UnconfirmedOrderFragment extends Fragment implements OrderInterface {
    private static final String TAG = "UnconfirmedFragment";
    private FragmentUnconfirmedOrderBinding unconfirmedOrderBinding;
    private NavController navController;
    private SharedViewModel sharedViewModel;
    private OrderAdapter orderAdapter;
    private List<Order> unconfirmedOrderList;


    public UnconfirmedOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        unconfirmedOrderBinding = FragmentUnconfirmedOrderBinding.inflate(inflater, container, false);
        return unconfirmedOrderBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        orderAdapter = new OrderAdapter(getContext(), this, sharedViewModel.getProductsList(), UNCONFIRMED_ORDER_FRAGMENT);
        unconfirmedOrderBinding.unconfirmedRecyclerView.setAdapter(orderAdapter);

        getOrdersData();   // Calling function to handle message visibility
    }

    /**
     *     function to check weather list is empty or not
     *     and performing action accordingly i.e. showing empty message
     *     and slso submit to recycler view if not empty
      */

    private void checkOrderList() {
        if (sharedViewModel.getTempOrderList() != null && !sharedViewModel.getTempOrderList().isEmpty()) {
            orderAdapter.submitList(sharedViewModel.getTempOrderList());
            if (unconfirmedOrderBinding.noOrderTextview.getVisibility() == View.VISIBLE) {
                unconfirmedOrderBinding.noOrderTextview.setVisibility(View.GONE);
            }
        } else {
            getOrdersData();
        }
    }

    // Function to change the status of the order as cancelled
    private void cancellingOrder(int position) {
        FirebaseFirestore.getInstance()
                .collection(ORDERS_COLLECTION)
                .document(unconfirmedOrderList.get(position).getOrderId())
                .update("orderStatus", ORDER_CANCELLED)
                .addOnSuccessListener(aVoid -> {
//                    sharedViewModel.removeTempOrderItem(position);
//                    orderAdapter.notifyItemRemoved(position);
                    NotifyUtils.showToast(getContext(), getString(R.string.order_cancelled));
//                    startTimer();
                })
                .addOnFailureListener(e -> {
                    NotifyUtils.showToast(getContext(), getString(R.string.error_message));
                    NotifyUtils.logError(TAG, "cancellingOrder", e);
                });
    }

    // Creating timer function for showing message (if list empty) after cancelling the order
    private void startTimer() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkOrderList();
            }
        }, 1000);
    }


    @Override
    public void cancelOrder(int position) {
        cancellingOrder(position);
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


    //Function to retrieve order data from the firebase Firestore
    private void getOrdersData() {
        if (getContext() != null) {
            Users user = LocalRepo.getInstance(getContext()).getLocalUser();
            String userId = user.getUserId();

            FirebaseFirestore.getInstance()
                    .collection(ORDERS_COLLECTION)
                    .whereEqualTo("userId", userId)
                    .whereEqualTo("orderStatus", ORDER_UNCONFIRMED)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                            List<Order> tempOrderList = new ArrayList<>();
                            if (error != null) {
                                NotifyUtils.showToast(getContext(), getString(R.string.error_message));
                                NotifyUtils.logError(TAG, "getOrdersData", error);
                            } else {
                                unconfirmedOrderList = new ArrayList<>();
                                if (value != null && !value.getDocuments().isEmpty()) {
                                    unconfirmedOrderBinding.noOrderTextview.setVisibility(View.GONE);
                                    unconfirmedOrderList = value.toObjects(Order.class);
                                    orderAdapter.submitList(unconfirmedOrderList);
                                } else {
                                    orderAdapter.submitList(unconfirmedOrderList);
                                    unconfirmedOrderBinding.noOrderTextview.setVisibility(View.VISIBLE);
                                }

                            }

                        }
                    });
        }

    }
}
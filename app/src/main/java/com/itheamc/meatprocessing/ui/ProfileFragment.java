package com.itheamc.meatprocessing.ui;

import android.content.Intent;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.itheamc.meatprocessing.R;
import com.itheamc.meatprocessing.databinding.FragmentProfileBinding;
import com.itheamc.meatprocessing.models.external.Order;
import com.itheamc.meatprocessing.models.external.Users;
import com.itheamc.meatprocessing.repositories.LocalRepo;
import com.itheamc.meatprocessing.ui.activities.LoginActivity;
import com.itheamc.meatprocessing.utilities.NotifyUtils;
import com.itheamc.meatprocessing.viewmodel.SharedViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.itheamc.meatprocessing.variables.Constants.ORDERS_COLLECTION;
import static com.itheamc.meatprocessing.variables.Constants.ORDER_COMPLETED;
import static com.itheamc.meatprocessing.variables.Constants.ORDER_ON_THE_WAY;
import static com.itheamc.meatprocessing.variables.Constants.ORDER_UNCONFIRMED;
import static com.itheamc.meatprocessing.variables.Constants.ORDER_UNDER_PROCESS;


public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private FragmentProfileBinding profileBinding;
    private SharedViewModel sharedViewModel;
    private NavController navController;

    // List of order as per their status
    private List<Order> unconfirmedOrders;
    private List<Order> underprocessOrders;
    private List<Order> onthewayOrders;
    private List<Order> completedOrders;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        profileBinding = FragmentProfileBinding.inflate(inflater, container, false);
        return profileBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        setUserData();

        // TextView onclick listener for address Textview
        profileBinding.userAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_profileFragment_to_addressFragment);
            }
        });

        // Onclick listener on signout textview to signout
        profileBinding.signoutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loggedOut();
            }
        });

    }

    // Method to set the user data from the local storage
    private void setUserData() {
        Users user = LocalRepo.getInstance(getContext()).getLocalUser();
        profileBinding.setUser(user);

        getOrdersData(user.getUserId());    // Calling function to get data
        setImageViewClick();    // Function to set onClicke Listener on image view

    }

    //Function to retrieve order data from the firebase Firestore
    private void getOrdersData(String userId) {
        FirebaseFirestore.getInstance()
                .collection(ORDERS_COLLECTION)
                .whereEqualTo("userId", userId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        List<Order> tempOrderList = new ArrayList<>();
                        if (error != null) {
                            NotifyUtils.showToast(getContext(), getString(R.string.error_message));
                            NotifyUtils.logError(TAG, "getOrdersData", error);
                        } else {
                            if (value != null) {
                                tempOrderList = value.toObjects(Order.class);
                            }

                            handleBadge(tempOrderList);

                        }

                    }
                });
    }

    // Function to handle order badge as per order status
    private void handleBadge(List<Order> orderList) {
        if (!orderList.isEmpty()) {
            unconfirmedOrders = new ArrayList<>();
            underprocessOrders = new ArrayList<>();
            onthewayOrders = new ArrayList<>();
            completedOrders = new ArrayList<>();

            for (Order order : orderList) {
                switch (order.getOrderStatus()) {
                    case ORDER_UNCONFIRMED:
                        unconfirmedOrders.add(order);
                        break;
                    case ORDER_UNDER_PROCESS:
                        underprocessOrders.add(order);
                        break;
                    case ORDER_ON_THE_WAY:
                        onthewayOrders.add(order);
                        break;
                    case ORDER_COMPLETED:
                        completedOrders.add(order);
                        break;
                }
            }

            // Setting Badge Visibility
            profileBinding.unconfirmedOrderBadge.setVisibility(!unconfirmedOrders.isEmpty() ? View.VISIBLE : View.GONE);
            profileBinding.underprocessOrderBadge.setVisibility(!underprocessOrders.isEmpty() ? View.VISIBLE : View.GONE);
            profileBinding.onthewayOrderBadge.setVisibility(!onthewayOrders.isEmpty() ? View.VISIBLE : View.GONE);
            profileBinding.deliveredOrderBadge.setVisibility(View.GONE);

            // Setting Bagde Value
            profileBinding.unconfirmedOrderBadge.setText(String.valueOf(unconfirmedOrders.size()));
            profileBinding.underprocessOrderBadge.setText(String.valueOf(underprocessOrders.size()));
            profileBinding.onthewayOrderBadge.setText(String.valueOf(onthewayOrders.size()));
            profileBinding.deliveredOrderBadge.setText(String.valueOf(completedOrders.size()));


        }
    }

    // Setting onclick listener for badge
    private void setImageViewClick() {
        // Unconfirmed Badge
        profileBinding.unconfirmedOrderImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedViewModel.setTempOrderList(unconfirmedOrders);
                navController.navigate(R.id.action_profileFragment_to_unconfirmedOrderFragment);
            }
        });

        // Underprocess Badge
        profileBinding.underprocessImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedViewModel.setTempOrderList(underprocessOrders);
                navController.navigate(R.id.action_profileFragment_to_confirmedOrderFragment);
            }
        });

        // Ontheway Badge
        profileBinding.onthewayImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedViewModel.setTempOrderList(onthewayOrders);
                navController.navigate(R.id.action_profileFragment_to_deliveringOrderFragment);
            }
        });

        // Delivered Badge
        profileBinding.deliveredImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedViewModel.setTempOrderList(completedOrders);
                navController.navigate(R.id.action_profileFragment_to_orderHistoryFragment);
            }
        });
    }


    // Mehtod to logout
    private void loggedOut() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.client_id))
                .build();

        if (getContext() != null) {
            // Build a GoogleSignInClient with the options specified by gso.
            GoogleSignInClient signInClient = GoogleSignIn.getClient(getContext(), googleSignInOptions);

            signInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        LocalRepo.getInstance(getContext()).clearUserData();
                        FirebaseAuth.getInstance().signOut();
                        moveToLoginActivity();
                    } else {
                        NotifyUtils.showToast(getContext(), "Unable to logout");
                    }

                }
            });
        } else {
            NotifyUtils.logDebug(TAG, "getContext() is null");
        }

    }

    // function to move in login activity
    private void moveToLoginActivity() {
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

}
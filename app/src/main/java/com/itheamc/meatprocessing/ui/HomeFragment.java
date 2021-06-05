package com.itheamc.meatprocessing.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.itheamc.meatprocessing.R;
import com.itheamc.meatprocessing.adapters.HomeAdapter;
import com.itheamc.meatprocessing.databinding.FragmentHomeBinding;
import com.itheamc.meatprocessing.interfaces.HomeAdapterInterface;
import com.itheamc.meatprocessing.models.internal.HomeItems;
import com.itheamc.meatprocessing.models.external.Products;
import com.itheamc.meatprocessing.utilities.NotifyUtils;
import com.itheamc.meatprocessing.viewmodel.SharedViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.itheamc.meatprocessing.variables.Constants.PREBOOK_TYPE;

public class HomeFragment extends Fragment implements HomeAdapterInterface {
    private static final String TAG = "HomeFragment";
    private FragmentHomeBinding homeBinding;
    private SharedViewModel sharedViewModel;
    private HomeAdapter homeAdapter;
    private List<HomeItems> homeItemsList;
    private NavController navController;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        return homeBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeItemsList = new ArrayList<>();
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        navController = Navigation.findNavController(view);
//        homeItemsList = sharedViewModel.getHomeItemsList();

        homeAdapter = new HomeAdapter(this, getContext());
//        homeBinding.homeRecyclerView.setAdapter(homeAdapter);
//        homeAdapter.submitList(homeItemsList);
        if (sharedViewModel.getProductsList() .isEmpty()) {
            getProductsList();
        } else {
            handleAdapter();
        }

    }


    // Method to get Products data from the firebase firestore server
    public void getProductsList() {

        FirebaseFirestore.getInstance()
                .collection("Products")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        NotifyUtils.logDebug(TAG, "Product list retrieved successfully");
                        if (queryDocumentSnapshots != null) {
                            List<Products> productsList = new ArrayList<>();
                            productsList = queryDocumentSnapshots.toObjects(Products.class);
                            Log.d(TAG, "onSuccess: "+ productsList.toString());
                            sharedViewModel.setProductsList(productsList);
                            handleAdapter();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        NotifyUtils.logError(TAG, "getProductsList", e);
                    }
                });

    }

    // Method to set call adapter class and submit value
    private void handleAdapter() {
        homeBinding.homeRecyclerView.setAdapter(homeAdapter);
        homeAdapter.submitList(sharedViewModel.getHomeItemsList());
    }



    // Method overriding from the ProductAdapterInterface
    @Override
    public void onItemClicked(int position, String type) {
        Products product;
        if (type.equals(PREBOOK_TYPE)) {
            product = sharedViewModel.getPrebookProductList().get(position);
        } else {
            product = sharedViewModel.getRegularProductList().get(position);
        }

        sharedViewModel.setProduct(product);
        navController.navigate(R.id.action_homeFragment_to_productFragment);
    }


    //
//    /**
//     * Add demo Products
//     */
//
//    private void generateProduct() {
//        for (int i = 1; i <= 7; i++) {
//            Products product = new Products(String.valueOf(i), "Product - " + i, Collections.singletonList("no-Image"), Arrays.asList("1", "2", "3", "4", "5"), "Regular Processed Product", 5 * i, 10 * i, 2 * i, true, false, "KG");
//            addProduct(product);
//        }
//    }
//
//    private void addProduct(Products product) {
//        FirebaseFirestore.getInstance()
//                .collection("Products")
//                .add(product)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(TAG, "onSuccess: Product with id -- " + documentReference.getId() + " -- added successfully");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e(TAG, "onFailure: ", e.getCause());
//                    }
//                });
//    }


}
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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itheamc.meatprocessing.R;
import com.itheamc.meatprocessing.adapters.HomeAdapter;
import com.itheamc.meatprocessing.adapters.ImageSliderAdapter;
import com.itheamc.meatprocessing.adapters.SliderAdapter;
import com.itheamc.meatprocessing.databinding.FragmentProductBinding;
import com.itheamc.meatprocessing.models.external.Products;
import com.itheamc.meatprocessing.models.internal.CartItems;
import com.itheamc.meatprocessing.utilities.NotifyUtils;
import com.itheamc.meatprocessing.viewmodel.SharedViewModel;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class ProductFragment extends Fragment {
    private static final String TAG = "ProductFragment";
    private FragmentProductBinding productBinding;
    private NavController navController;
    private double selectedQty = 0;
    private SharedViewModel sharedViewModel;
    private ImageSliderAdapter imageSliderAdapter;


    public ProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        productBinding = FragmentProductBinding.inflate(inflater, container, false);
        return productBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        navController = Navigation.findNavController(view);
        // Passing product from viewmodel to xml layout
        productBinding.setProduct(sharedViewModel.getProduct());
        handleSlider();     // Calling method to handle the image slider
        setSelectedQty();   // Calling function to set the value of the selectedQty variable

        productBinding.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildQuantitySelectionDialog();
            }
        });

    }

    // Method to handle Image Slider
    private void handleSlider() {
        imageSliderAdapter = new ImageSliderAdapter(sharedViewModel.getProduct().getProductImages());
        productBinding.productImageSlider.setSliderAdapter(imageSliderAdapter);
        productBinding.productImageSlider.setSliderTransformAnimation(SliderAnimations.FANTRANSFORMATION);
        NotifyUtils.logDebug(TAG, String.valueOf(sharedViewModel.getProduct().getProductImages().size()));

    }

    // Function to set the default value for the selected qty
    private void setSelectedQty() {
        selectedQty = Double.parseDouble(sharedViewModel.getProduct().getAvailableQuantities().get(1));
    }

    // Material Dialog builder for quantity selection
    private void buildQuantitySelectionDialog() {
        String unit = getString(R.string.unit);
        String[] quantityList = generateArrays(unit);
        if (getContext() != null) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
            builder.setTitle(getResources().getString(R.string.choose_quantity))
                    .setSingleChoiceItems(quantityList, 1, (dialog, which) -> {
                        String trimmedText = quantityList[which].replace(unit, "").trim();
                        selectedQty = Double.parseDouble(trimmedText);
                    })
                    .setNegativeButton(getResources().getString(R.string.cancel), (dialog, which) -> {
                        dialog.dismiss();
                        setSelectedQty();
                    })
                    .setPositiveButton(getResources().getText(R.string.confirm), (dialog, which) -> {
                        navigateToDestination();
                    })
                    .show();
        }

    }

    // Function to generate available quanities arrays
    private String[] generateArrays( String unit) {
        List<String> availableQuantities = sharedViewModel.getProduct().getAvailableQuantities();
        String[] tempQuantities = new String[availableQuantities.size()];

        for (int i = 0; i < availableQuantities.size(); i++) {
            tempQuantities[i] = String.format("%s %s", availableQuantities.get(i), unit);
        }

        return tempQuantities;
    }


    // Method to identify either product is prebooking or not and behave accordingly
    private void navigateToDestination() {
        if (sharedViewModel.getProduct().isPrebooking()) {
            if (!sharedViewModel.getCartItemsList().isEmpty()) {
                sharedViewModel.clearCart();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addItems();
                        navController.navigate(R.id.action_productFragment_to_orderFragment);
                    }
                }, 200);
            } else {
                addItems();
                navController.navigate(R.id.action_productFragment_to_orderFragment);
            }

        } else {
            addItems();
            NotifyUtils.showSnackBarWithAction(navController,
                    R.id.action_productFragment_to_cartFragment,
                    productBinding.addToCartButton,
                    getString(R.string.go_to_cart_button),
                    getString(R.string.product_added_in_cart_message));
        }

    }

    // Method to add items to the cartItemList
    private void addItems() {
        Products product = sharedViewModel.getProduct();
        Random random = new Random();
        int tempRand = random.nextInt(9);
        String itemId = tempRand + product.getProductId();
        CartItems cartItem = new CartItems(itemId, product, selectedQty);
        sharedViewModel.addCartItem(cartItem);
    }
}
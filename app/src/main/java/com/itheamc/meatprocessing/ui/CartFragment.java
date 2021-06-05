package com.itheamc.meatprocessing.ui;

import android.content.DialogInterface;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itheamc.meatprocessing.R;
import com.itheamc.meatprocessing.adapters.CartAdapter;
import com.itheamc.meatprocessing.databinding.FragmentCartBinding;
import com.itheamc.meatprocessing.interfaces.DialogBuilderInterface;
import com.itheamc.meatprocessing.models.internal.CartItems;
import com.itheamc.meatprocessing.utilities.NotifyUtils;
import com.itheamc.meatprocessing.viewmodel.InterfaceViewModel;
import com.itheamc.meatprocessing.viewmodel.SharedViewModel;

import java.util.List;

import static com.itheamc.meatprocessing.variables.Constants.CART_FRAGMENT;

public class CartFragment extends Fragment implements DialogBuilderInterface {
    private static final String TAG = "CartFragment";
    private FragmentCartBinding cartBinding;
    private CartAdapter cartAdapter;
    private SharedViewModel sharedViewModel;
    private NavController navController;
    private InterfaceViewModel interfaceViewModel;


    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        cartBinding = FragmentCartBinding.inflate(inflater, container, false);
        return cartBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        interfaceViewModel = new ViewModelProvider(requireActivity()).get(InterfaceViewModel.class);
        navController = Navigation.findNavController(view);

        RecyclerView cartRecyclerView = cartBinding.cartRecyclerView;
        cartAdapter = new CartAdapter(CART_FRAGMENT, this);
        if (getContext() != null) {
            cartRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        }
        cartRecyclerView.setAdapter(cartAdapter);

        cartAdapter.submitList(sharedViewModel.getCartItemsList());
        calcTotal();    // Calculating the total amount
        checkButtonStatus();    // Calling function to disable button if list is empty

        //Calling function to implement swipe remove gesture
        implementSwipe();

        // Adding Product on btn click
        cartBinding.orderNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_cartFragment_to_orderFragment);
            }
        });


    }

    // Method to disable or enable button on the basis of cartList
    private void checkButtonStatus() {
        cartBinding.orderNowButton.setEnabled(!sharedViewModel.getCartItemsList().isEmpty());
    }

    // Implementing swipe gesture on Recycler view
    private void implementSwipe() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                CartItems item = sharedViewModel.getCartItemsList().get(position);
                boolean isRemoved = sharedViewModel.removeCartItem(position);
                if (isRemoved) {
                    NotifyUtils.showSnackBar(cartBinding.orderNowButton, getString(R.string.cart_item_removed));
                    cartAdapter.notifyItemRemoved(position);
                    calcTotal();
                    checkButtonStatus();
                    interfaceViewModel.getMainActivityInterface().updateBadgeNumber();
                } else {
                    NotifyUtils.showSnackBar(cartBinding.orderNowButton, getString(R.string.cart_item_not_removed));
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(cartBinding.cartRecyclerView);
    }


    // Calculate total amount
    private void calcTotal() {
        double total = 0;
        for (CartItems cartItem : sharedViewModel.getCartItemsList()) {
            total += cartItem.getOrderedQuantity() * cartItem.getProduct().getProductPrice();
        }
        cartBinding.totalAmountTextview.setText(String.format("%s %s", getResources().getString(R.string.currency), String.valueOf(total)));
    }

    // Material Dialog builder for quantity selection
    private void buildQuantitySelectionDialog(int position) {

        String[] quantityList = generateArrays(position);
        // material dialog building process started from here--
        if (getContext() != null) {
            // setting index for checked item on the basis of previous selection
            int[] checkedItemIndex = {getQtyIndex(quantityList, position)};
            if (checkedItemIndex[0] < 0) {
                quantityList[quantityList.length-1] = String.valueOf(sharedViewModel.getCartItemsList().get(position).getOrderedQuantity()) + " "+ getString(R.string.unit);
                checkedItemIndex[0] = quantityList.length - 1;
            }
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
            builder.setTitle(getResources().getString(R.string.choose_quantity))
                    .setSingleChoiceItems(quantityList, checkedItemIndex[0], new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkedItemIndex[0] = which;
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.cancel), (dialog, which) -> {
                        // dismiss dialog
                        dialog.dismiss();

                    })
                    .setPositiveButton(getResources().getText(R.string.confirm), (dialog, which) -> {
                        String selectedQty = quantityList[checkedItemIndex[0]].replace(getString(R.string.unit), "").trim();
                        cartAdapter.submitList(sharedViewModel.updateCartItems(selectedQty, position));
                        cartAdapter.notifyItemChanged(position);
                        calcTotal();
                    })
                    .show();
        }

    }


    // Function to get already selected quantity's index from the quantities list
    private int getQtyIndex(String[] qtyList, int position) {
        CartItems cartItem = sharedViewModel.getCartItemsList().get(position);
        double itemQty = cartItem.getOrderedQuantity();
        String qtyString = itemQty + " " + getString(R.string.unit);
        int index = -1;
        for (int i = 0; i < qtyList.length; i++) {
            if (qtyList[i].toLowerCase().trim().equals(qtyString.toLowerCase().trim())) {
                index = i;
                break;
            }
        }

        return index;
    }


    // Function to generate available quanities arrays for product
    private String[] generateArrays(int position) {
        String unit = getString(R.string.unit);
        List<String> availableQuantities = sharedViewModel.getCartItemsList().get(position).getProduct().getAvailableQuantities();
        String[] tempQuantities = new String[availableQuantities.size()];

        for (int i = 0; i < availableQuantities.size(); i++) {
            tempQuantities[i] = String.format("%s %s", availableQuantities.get(i), unit);
        }

        return tempQuantities;
    }

    /**
     * Method overriding from the DialogBuilderInterface
     * to build a quantity selection dialog whenever
     * Quantity text is clicked from the recyclerview
     **/
    @Override
    public void onQtyClicked(int position) {
        buildQuantitySelectionDialog(position);
    }


}
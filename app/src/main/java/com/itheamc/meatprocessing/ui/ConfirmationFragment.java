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

import com.itheamc.meatprocessing.R;
import com.itheamc.meatprocessing.databinding.FragmentConfirmationBinding;
import com.itheamc.meatprocessing.viewmodel.SharedViewModel;


public class ConfirmationFragment extends Fragment {
    private static final String TAG = "ConfirmationFragment";
    private FragmentConfirmationBinding confirmationBinding;
    private SharedViewModel sharedViewModel;
    private NavController navController;


    public ConfirmationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        confirmationBinding = FragmentConfirmationBinding.inflate(inflater, container, false);
        return confirmationBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        confirmationBinding.viewOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_confirmationFragment_to_unconfirmedOrderFragment);
            }
        });
    }


}
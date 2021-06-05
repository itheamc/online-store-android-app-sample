package com.itheamc.meatprocessing.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.itheamc.meatprocessing.R;
import com.itheamc.meatprocessing.databinding.FragmentAddressBinding;
import com.itheamc.meatprocessing.models.external.Users;
import com.itheamc.meatprocessing.repositories.LocalRepo;
import com.itheamc.meatprocessing.utilities.NotifyUtils;

import java.util.HashMap;
import java.util.Objects;

import static com.itheamc.meatprocessing.variables.Constants.USERS_COLLECTION;

public class AddressFragment extends Fragment {
    private static final String TAG = "AddressFragment";
    private FragmentAddressBinding addressBinding;
    private NavController navController;
    private Button button;

    // Declaring Views
    private TextInputLayout name;
    private TextInputLayout phone;
    private TextInputLayout email;
    private TextInputLayout houseno;
    private TextInputLayout street;
    private TextInputLayout wardno;
    private TextInputLayout municipality;
    private TextInputLayout district;
    private TextInputLayout province;


    public AddressFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addressBinding = FragmentAddressBinding.inflate(inflater, container, false);
        return addressBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        initViews();
        setData();  //Calling function to set data

        button = addressBinding.editSaveButton;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isEnabled = isViewsEnabled();
                if (isEnabled) {
                    button.setText(getString(R.string.edit_btn_text));
                    storeData();
                } else {
                    button.setText(getString(R.string.update));
                }

            }
        });
    }

    /**
     *Initialize views
     */
    private void initViews() {
        name = addressBinding.nameInputLayout;
        phone = addressBinding.phoneInputLayout;
        email = addressBinding.emailInputLayout;
        houseno = addressBinding.houseNoInputLayout;
        street = addressBinding.streetInputLayout;
        wardno = addressBinding.wardNoInputLayout;
        municipality = addressBinding.municipalityInputLayout;
        district = addressBinding.districtInputLayout;
        province = addressBinding.provinceInputLayout;
    }


    /**
     * Method to set the already provided data to the textfield
     * from the viewmodel
     */
    private void setData() {
        // Retireving data from the viewmodel set in profile fragment
//        Users user = sharedViewModel.getUser();
        Users user = LocalRepo.getInstance(getContext()).getLocalUser();
        String[] address = user.getUserAddress().replaceAll(" - ", ", ").split(", ");

        // Setting data
        name.getEditText().setText(user.getUserName());
        phone.getEditText().setText(user.getUserPhone());
        email.getEditText().setText(user.getUserEmail());
//        addressBinding.houseNoInputLayout.getEditText().setText(user.);
        street.getEditText().setText(address[2]);
        wardno.getEditText().setText(address[1]);
        municipality.getEditText().setText(address[0]);
        district.getEditText().setText(address[address.length - 2]);
        province.getEditText().setText(address[address.length - 1]);
    }

    // method to disable all the input text views
    private boolean isViewsEnabled() {
        if (name.isEnabled() ||
                phone.isEnabled() ||
                email.isEnabled() ||
                houseno.isEnabled() ||
                street.isEnabled() ||
                wardno.isEnabled() ||
                municipality.isEnabled() ||
                district.isEnabled() ||
                province.isEnabled()) {

            name.setEnabled(false);
            phone.setEnabled(false);
            email.setEnabled(false);
            houseno.setEnabled(false);
            street.setEnabled(false);
            wardno.setEnabled(false);
            municipality.setEnabled(false);
            district.setEnabled(false);
            province.setEnabled(false);
            return true;
        } else {
            name.setEnabled(true);
            phone.setEnabled(true);
            email.setEnabled(false);
            houseno.setEnabled(true);
            street.setEnabled(true);
            wardno.setEnabled(true);
            municipality.setEnabled(true);
            district.setEnabled(true);
            province.setEnabled(true);
            return false;
        }
    }


    /**
     * Method to store edited data on the firebase firestore
     */
    private void storeData() {
        boolean isInfoChanged = false;
//        Users user = sharedViewModel.getUser();
        Users user = LocalRepo.getInstance(getContext()).getLocalUser();
        // Hashmap for data update
        HashMap<String, Object> hashMap = new HashMap<>();
        // Doc reference object for storing in firestore
        DocumentReference docReference = FirebaseFirestore.getInstance()
                .collection(USERS_COLLECTION)
                .document(user.getUserId());

        // Combined edited Address
        String userAddress = Objects.requireNonNull(municipality.getEditText()).getText().toString().trim()
                + " - " + Objects.requireNonNull(wardno.getEditText()).getText().toString().trim()
                + ", " + Objects.requireNonNull(street.getEditText()).getText().toString().trim()
                + ", " + Objects.requireNonNull(district.getEditText()).getText().toString().trim()
                + ", " + Objects.requireNonNull(province.getEditText()).getText().toString().trim();

        if (!name.getEditText().getText().toString().toLowerCase().trim().equals(user.getUserName().toLowerCase())) {
            hashMap.put("userName", name.getEditText().getText().toString().trim());
            isInfoChanged = true;
        }

        if (!phone.getEditText().getText().toString().trim().equals(user.getUserPhone())) {
            hashMap.put("userPhone", phone.getEditText().getText().toString().trim());
            isInfoChanged = true;
        }

        if (!userAddress.toLowerCase().trim().equals(user.getUserAddress().toLowerCase())) {
            hashMap.put("userAddress", userAddress);
            isInfoChanged = true;
        }


        // Finally updating to firestore
        if (isInfoChanged) {
            docReference.update(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            NotifyUtils.showSnackBar(addressBinding.editSaveButton, getString(R.string.info_updated_message));
                            listentDataChange(user.getUserId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            NotifyUtils.logError(TAG, "storeData", e);
                            NotifyUtils.showToast(getContext(), getString(R.string.error_message));
                        }
                    });

        }
    }

    /**
     * Method to listent data change in firestore database
     */
    private void listentDataChange(String userId) {
        FirebaseFirestore.getInstance()
                .collection(USERS_COLLECTION)
                .document(userId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error == null && value != null) {
                            Users user = value.toObject(Users.class);
                            if (user != null) {
                                LocalRepo.getInstance(getContext()).storeUser(user);
                            }
                        }
                    }
                });
    }
}
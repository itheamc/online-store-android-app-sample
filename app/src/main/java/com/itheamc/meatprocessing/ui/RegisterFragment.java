package com.itheamc.meatprocessing.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itheamc.meatprocessing.MainActivity;
import com.itheamc.meatprocessing.R;
import com.itheamc.meatprocessing.databinding.FragmentRegisterBinding;
import com.itheamc.meatprocessing.models.external.Users;
import com.itheamc.meatprocessing.repositories.LocalRepo;
import com.itheamc.meatprocessing.utilities.LoadingUtils;
import com.itheamc.meatprocessing.utilities.NotifyUtils;
import com.itheamc.meatprocessing.viewmodel.LoginViewModel;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.itheamc.meatprocessing.variables.Constants.USERS_COLLECTION;


public class RegisterFragment extends Fragment {
    private static final String TAG = "RegisterFragment";
    private FragmentRegisterBinding registerBinding;
    private NavController navController;
    private LoginViewModel loginViewModel;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int PERMISSION_REQUEST_CODE = 10101;

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


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        registerBinding = FragmentRegisterBinding.inflate(inflater, container, false);
        return registerBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Calling function to request permission
        checkPremission();

        // Init views
        initViews();

        // Calling function to handle button click
        handleButtonClick();
    }


    /**
     * Initialize views
     */
    private void initViews() {
        name = registerBinding.nameInputLayout;
        phone = registerBinding.phoneInputLayout;
        email = registerBinding.emailInputLayout;
        houseno = registerBinding.houseNoInputLayout;
        street = registerBinding.streetInputLayout;
        wardno = registerBinding.wardNoInputLayout;
        municipality = registerBinding.municipalityInputLayout;
        district = registerBinding.districtInputLayout;
        province = registerBinding.provinceInputLayout;

        // Calling setGotData() to set some known data
        setGotData();
    }

    // Function to set data get from the viewmodel
    private void setGotData() {
        Objects.requireNonNull(name.getEditText()).setText(loginViewModel.getUserName());
        if (loginViewModel.getUserEmail() != null && loginViewModel.getUserEmail().length() > 12) {
            email.getEditText().setText(loginViewModel.getUserEmail());
            email.setEnabled(false);
        }
        if (loginViewModel.getUserMobile() != null && loginViewModel.getUserMobile().length() > 9) {
            phone.getEditText().setText(loginViewModel.getUserMobile());
            phone.setEnabled(false);
        }

    }

    // Checking whether permission is granted or not
    private void checkPremission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }
    }

    // Requesting permission if not allowed
    private void requestPermission() {
        ActivityCompat
                .requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    // Overriding OnRequestPermissionResult method
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                NotifyUtils.logDebug(TAG, "Location Permission Granted");
            } else {
                requestPermission();
            }
        }
    }

    // Function to handle onclick listener on the button view
    private void handleButtonClick() {
        //Setting on click listener on button
        registerBinding.doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInputValidated()) {
                    getCurrentLocation();
                    registerBinding.doneButton.setEnabled(false);
                    LoadingUtils.showProgress(registerBinding.registerProgressBar);
                    changeViewsEnability();
                } else {
                    NotifyUtils.showSnackBar(v, getString(R.string.input_field_validation_error));
                }
            }
        });

    }


    // Getting Current Location
    private void getCurrentLocation() {
        if (getContext() != null) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermission();
            } else {
                LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                    fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location != null) {
                                        double[] latLong = new double[]{location.getLatitude(), location.getLongitude()};
                                        createUser(latLong);    // Calling function to create user
                                    } else {
                                        // Calling another function to request user location
                                        requestUserLocation();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    requestUserLocation();  // Requesting again incase of failure to get location
                                }
                            });

                } else {
                    // If location provider is disabled
                    NotifyUtils.showSnackBar(registerBinding.doneButton, getString(R.string.location_provider_enable_request));
                    LoadingUtils.dismissProgress(registerBinding.registerProgressBar);
                    registerBinding.doneButton.setEnabled(true);
                    changeViewsEnability();
                }

            }

        }   // if getContext() == null ends

    }


    /**
     * Creating function to request network update incase
     * getcurrentlocation() function unable to get user location
     */
    @SuppressLint("MissingPermission")
    private void requestUserLocation() {
        LocationRequest locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(2000)
                .setFastestInterval(1000)
                .setNumUpdates(1);

        // Location Callback
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null) {
                    Location gotLocation = locationResult.getLastLocation();
                    double[] latLong = new double[]{gotLocation.getLatitude(), gotLocation.getLongitude()};
                    createUser(latLong);    // Calling function to create user
                } else {
                    NotifyUtils.showSnackBar(registerBinding.doneButton, getString(R.string.error_message));
                    LoadingUtils.dismissProgress(registerBinding.registerProgressBar);
                    registerBinding.doneButton.setEnabled(true);
                    changeViewsEnability();
                }
            }
        };

        // Finally requesting to get user location
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }


    //Checking whether Nmae is empty or not.
    public boolean validateName() {
        if (name.getEditText() == null || name.getEditText().getText().toString().trim().isEmpty()) {
            name.setError(getString(R.string.empty_name_error_message));
            name.setErrorEnabled(true);
            return false;
        } else {
            name.setError(null);
            return true;
        }
    }

    //Checking whether Phome Number is empty or not.
    public boolean validatePhone() {
        if (phone.getEditText() == null || phone.getEditText().getText().toString().trim().isEmpty()) {
            phone.setError(getString(R.string.empty_phone_error_message));
            phone.setErrorEnabled(true);
            return false;
        } else {
            phone.setError(null);
            return true;
        }
    }


    //Checking whether Street Name is empty or not.
    public boolean validateStreetAddress() {
        if (street.getEditText() == null || street.getEditText().getText().toString().trim().isEmpty()) {
            street.setError(getString(R.string.empty_street_name_error_message));
            street.setErrorEnabled(true);
            return false;
        } else {
            street.setError(null);
            return true;
        }
    }

    //Checking whether Ward No. is empty or not.
    public boolean validateWardNo() {
        if (wardno.getEditText() == null || wardno.getEditText().getText().toString().trim().isEmpty()) {
            wardno.setError(getString(R.string.empty_ward_number_error_message));
            wardno.setErrorEnabled(true);
            return false;
        } else {
            wardno.setError(null);
            return true;
        }
    }

    //Checking whether LocalBody is empty or not.
    public boolean validateLocalBody() {
        if (municipality.getEditText() == null || municipality.getEditText().getText().toString().trim().isEmpty()) {
            municipality.setError(getString(R.string.empty_municipality_error_message));
            municipality.setErrorEnabled(true);
            return false;
        } else {
            municipality.setError(null);
            return true;
        }
    }

    //Checking whether District is empty or not.
    public boolean validateDistrict() {
        if (district.getEditText() == null || district.getEditText().getText().toString().trim().isEmpty()) {
            district.setError(getString(R.string.empty_district_name_error_message));
            district.setErrorEnabled(true);
            return false;
        } else {
            district.setError(null);
            return true;
        }
    }

    //Checking whether Province is empty or not.
    public boolean validateProvince() {
        if (province.getEditText() == null || province.getEditText().getText().toString().trim().isEmpty()) {
            province.setError(getString(R.string.empty_province_name_error_message));
            province.setErrorEnabled(true);
            return false;
        } else {
            province.setError(null);
            return true;
        }
    }


    // function to validate inputed data
    // This is final Validation
    private boolean isInputValidated() {
        return !(!validateName() | !validatePhone() | !validateStreetAddress() | !validateWardNo() | !validateLocalBody() | !validateDistrict() | !validateProvince());
    }

    // Function to create user object
    private void createUser(double[] latLong) {
        String temp_phone = Objects.requireNonNull(phone.getEditText()).getText().toString().trim();
        String userAddress = Objects.requireNonNull(municipality.getEditText()).getText().toString().trim()
                + " - " + Objects.requireNonNull(wardno.getEditText()).getText().toString().trim()
                + ", " + Objects.requireNonNull(street.getEditText()).getText().toString().trim()
                + ", " + Objects.requireNonNull(district.getEditText()).getText().toString().trim()
                + ", " + Objects.requireNonNull(province.getEditText()).getText().toString().trim();
        Users user = new Users(loginViewModel.getUserId(),
                Objects.requireNonNull(name.getEditText()).getText().toString().trim(),
                loginViewModel.getUserProfile(),
                temp_phone.startsWith("+") ? temp_phone : "+977" + temp_phone,
                loginViewModel.getUserEmail(),
                userAddress,
                latLong[0],
                latLong[1]);

        storeInFirestore(user);
    }

    // Function to store users data on firestore
    private void storeInFirestore(Users user) {
        FirebaseFirestore.getInstance()
                .collection(USERS_COLLECTION)
                .document(user.getUserId())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        localStorage(user);
                        NotifyUtils.logDebug(TAG, getString(R.string.user_stored_success_message));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        NotifyUtils.showToast(getContext(), getString(R.string.error_message));
                        NotifyUtils.logError(TAG, "storeInFirestore", e);
                        registerBinding.doneButton.setEnabled(true);
                        LoadingUtils.dismissProgress(registerBinding.registerProgressBar);
                        changeViewsEnability();
                    }
                });
    }

    // Function to store user data in Local database
    private void localStorage(Users user) {
        boolean isStored = LocalRepo.getInstance(getContext())
                .storeUser(user);

        if (isStored) {
            NotifyUtils.showToast(requireContext(), getString(R.string.welcome) + " " + getString(R.string.app_name));
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        } else {
            NotifyUtils.showToast(getContext(), getString(R.string.error_message));
            registerBinding.doneButton.setEnabled(true);
            LoadingUtils.dismissProgress(registerBinding.registerProgressBar);
            changeViewsEnability();
        }
    }


    /**
     * Creating the function to disable all the input layout whenever button is clicked
     * and also enable them whenever error occurred
     */
    // method to disable all the input text views
    private void changeViewsEnability() {
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
        }
    }

}
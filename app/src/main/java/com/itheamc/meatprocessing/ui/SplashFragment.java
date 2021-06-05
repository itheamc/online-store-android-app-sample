package com.itheamc.meatprocessing.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itheamc.meatprocessing.MainActivity;
import com.itheamc.meatprocessing.R;
import com.itheamc.meatprocessing.databinding.FragmentSplashBinding;
import com.itheamc.meatprocessing.models.external.Users;
import com.itheamc.meatprocessing.repositories.LocalRepo;
import com.itheamc.meatprocessing.utilities.NotifyUtils;
import com.itheamc.meatprocessing.viewmodel.LoginViewModel;

import java.util.Locale;

import static com.itheamc.meatprocessing.variables.Constants.ENGLISH_LANGUAGE;
import static com.itheamc.meatprocessing.variables.Constants.USERS_COLLECTION;

public class SplashFragment extends Fragment {
    private static final String TAG = "SplashFragment";
    private FragmentSplashBinding splashBinding;
    private NavController navController;
    private LoginViewModel loginViewModel;
    private FirebaseAuth mAuth;



    public SplashFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        splashBinding = FragmentSplashBinding.inflate(inflater, container, false);
        return splashBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        navController = Navigation.findNavController(view);
        mAuth = FirebaseAuth.getInstance();

        // Change app appearance and language as per the user setting
        adoptUserTheme();   // Calling function to adopt theme as per user setting
        setLanguage();

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Wait 1500 second and then navigate to next screen
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                processingLoginInfo(currentUser);
            }
        }, 1500);
    }




    // Function to process some login info
    private void processingLoginInfo(FirebaseUser currentUser) {
        if (getContext() != null) {
            if (isSignedIn(currentUser)) {
                if (LocalRepo.getInstance(getContext()).getLocalUser() == null ||
                        LocalRepo.getInstance(getContext()).getLocalUser().getUserName().equals("null")) {
                    //if not found in cloud move to register fragment
                    loginViewModel.setUserId(currentUser.getUid());
                    loginViewModel.setUserName(currentUser.getDisplayName());
                    loginViewModel.setUserEmail(currentUser.getEmail());
                    loginViewModel.setUserProfile(String.valueOf(currentUser.getPhotoUrl()));
                    // Switching to another fragment for other details
                    navController.navigate(R.id.action_splashFragment_to_registerFragment);

                } else {
                    Intent intent = new Intent(requireActivity(), MainActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                }
            } else {
                if (LocalRepo.getInstance(getContext()).getLanguageData().equals("null")) {
                    // If language is not set previously then
                    navController.navigate(R.id.action_splashFragment_to_languageFragment);
                } else {
                    // if language is set previously then
                    navController.navigate(R.id.action_splashFragment_to_loginFragment);
                }
            }
        }
    }

    // method to check weather user is already logged in or not
    private boolean isSignedIn(FirebaseUser firebaseUser) {
        // checking if account is null
        return firebaseUser != null;
    }


    /**
     * Creating the function to change app theme as per the user setting
     * Dark Mode
     * or Light mode
     */
    private void adoptUserTheme() {
        if (getContext() != null) {
            AppCompatDelegate.setDefaultNightMode(LocalRepo.getInstance(getContext()).getAppearanceData());
        }
    }

    // Function to change the app language as per the user setting
    public void setLanguage() {
        if (Build.VERSION.SDK_INT > 16 && getContext() != null) {
            // Retrieving user language fron the local storage
            String lanCode = LocalRepo.getInstance(getContext()).getLanguageData();
            if (lanCode.toLowerCase().equals("null")) {
                lanCode = ENGLISH_LANGUAGE;
            }

            Locale locale = new Locale(lanCode);
            Locale.setDefault(locale);
            Resources resources = getResources();
            Configuration config = resources.getConfiguration();
            config.setLocale(locale);
            resources.updateConfiguration(config, resources.getDisplayMetrics());
        }
    }
}
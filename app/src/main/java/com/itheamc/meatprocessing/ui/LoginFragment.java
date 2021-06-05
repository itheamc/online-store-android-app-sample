package com.itheamc.meatprocessing.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Session2Command;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.NavigatorProvider;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itheamc.meatprocessing.MainActivity;
import com.itheamc.meatprocessing.R;
import com.itheamc.meatprocessing.databinding.FragmentLoginBinding;
import com.itheamc.meatprocessing.models.external.Users;
import com.itheamc.meatprocessing.repositories.LocalRepo;
import com.itheamc.meatprocessing.utilities.NetworkUtils;
import com.itheamc.meatprocessing.utilities.NotifyUtils;
import com.itheamc.meatprocessing.viewmodel.LoginViewModel;

import java.util.Objects;

import static com.itheamc.meatprocessing.variables.Constants.PRIVACY_POLICY_URL;
import static com.itheamc.meatprocessing.variables.Constants.TERMS_CONDITIONS_URL;
import static com.itheamc.meatprocessing.variables.Constants.USERS_COLLECTION;


public class LoginFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "LoginFragment";
    public static final int SIGN_IN_REQUEST = 10101;
    private FragmentLoginBinding loginBinding;
    private NavController navController;
    private LoginViewModel loginViewModel;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        loginBinding = FragmentLoginBinding.inflate(inflater, container, false);
        return loginBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        // Implementing onClick listener on views
        loginBinding.privacyPolicy.setOnClickListener(this);
        loginBinding.termsConditions.setOnClickListener(this);
        loginBinding.googleSigninButton.setOnClickListener(this);
        loginBinding.mobileSigninButton.setOnClickListener(this);

    }

    /**
     * Overriding function to handle onClick listener
     * on all the views and buttons
     */
    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == loginBinding.privacyPolicy.getId()) {
            LocalRepo.getInstance(getContext()).storeUrl(PRIVACY_POLICY_URL);
            new WebView(getContext()).loadUrl(PRIVACY_POLICY_URL);

        } else if (viewId == loginBinding.termsConditions.getId()) {
            LocalRepo.getInstance(getContext()).storeUrl(TERMS_CONDITIONS_URL);
            new WebView(getContext()).loadUrl(TERMS_CONDITIONS_URL);

        } else if (viewId == loginBinding.googleSigninButton.getId()) {
            if (getContext() != null && getActivity() != null) {
                if (NetworkUtils.isConnected(getContext())) {
                    handleButtonEnability();
                    signIn();   // Calling function to start signing activity
                } else {
                    showConnectionDialog();
                }
            } else {
                NotifyUtils.showSnackBar(loginBinding.loginConstraintLayout, getString(R.string.error_message));
            }
        } else if (viewId == loginBinding.mobileSigninButton.getId()) {
            if (getContext() != null && getActivity() != null) {
                if (NetworkUtils.isConnected(getContext())) {
                    navController.navigate(R.id.action_loginFragment_to_mobileLoginFragment);
                } else {
                    showConnectionDialog();
                }
            } else {
                NotifyUtils.showSnackBar(loginBinding.loginConstraintLayout, getString(R.string.error_message));
            }
        } else {
            NotifyUtils.logDebug(TAG, "Unspecified view's clicked");
        }
    }

    /*
    Creating function
    to create the
    custom dialog
    to inform the user to
    connect to the
    internet
     */

    private void showConnectionDialog() {
        if (getContext() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(R.string.internet_connection_error)
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.connect), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Sending user to the wifi connection setting
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.show();     // Finally showing the builder
        }
    }


    // Method to handleSignIn
    private void signIn() {
        // Configure sign-in to request the user's ID, email address, and basic profile.
        // ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.client_id))
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        if (getContext() != null) {
            GoogleSignInClient signInClient = GoogleSignIn.getClient(getContext(), googleSignInOptions);
            Intent signInIntent = signInClient.getSignInIntent();
            startActivityForResult(signInIntent, SIGN_IN_REQUEST);
        } else {
            handleButtonEnability();
        }
    }


    // Overriding OnActivityResult method
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == SIGN_IN_REQUEST && data != null) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            GoogleSignIn.getSignedInAccountFromIntent(data)
                    .addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
                        @Override
                        public void onSuccess(GoogleSignInAccount signInAccount) {
                            firebaseAuthWithGoogle(signInAccount.getIdToken());
                            NotifyUtils.logDebug(TAG, signInAccount.getIdToken());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            handleButtonEnability();
                            NotifyUtils.logError(TAG, "onActivityResult", e);
                            NotifyUtils.showToast(getContext(), getString(R.string.unable_to_login_message));
                        }
                    });
        } else {
            handleButtonEnability();
            NotifyUtils.showToast(getContext(), getString(R.string.unable_to_login_message));
        }
    }


    /**
     * After a user successfully signs in,
     * get an ID token from the GoogleSignInAccount object,
     * exchange it for a Firebase credential,
     * and authenticate with Firebase using the Firebase credential
     */
    private void firebaseAuthWithGoogle(String idToken) {
        // Getting instance of the FirebaseAuth
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (authResult != null && authResult.getUser() != null) {
                            FirebaseUser user = authResult.getUser();
                            NotifyUtils.logDebug(TAG, "signInWithCredential:success");
                            checkUsers(user);
                        } else {
                            // If authResult is null.
                            handleButtonEnability();
                            NotifyUtils.showSnackBarWithoutAnchorView(loginBinding.loginConstraintLayout, getString(R.string.error_message));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // If sign in fails, display a message to the user.
                        handleButtonEnability();
                        NotifyUtils.logError(TAG, "firebaseAuthWithGoogle", e);
                        NotifyUtils.showSnackBarWithoutAnchorView(loginBinding.loginConstraintLayout, getString(R.string.authentication_failed_message));
                    }
                });
    }


    //Function to check whether user is already registered user or not
    private void checkUsers(FirebaseUser firebaseUser) {
        String userId = firebaseUser.getUid();

        FirebaseFirestore.getInstance()
                .collection(USERS_COLLECTION)
                .document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot != null && documentSnapshot.getString("userId") != null) {
                            NotifyUtils.showToast(requireContext(), getString(R.string.welcome_to_existing_user) + " " + getString(R.string.app_name));
                            NotifyUtils.logDebug(TAG, "Existing User");
                            Users user = documentSnapshot.toObject(Users.class);

                            boolean isStored = LocalRepo.getInstance(requireContext()).storeUser(user);
                            if (isStored) {
                                Intent intent = new Intent(requireActivity(), MainActivity.class);
                                startActivity(intent);
                                requireActivity().finish();
                            } else {
                                handleButtonEnability();
                                NotifyUtils.showToast(getContext(), getString(R.string.error_message));
                            }

                        } else {
                            //if not found in cloud move to register fragment
                            loginViewModel.setUserId(userId);
                            loginViewModel.setUserName(firebaseUser.getDisplayName());
                            loginViewModel.setUserEmail(firebaseUser.getEmail());
                            loginViewModel.setUserProfile(String.valueOf(firebaseUser.getPhotoUrl()));
                            // Switching to another fragment for other details
                            navController.navigate(R.id.action_loginFragment_to_registerFragment);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        NotifyUtils.logError(TAG, "checkUsers", e);
                        // Switching to another fragment for other details
                        navController.navigate(R.id.action_loginFragment_to_registerFragment);
                    }
                });

    }


    /**
     * Creating function to handle button enability
     * i.e. enable or disable button
     */
    private void handleButtonEnability() {
        loginBinding.googleSigninButton.setEnabled(!loginBinding.googleSigninButton.isEnabled());
    }

}
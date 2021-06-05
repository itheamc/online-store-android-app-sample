package com.itheamc.meatprocessing.ui;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.HarmfulAppsData;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.internal.RecaptchaActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itheamc.meatprocessing.MainActivity;
import com.itheamc.meatprocessing.R;
import com.itheamc.meatprocessing.databinding.FragmentMobileLoginBinding;
import com.itheamc.meatprocessing.models.external.Users;
import com.itheamc.meatprocessing.repositories.LocalRepo;
import com.itheamc.meatprocessing.utilities.NotifyUtils;
import com.itheamc.meatprocessing.viewmodel.LoginViewModel;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import static com.itheamc.meatprocessing.variables.Constants.USERS_COLLECTION;

public class MobileLoginFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MobileLoginFragment";
    private FragmentMobileLoginBinding mobileLoginBinding;
    private NavController navController;
    private LoginViewModel loginViewModel;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    // Variabler
    String enteredPhone = null;
    String verificationId = null;
    PhoneAuthProvider.ForceResendingToken resendingToken = null;

    public MobileLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mobileLoginBinding = FragmentMobileLoginBinding.inflate(inflater, container, false);
        return mobileLoginBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        mAuth = FirebaseAuth.getInstance();

        mobileLoginBinding.button.setOnClickListener(this);
        mobileLoginBinding.resendCode.setOnClickListener(this);

        requestFocusOnEditText();


        // Creating callback object
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                // When the code is successfully verified
                String gotCode = phoneAuthCredential.getSmsCode();
                if (gotCode != null && !TextUtils.isEmpty(gotCode)) {
                    verifyCode(gotCode);
                }

                changeProgressVisibility();

            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                NotifyUtils.showToast(getContext(), getString(R.string.unable_to_register));
                NotifyUtils.logError(TAG, "onVerificationFailed", e);
                changeProgressVisibility();

            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                // Setting the verificationId
                verificationId = s;
                resendingToken = forceResendingToken;
                if (isNumberInputLayoutVisible()) {
                    changeLayoutVisibility();
                }
                changeProgressVisibility();
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                resendVisibility();     // Make the resend text visible


            }
        };

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        if (v.getId() == mobileLoginBinding.button.getId()) {
            if (isNumberInputLayoutVisible()) {
                enteredPhone = mobileLoginBinding.phoneCodeEdittext.getText().toString().trim() +
                        mobileLoginBinding.phoneNumberEdittext.getText().toString().trim();

                if (enteredPhone != null && !TextUtils.isEmpty(enteredPhone)) {
                    changeProgressVisibility();
                    sendCode();
                }
            } else {
                String enteredCode = mobileLoginBinding.code1.getText().toString().trim() +
                        mobileLoginBinding.code2.getText().toString().trim() +
                        mobileLoginBinding.code3.getText().toString().trim() +
                        mobileLoginBinding.code4.getText().toString().trim() +
                        mobileLoginBinding.code5.getText().toString().trim() +
                        mobileLoginBinding.code6.getText().toString().trim();

                if (enteredCode != null && !TextUtils.isEmpty(enteredCode)) {
                    changeProgressVisibility();
                    verifyCode(enteredCode);
                }
            }
        } else if (v.getId() == mobileLoginBinding.resendCode.getId()) {
            changeProgressVisibility();
            resendVisibility();
            resendCode();
        }
    }


    // Function to start phone sign in
    private void sendCode() {
        if (getActivity() != null) {
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(enteredPhone)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(getActivity())                 // Activity (for callback binding)
                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        } else {
            NotifyUtils.showToast(getContext(), getString(R.string.error_message));
        }
    }

    // Function to start phone sign in
    private void resendCode() {
        if (getActivity() != null) {
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(enteredPhone)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(getActivity())                 // Activity (for callback binding)
                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                            .setForceResendingToken(resendingToken)
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        } else {
            NotifyUtils.showToast(getContext(), getString(R.string.error_message));
        }
    }


    // Function to verify code when verify button clicked
    private void verifyCode(String enteredCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, enteredCode);
        signInWithPhoneAuthCredential(credential);
    }


    // Function to handle user data after verification of code
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (authResult != null && authResult.getUser() != null) {
                            FirebaseUser user = authResult.getUser();
                            NotifyUtils.logDebug(TAG, "signInWithCredential:success");
                            checkUsers(user);
                        } else {
                            // If authResult is null.
                            NotifyUtils.showToast(getContext(), getString(R.string.error_message));
                            changeProgressVisibility();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // If sign in fails, display a message to the user.
                        NotifyUtils.logError(TAG, "firebaseAuthWithPhone", e);
                        NotifyUtils.showToast(getContext(), getString(R.string.invalid_varification_code));
                        changeProgressVisibility();
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
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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

                                NotifyUtils.showToast(getContext(), getString(R.string.error_message));
                                changeProgressVisibility();
                            }

                        } else {
                            //if not found in cloud move to register fragment
                            loginViewModel.setUserId(userId);
                            loginViewModel.setUserName(firebaseUser.getDisplayName());
                            loginViewModel.setUserEmail(firebaseUser.getEmail());
                            loginViewModel.setUserMobile(enteredPhone);
                            loginViewModel.setUserProfile(String.valueOf(firebaseUser.getPhotoUrl()));
                            // Switching to another fragment for other details
                            navController.navigate(R.id.action_mobileLoginFragment_to_registerFragment);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        NotifyUtils.logError(TAG, "checkUsers", e);
                        // Switching to another fragment for other details
                        navController.navigate(R.id.action_mobileLoginFragment_to_registerFragment);
                    }
                });

    }


//    // Function to set code automatically in the text box
//    private void enterCode(String s) {
//        mobileLoginBinding.code1.setText(s.charAt(0));
//        mobileLoginBinding.code1.setText(s.charAt(1));
//        mobileLoginBinding.code1.setText(s.charAt(2));
//        mobileLoginBinding.code1.setText(s.charAt(3));
//        mobileLoginBinding.code1.setText(s.charAt(4));
//        mobileLoginBinding.code1.setText(s.charAt(5));
//    }


    // Function to make layout visible or invisible
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void changeLayoutVisibility() {
        if (isNumberInputLayoutVisible()) {
            mobileLoginBinding.enterNumberLayout.setVisibility(View.INVISIBLE);
            mobileLoginBinding.verifyCodeLayout.setVisibility(View.VISIBLE);
            mobileLoginBinding.labelText.setText(R.string.enter_the_varification_code);
            mobileLoginBinding.button.setText(R.string.verify_code);
            TransitionManager.beginDelayedTransition(mobileLoginBinding.mobileLoginConstraintLayout);
        } else {
            mobileLoginBinding.enterNumberLayout.setVisibility(View.VISIBLE);
            mobileLoginBinding.verifyCodeLayout.setVisibility(View.INVISIBLE);
            mobileLoginBinding.labelText.setText(R.string.enter_your_mobile_number_login);
            mobileLoginBinding.button.setText(R.string.sent_otp);
            TransitionManager.beginDelayedTransition(mobileLoginBinding.mobileLoginConstraintLayout);
        }
    }

    // Function to decide what function to call - sendCode() or verifyCode()
    private boolean isNumberInputLayoutVisible() {
        return mobileLoginBinding.enterNumberLayout.getVisibility() == View.VISIBLE;
    }


    // Function to change visibility of the resend textview
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void resendVisibility() {
        if (mobileLoginBinding.resendcodeLayout.getVisibility() == View.VISIBLE) {
            mobileLoginBinding.resendcodeLayout.setVisibility(View.INVISIBLE);
            TransitionManager.beginDelayedTransition(mobileLoginBinding.mobileLoginConstraintLayout);
        } else {
            mobileLoginBinding.resendcodeLayout.setVisibility(View.VISIBLE);
            TransitionManager.beginDelayedTransition(mobileLoginBinding.mobileLoginConstraintLayout);
        }
    }


    // Function to change progressbar visibility
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void changeProgressVisibility() {
        if (mobileLoginBinding.progressBar.getVisibility() == View.VISIBLE) {
            mobileLoginBinding.progressBar.setVisibility(View.INVISIBLE);
            mobileLoginBinding.button.setEnabled(true);
            TransitionManager.beginDelayedTransition(mobileLoginBinding.mobileLoginConstraintLayout);
        } else {
            mobileLoginBinding.progressBar.setVisibility(View.VISIBLE);
            mobileLoginBinding.button.setEnabled(false);
            TransitionManager.beginDelayedTransition(mobileLoginBinding.mobileLoginConstraintLayout);
        }
    }

    // Function to change focuses on the otp edit text
    private void requestFocusOnEditText() {
        mobileLoginBinding.code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mobileLoginBinding.code1.getText().toString().isEmpty()) {
                    mobileLoginBinding.code2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mobileLoginBinding.code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mobileLoginBinding.code2.getText().toString().isEmpty()) {
                    mobileLoginBinding.code3.requestFocus();
                } else {
                    mobileLoginBinding.code1.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mobileLoginBinding.code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mobileLoginBinding.code3.getText().toString().isEmpty()) {
                    mobileLoginBinding.code4.requestFocus();
                } else {
                    mobileLoginBinding.code2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mobileLoginBinding.code4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mobileLoginBinding.code4.getText().toString().isEmpty()) {
                    mobileLoginBinding.code5.requestFocus();
                } else {
                    mobileLoginBinding.code3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mobileLoginBinding.code5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mobileLoginBinding.code5.getText().toString().isEmpty()) {
                    mobileLoginBinding.code6.requestFocus();
                } else {
                    mobileLoginBinding.code4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mobileLoginBinding.code6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mobileLoginBinding.code6.getText().toString().isEmpty()) {
                    mobileLoginBinding.code5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

}
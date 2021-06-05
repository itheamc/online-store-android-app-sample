package com.itheamc.meatprocessing.ui;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.itheamc.meatprocessing.R;
import com.itheamc.meatprocessing.databinding.FragmentLanguageBinding;
import com.itheamc.meatprocessing.databinding.FragmentLoginBinding;
import com.itheamc.meatprocessing.repositories.LocalRepo;
import com.itheamc.meatprocessing.utilities.NotifyUtils;
import com.itheamc.meatprocessing.viewmodel.LoginViewModel;

import java.util.Locale;

import static com.itheamc.meatprocessing.variables.Constants.DARK_MODE;
import static com.itheamc.meatprocessing.variables.Constants.ENGLISH_LANGUAGE;
import static com.itheamc.meatprocessing.variables.Constants.FOLLOW_SYSTEM;
import static com.itheamc.meatprocessing.variables.Constants.LIGHT_MODE;
import static com.itheamc.meatprocessing.variables.Constants.NEPALI_LANGUAGE;
import static com.itheamc.meatprocessing.variables.Constants.THARU_LANGUAGE;

public class LanguageFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "LanguageFragment";
    private FragmentLanguageBinding languageBinding;
    private NavController navController;
    private LoginViewModel loginViewModel;

    public LanguageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        languageBinding = FragmentLanguageBinding.inflate(inflater, container, false);
        return languageBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        // Calling method to handle onItemSelected listener
        languageBinding.languageRadioGroup.setOnCheckedChangeListener(this);
        // Calling method to handle button onClickListener
        languageBinding.languageNextButton.setOnClickListener(this);

    }

    // Creating method to handle OnClickListener on button
    @Override
    public void onClick(View v) {
        if (v.getId() == languageBinding.languageNextButton.getId()) {
            if (languageBinding.englishLanguage.isChecked() || languageBinding.nepaliLanguage.isChecked() || languageBinding.tharuLanguage.isChecked()) {
                navController.navigate(R.id.action_languageFragment_to_loginFragment);
            } else {
                NotifyUtils.showSnackBar(languageBinding.languageNextButton, getString(R.string.language_unchecked_error_message));
            }
        }
    }


    // radio group onChecked listener
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // Enable next button
        languageBinding.languageNextButton.setEnabled(true);

        if (group.getId() == languageBinding.languageRadioGroup.getId()) {
            if (getContext() != null) {
                LocalRepo localRepo = LocalRepo.getInstance(getContext());
                if (checkedId == R.id.english_language) {
                    setLanguage(ENGLISH_LANGUAGE);
                    localRepo.storeLanguageData(ENGLISH_LANGUAGE);
                } else if (checkedId == R.id.nepali_language) {
                    setLanguage(NEPALI_LANGUAGE);
                    localRepo.storeLanguageData(NEPALI_LANGUAGE);
                } else {
                    setLanguage(THARU_LANGUAGE);
                    localRepo.storeLanguageData(THARU_LANGUAGE);
                }
            } else {
                NotifyUtils.showSnackBar(languageBinding.languageNextButton, getString(R.string.error_message));
                NotifyUtils.logDebug(TAG, "getContext() is null");
            }
        }

    }


    // Function to change language as per user selection
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }


}
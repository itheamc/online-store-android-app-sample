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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.itheamc.meatprocessing.R;
import com.itheamc.meatprocessing.databinding.FragmentSettingBinding;
import com.itheamc.meatprocessing.models.external.Users;
import com.itheamc.meatprocessing.repositories.LocalRepo;
import com.itheamc.meatprocessing.utilities.NotifyUtils;

import java.util.Locale;

import static com.itheamc.meatprocessing.variables.Constants.DARK_MODE;
import static com.itheamc.meatprocessing.variables.Constants.ENGLISH_LANGUAGE;
import static com.itheamc.meatprocessing.variables.Constants.FOLLOW_SYSTEM;
import static com.itheamc.meatprocessing.variables.Constants.LIGHT_MODE;
import static com.itheamc.meatprocessing.variables.Constants.NEPALI_LANGUAGE;
import static com.itheamc.meatprocessing.variables.Constants.THARU_LANGUAGE;

public class SettingFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "SettingFragment";
    private FragmentSettingBinding settingBinding;
    private NavController navController;

    // Creating some int variable to control the drawable of setting
    private int apperance = 0;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        settingBinding = FragmentSettingBinding.inflate(inflater, container, false);
        return settingBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        checkedItems();     // Calling function to make check in the radio group as per previous setting

        // Implementing on click listener
        settingBinding.accountIcon.setOnClickListener(this);
        settingBinding.userNameTextview.setOnClickListener(this);
        settingBinding.appearanceSettingTitle.setOnClickListener(this);
        settingBinding.languageSettingTitle.setOnClickListener(this);

        // Implementing onChecked listener on radio group
        settingBinding.apperanceRadioGroup.setOnCheckedChangeListener(this);
        settingBinding.languageRadioGroup.setOnCheckedChangeListener(this);

        // Passing data to databinding variables
        if (getContext() != null) {
            Users user = LocalRepo.getInstance(getContext()).getLocalUser();
            settingBinding.setName(user.getUserName());
            settingBinding.setImageurl(user.getUserProfile());
        } else {
            settingBinding.setName(getString(R.string.guest));
        }

    }

    /**
     * Method to Handle onClick listener on views
     * Views may be -
     * TextViews, Buttons etc.
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == settingBinding.accountIcon.getId() || id == settingBinding.userNameTextview.getId()) {
            navController.navigate(R.id.action_settingFragment_to_profileFragment);
        } else if (id == settingBinding.appearanceSettingTitle.getId()) {
            handleAppearance();
        } else if (id == settingBinding.languageSettingTitle.getId()) {
            handleLanguages();
        } else {
            NotifyUtils.logDebug(TAG, "Unspecified views clicked");
        }
    }


    /**
     * Method to handle appearance_setting
     * This function will be responsible for the
     * visibility of the appearance radio group
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleAppearance() {
        TransitionManager.beginDelayedTransition(settingBinding.settingConstraintLayout);
        if (settingBinding.apperanceRadioGroup.getVisibility() == View.GONE) {
            apperance = 1;
            settingBinding.setApperance(apperance);
            settingBinding.apperanceRadioGroup.setVisibility(View.VISIBLE);
        } else {
            apperance = 0;
            settingBinding.setApperance(apperance);
            settingBinding.apperanceRadioGroup.setVisibility(View.GONE);
        }

    }

    // Method to handle language
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleLanguages() {
        TransitionManager.beginDelayedTransition(settingBinding.settingConstraintLayout);
        if (settingBinding.languageRadioGroup.getVisibility() == View.GONE) {
            apperance = 1;
            settingBinding.setLanguage(apperance);
            settingBinding.languageRadioGroup.setVisibility(View.VISIBLE);
        } else {
            apperance = 0;
            settingBinding.setLanguage(apperance);
            settingBinding.languageRadioGroup.setVisibility(View.GONE);
        }
    }

    // radio group onChecked listener
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (getContext() != null) {
            LocalRepo localRepo = LocalRepo.getInstance(getContext());

            if (group.getId() == settingBinding.apperanceRadioGroup.getId()) {
                if (checkedId == R.id.dark_mode_theme) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    localRepo.storeAppearanceData(DARK_MODE);
                } else if (checkedId == R.id.light_mode_theme) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    localRepo.storeAppearanceData(LIGHT_MODE);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    localRepo.storeAppearanceData(FOLLOW_SYSTEM);
                }
            } else if (group.getId() == settingBinding.languageRadioGroup.getId()) {
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
                NotifyUtils.logDebug(TAG, "option from the unspecified radio group is checked");
            }
        } else {
            NotifyUtils.logDebug(TAG, "getContext() is getting null result");
        }
    }


    // Function to make checked as per the user setting
    private void checkedItems() {
        if (getContext() != null) {
            LocalRepo localRepo = LocalRepo.getInstance(getContext());

            // For AppearanceRadio Group
            int mode = localRepo.getAppearanceData();
            switch (mode) {
                case DARK_MODE:
                    settingBinding.darkModeTheme.setChecked(true);
                    break;
                case LIGHT_MODE:
                    settingBinding.lightModeTheme.setChecked(true);
                    break;
                default:
                    settingBinding.followSystemTheme.setChecked(true);
                    break;
            }

            // For Language Radio Group
            String lang = localRepo.getLanguageData();
            switch (lang) {
                case ENGLISH_LANGUAGE:
                    settingBinding.englishLanguage.setChecked(true);
                    break;
                case NEPALI_LANGUAGE:
                    settingBinding.nepaliLanguage.setChecked(true);
                    break;
                default:
                    settingBinding.tharuLanguage.setChecked(true);
                    break;
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
//        getActivity().recreate();   // Only for see real time changes
    }


}
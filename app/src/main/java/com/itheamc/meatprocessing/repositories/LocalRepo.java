package com.itheamc.meatprocessing.repositories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.itheamc.meatprocessing.models.external.Users;

import java.util.HashMap;

import static com.itheamc.meatprocessing.variables.Constants.APPEARANCE_SETTING;
import static com.itheamc.meatprocessing.variables.Constants.ENGLISH_LANGUAGE;
import static com.itheamc.meatprocessing.variables.Constants.FOLLOW_SYSTEM;
import static com.itheamc.meatprocessing.variables.Constants.LANGUAGE_SETTING;
import static com.itheamc.meatprocessing.variables.Constants.SETTING_DATA_KEYWORD;
import static com.itheamc.meatprocessing.variables.Constants.USER_ADDRESS;
import static com.itheamc.meatprocessing.variables.Constants.USER_DATA_KEYWORD;
import static com.itheamc.meatprocessing.variables.Constants.USER_EMAIL;
import static com.itheamc.meatprocessing.variables.Constants.USER_ID;
import static com.itheamc.meatprocessing.variables.Constants.USER_IMAGE;
import static com.itheamc.meatprocessing.variables.Constants.USER_LOCLAT;
import static com.itheamc.meatprocessing.variables.Constants.USER_LOCLONG;
import static com.itheamc.meatprocessing.variables.Constants.USER_NAME;
import static com.itheamc.meatprocessing.variables.Constants.USER_PHONE;

public class LocalRepo {
    @SuppressLint("StaticFieldLeak")
    private static LocalRepo instance;
    private final Context mContext;

    // Constructor
    public LocalRepo(Context context) {
        this.mContext = context;
    }

    // Getter for the instance of the LocalReps class
    public static LocalRepo getInstance(Context context) {
        if (instance == null) {
            instance = new LocalRepo(context);
        }


        return instance;
    }

    // Function to store user data in local storage
    public boolean storeUser(Users user) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_DATA_KEYWORD, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Putting data to local Storage
        editor.putString(USER_ID, user.getUserId());
        editor.putString(USER_NAME, user.getUserName());
        editor.putString(USER_IMAGE, user.getUserProfile());
        editor.putString(USER_PHONE, user.getUserPhone());
        editor.putString(USER_EMAIL, user.getUserEmail());
        editor.putString(USER_ADDRESS, user.getUserAddress());
        editor.putString(USER_LOCLAT, String.valueOf(user.getLocLat()));
        editor.putString(USER_LOCLONG, String.valueOf(user.getLocLong()));
        editor.apply();

        return true;
    }

    // Function to return User object by processing data from loacal storage
    public Users getLocalUser() {
        //Retrieve data from the local storage
        SharedPreferences retrieveData = mContext.getSharedPreferences(USER_DATA_KEYWORD, Context.MODE_PRIVATE);

        return new Users( retrieveData.getString(USER_ID, "null"),
                retrieveData.getString(USER_NAME, "null"),
                retrieveData.getString(USER_IMAGE, "null"),
                retrieveData.getString(USER_PHONE, "null"),
                retrieveData.getString(USER_EMAIL, "null"),
                retrieveData.getString(USER_ADDRESS, "null"),
                Double.parseDouble(retrieveData.getString(USER_LOCLAT, "27.4546")),
                Double.parseDouble(retrieveData.getString(USER_LOCLONG, "82.3456")));

    }

    //Functions to clear data from local storage
    public void clearUserData() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_DATA_KEYWORD, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
    }

    // Method to store appearance setting data to local storage
    public void storeAppearanceData(int themeMode) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SETTING_DATA_KEYWORD, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Putting data to local Storage

        editor.putInt(APPEARANCE_SETTING, themeMode);
        editor.apply();
    }

    // Method to retrieve appearance sata from the local storage
    public int getAppearanceData() {
        //Retrieve data from the local storage
        SharedPreferences retrieveData = mContext.getSharedPreferences(SETTING_DATA_KEYWORD, Context.MODE_PRIVATE);
        return retrieveData.getInt(APPEARANCE_SETTING, FOLLOW_SYSTEM);
    }

    // Method to store Language setting data to local storage
    public void storeLanguageData(String language) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SETTING_DATA_KEYWORD, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Putting data to local Storage
        editor.putString(LANGUAGE_SETTING, language);
        editor.apply();
    }

    // Method to retrieve language data from the local storage
    public String getLanguageData() {
        //Retrieve data from the local storage
        SharedPreferences retrieveData = mContext.getSharedPreferences(SETTING_DATA_KEYWORD, Context.MODE_PRIVATE);
        return retrieveData.getString(LANGUAGE_SETTING, "null");
    }

    /**
     * Creating function to store temporary url for webview
     * and also creating function to retrieve it
     */
    public void storeUrl(String url) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("URL_DATA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Putting data to local Storage
        editor.putString("url", url);
        editor.apply();
    }

    public String getUrl() {
        //Retrieve data from the local storage
        SharedPreferences retrieveData = mContext.getSharedPreferences("URL_DATA", Context.MODE_PRIVATE);
        return retrieveData.getString("url", "https://itheamc.blogspot.com");
    }

    /**
     * Creating method to store signIn info
     * It will store boolean value
     * i.e. is User (existing or new) using app first time
     */
    public boolean isOpeningFirstTime() {
        //Retrieve data from the local storage
        SharedPreferences sharedPreference = mContext.getSharedPreferences("LangInfo", Context.MODE_PRIVATE);

        boolean isNew = sharedPreference.getBoolean("isnew", true);
        if (isNew) {
            SharedPreferences.Editor editor = sharedPreference.edit();
            // Putting data to local Storage
            editor.putBoolean("isnew", false);
            editor.apply();
            return true;
        } else {
            return false;
        }
    }

}

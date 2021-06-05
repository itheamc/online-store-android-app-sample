package com.itheamc.meatprocessing.variables;

import androidx.appcompat.app.AppCompatDelegate;

public class Constants {

    // These are some view types for the home screen
    public static final int PRICE_TAG_VIEW = 1000;
    public static final int PRODUCT_VIEW = 1001;
    public static final int SLIDER_VIEW = 1002;
    public static final int PREBOOKED_VIEW = 1003;
    public static final int INFO_CARD_VIEW = 1004;

    // View Type for the cart fragment (cart_item_view.xml) and order fragment (confirmed_item_view)
    public static final int CART_FRAGMENT = 101;
    public static final int ORDER_FRAGMENT = 102;


    // Product Types
    public static final String REGULAR_TYPE = "regular";
    public static final String PREBOOK_TYPE = "prebook";

    public static final String USER_DATA_KEYWORD = "USERINFO";
    public static final String USER_NAME = "UserName";
    public static final String USER_ID = "UserId";
    public static final String USER_PHONE = "UserPhome";
    public static final String USER_IMAGE = "UserImage";
    public static final String USER_ADDRESS = "UserAddress";
    public static final String USER_EMAIL = "UserEmail";
    public static final String USER_LOCLAT = "LocLat";
    public static final String USER_LOCLONG = "LocLong";

    // Order
    public static final String ORDER_UNCONFIRMED = "unconfirmed";
    public static final String ORDER_UNDER_PROCESS = "confirmed";
    public static final String ORDER_ON_THE_WAY = "ontheway";
    public static final String ORDER_COMPLETED = "completed";
    public static final String ORDER_CANCELLED = "cancelled";
    public static final String ORDER_RETURNED = "returned";
    public static final String ORDER_REJECTED = "rejected";


    //Payments Method
    public static final String CASH_ON_DELIVERY = "CashonDelivery";
    public static final String ONLINE_PAYMENT = "OnlinePayment";

    // Firestore Collection List
    public static final String ORDERS_COLLECTION = "Orders";
    public static final String USERS_COLLECTION = "Users";
    public static final String PRODUCT_COLLECTION = "Products";
    public static final String SLIDER_COLLECTION = "Sliders";
    public static final String INFO_COLLECTION = "Infos";
    public static final String SALES_POINTS_COLLECTION = "SalesPoints";
    public static final String CATEGORIES_COLLECTION = "Categories";
    public static final String PRICE_COLLECTION = "Prices";

    // Order handling fragment
    public static final String UNCONFIRMED_ORDER_FRAGMENT = "unconfirmed";
    public static final String CONFIRMED_ORDER_FRAGMENT = "confirmed";
    public static final String DELIVERING_ORDER_FRAGMENT = "ontheway";
    public static final String ORDERED_HISTORY_FRAGMENT = "completed";

    // Locla repo setting fragment keywords for sharedpreference
    public static final String SETTING_DATA_KEYWORD = "SettingData";
    public static final String APPEARANCE_SETTING = "appearence";
    public static final String LANGUAGE_SETTING = "language";


    // Language keywords
    public static final String ENGLISH_LANGUAGE = "en";
    public static final String NEPALI_LANGUAGE = "ne";
    public static final String THARU_LANGUAGE = "th";

    // Appearance keywords
    public static final int DARK_MODE = AppCompatDelegate.MODE_NIGHT_YES;
    public static final int LIGHT_MODE = AppCompatDelegate.MODE_NIGHT_NO;
    public static final int FOLLOW_SYSTEM = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;

    // URL for the privacy policy and Terms and conditions
    public static final String PRIVACY_POLICY_URL = "https://google.com";
    public static final String TERMS_CONDITIONS_URL = "https://google.com";
    public static final String USER_ICON_URL = "https://firebasestorage.googleapis.com/v0/b/meat-processing.appspot.com/o/userimg%2Fuser.jpg?alt=media&token=79f99578-dc10-4190-8894-6155677955f4";
}

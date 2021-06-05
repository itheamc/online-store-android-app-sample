package com.itheamc.meatprocessing.repositories;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.itheamc.meatprocessing.models.external.Categories;
import com.itheamc.meatprocessing.models.external.Distributions;
import com.itheamc.meatprocessing.models.external.Info;
import com.itheamc.meatprocessing.models.external.PriceData;
import com.itheamc.meatprocessing.models.external.Products;
import com.itheamc.meatprocessing.models.external.Slider;
import com.itheamc.meatprocessing.utilities.NotifyUtils;

import java.util.ArrayList;
import java.util.List;

public class FirestoreRepo {
    private static final String TAG = "FirestoreRepo";
    private static FirestoreRepo instance;
    private static FirebaseFirestore firestore;
    private List<Categories> categoriesList;
    private List<Products> productsList;
    private List<Distributions> distributionsList;
    private List<PriceData> priceDataList;
    private List<Slider> sliderList;
    private List<Info> infoList;


    // Getters for the instance of the class FirestoreRepo
    public static FirestoreRepo getInstance() {
        if (instance == null) {
            instance = new FirestoreRepo();
        }

        return instance;
    }

    // Getters for the instance of the class FirestoreRepo
    public static FirebaseFirestore getFirestore() {
        if (firestore == null) {
            firestore = FirebaseFirestore.getInstance();
        }

        return firestore;
    }

    // Method to get category data from the firebase firestore server
    public List<Categories> getCategoriesList() {
        if (categoriesList == null) {
            categoriesList = new ArrayList<>();
        }

        getFirestore()
                .collection("Categories")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null) {
                            categoriesList = queryDocumentSnapshots.toObjects(Categories.class);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        NotifyUtils.logError(TAG, "getCategoriesList", e);
                    }
                });

        return categoriesList;
    }


    // Method to get Products data from the firebase firestore server
    public List<Products> getProductsList() {
        if (productsList == null) {
            productsList = new ArrayList<>();
        }

        getFirestore()
                .collection("Products")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        NotifyUtils.logDebug(TAG, "Product list retrieved successfully");
                        if (queryDocumentSnapshots != null) {
                            productsList = queryDocumentSnapshots.toObjects(Products.class);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        NotifyUtils.logError(TAG, "getProductsList", e);
                    }
                });

        return productsList;

    }


    // Method to get Distributions data from the firebase firestore server
    public List<Distributions> getDistributionsList() {
        if (distributionsList == null) {
            distributionsList = new ArrayList<>();
        }

        getFirestore()
                .collection("Distributions")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null) {
                            distributionsList = queryDocumentSnapshots.toObjects(Distributions.class);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        NotifyUtils.logError(TAG, "getDistributionsList", e);
                    }
                });

        return distributionsList;
    }

    // Method to get price data from the firebase firestore server
    public List<PriceData> getPriceDataList() {
        if (priceDataList == null) {
            priceDataList = new ArrayList<>();
        }

        getFirestore()
                .collection("Prices")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null) {
                            priceDataList = queryDocumentSnapshots.toObjects(PriceData.class);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        NotifyUtils.logError(TAG, "getPriceDataList", e);
                    }
                });

        return priceDataList;
    }


    // Methods to get sliders data from the firestore
    public List<Slider> getSliderList() {
        if (sliderList == null) {
            sliderList = new ArrayList<>();
        }

        getFirestore()
                .collection("Slider")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null) {
                            sliderList = queryDocumentSnapshots.toObjects(Slider.class);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        NotifyUtils.logError(TAG, "getSliderList", e);
                    }
                });

        return sliderList;
    }


    // Methods to get info data from the firestore
    public List<Info> getInfoList() {
        if (infoList == null) {
            infoList = new ArrayList<>();
        }

        getFirestore()
                .collection("Info")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null) {
                            infoList = queryDocumentSnapshots.toObjects(Info.class);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        NotifyUtils.logError(TAG, "getInfoList", e);
                    }
                });

        return infoList;
    }

}

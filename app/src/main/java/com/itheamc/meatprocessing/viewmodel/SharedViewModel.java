package com.itheamc.meatprocessing.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.itheamc.meatprocessing.adapters.OrderAdapter;
import com.itheamc.meatprocessing.models.external.Order;
import com.itheamc.meatprocessing.models.internal.CartItems;
import com.itheamc.meatprocessing.models.external.Categories;
import com.itheamc.meatprocessing.models.external.Distributions;
import com.itheamc.meatprocessing.models.internal.HomeItems;
import com.itheamc.meatprocessing.models.external.Info;
import com.itheamc.meatprocessing.models.internal.OrderItems;
import com.itheamc.meatprocessing.models.external.PriceData;
import com.itheamc.meatprocessing.models.external.Products;
import com.itheamc.meatprocessing.models.external.Slider;
import com.itheamc.meatprocessing.models.external.Users;
import com.itheamc.meatprocessing.repositories.FirestoreRepo;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private static final String TAG = "SharedViewModel";
    private static FirestoreRepo firestoreRepo;
    private List<Categories> categoriesList;
    private List<Products> productsList;
    private List<Products> prebookProductList;
    private List<Products> regularProductList;
    private List<Slider> sliderList;
    private List<Info> infoList;
    private List<Distributions> distributionsList;
    private List<PriceData> priceDataList;
    private List<CartItems> cartItemsList;
    private MutableLiveData<Integer> cartItemSize;
    private List<HomeItems> homeItemsList;
    private List<Order> tempOrderList;    // Only for unconfirmed, underprocess, ontheway and completed order fragment
    private Products product;
    private Users user;
    private String paymentMethod;
    private LatLng latLng;


    // Getting the instance of the firestoreRepo class
    public static FirestoreRepo getFirestoreRepo() {
        if (firestoreRepo == null) {
            firestoreRepo = FirestoreRepo.getInstance();
        }

        return firestoreRepo;
    }


    // Getter method for the categoriesList
    public List<Categories> getCategoriesList() {
        if (categoriesList == null) {
            categoriesList = new ArrayList<>();
            categoriesList = getFirestoreRepo().getCategoriesList();
        }

        return categoriesList;
    }

    // Getter method for the productsList
    public List<Products> getProductsList() {
        if (productsList == null) {
            productsList = new ArrayList<>();
            productsList = getFirestoreRepo().getProductsList();
            return productsList;
        } else if (productsList.isEmpty()) {
            productsList = getFirestoreRepo().getProductsList();
            return productsList;
        } else {
            return productsList;
        }
    }

    // Setter for product List
    public void setProductsList(List<Products> productList) {
        if (productsList == null) {
            productsList = new ArrayList<>();
        }
        this.productsList = productList;
    }

    // Getter method for the DistributionsList
    public List<Distributions> getDistributionsList() {
        if (distributionsList == null) {
            distributionsList = new ArrayList<>();
            distributionsList = getFirestoreRepo().getDistributionsList();
        }

        return distributionsList;
    }

    // Getter method for priceDataList
    public List<PriceData> getPriceDataList() {
        if (priceDataList == null) {
            priceDataList = new ArrayList<>();
            priceDataList = getFirestoreRepo().getPriceDataList();
        }

        return priceDataList;
    }

    /**
     * Functions for the cartItemList
     **/
    //Getter
    public List<CartItems> getCartItemsList() {
        if (cartItemsList == null) {
            cartItemsList = new ArrayList<>();
        }

        return cartItemsList;
    }

    // Setter for cartItemsList
    public void setCartItemsList(List<CartItems> cartItemsList) {
        if (cartItemsList == null) {
            cartItemsList = new ArrayList<>();
        }

        this.cartItemsList = cartItemsList;
    }

    // Function to update cart item
    public List<CartItems> updateCartItems(String quantity, int position) {
        if (cartItemsList != null && !cartItemsList.isEmpty()) {
            double selectedQuantity = Double.parseDouble(quantity);
            CartItems cartItem = cartItemsList.get(position);
            cartItem.setOrderedQuantity(selectedQuantity);
            cartItemsList.set(position, cartItem);
        }

        return cartItemsList;
    }

    // Function to remove cart item from cartItemsList
    public boolean removeCartItem(int position) {
        int listSizeBefore = cartItemsList.size();
        cartItemsList.remove(position);
        int listSizeAfter = cartItemsList.size();

        return listSizeAfter != listSizeBefore;
    }


    // Function to add new item in cartItemList
    public void addCartItem(CartItems cartItem) {
        if (cartItemsList == null) {
            cartItemsList = new ArrayList<>();
            cartItemsList.add(cartItem);
        } else {
            int index = -1;
            for (int i = 0; i < cartItemsList.size(); i++) {
                if (cartItemsList.get(i).getProduct().getProductId().toLowerCase().equals(cartItem.getProduct().getProductId().toLowerCase())) {
                    index = i;
                    break;
                }
            }

            if (index == -1) {
                cartItemsList.add(cartItem);
            } else {
                CartItems tempCartItem = cartItemsList.get(index);
                tempCartItem.setOrderedQuantity(tempCartItem.getOrderedQuantity() + cartItem.getOrderedQuantity());
                cartItemsList.set(index, tempCartItem);
            }

        }
    }


    // Getter for mutable cart list for badge only
    public MutableLiveData<Integer> getCartItemSize() {
        if (cartItemSize == null) {
            cartItemSize = new MutableLiveData<>();
        }
        cartItemSize.postValue(getCartItemsList().size());
        return cartItemSize;
    }

    // Function to clear cart item
    public void clearCart() {
        cartItemsList.clear();
    }

    // Function for setter and getter for the product object for product pragment
    //Getter
    public Products getProduct() {
        if (product == null) {
            product = new Products();
        }

        return product;
    }

    //Setter
    public void setProduct(Products product) {
        if (this.product == null) {
            this.product = new Products();
        }

        this.product = product;
    }

    // Getter for Regular Product List
    public List<Products> getRegularProductList() {
        if (regularProductList == null) {
            regularProductList = new ArrayList<>();
            regularProductList = createRegularProductList();
        }


        return regularProductList;
    }

    // Getter for Prebook Product List
    public List<Products> getPrebookProductList() {
        if (prebookProductList == null) {
            prebookProductList = new ArrayList<>();
            prebookProductList = createPrebookProductList();
        }


        return prebookProductList;
    }

    // Creating regular or prebook product list
    private List<Products> createRegularProductList() {
        List<Products> tempProductList = new ArrayList<>();
        for (Products product : getProductsList()) {
            if (!product.isPrebooking()) {
                tempProductList.add(product);
            }

        }

        return tempProductList;
    }


    // Creating prebook product list
    private List<Products> createPrebookProductList() {
        List<Products> tempProductList = new ArrayList<>();
        for (Products product : getProductsList()) {
            if (product.isPrebooking()) {
                tempProductList.add(product);
            }

        }

        return tempProductList;
    }


    // Getter for the SliderList
    public List<Slider> getSliderList() {
        if (sliderList == null) {
            sliderList = new ArrayList<>();
            sliderList = getFirestoreRepo().getSliderList();
        }

        return sliderList;
    }


    // Getter for the infoList
    public List<Info> getInfoList() {
        if (infoList == null) {
            infoList = new ArrayList<>();
            infoList = getFirestoreRepo().getInfoList();
        }


        return infoList;
    }


    // Getter for homeItemLists
    public List<HomeItems> getHomeItemsList() {
        if (homeItemsList == null) {
            homeItemsList = new ArrayList<>();
            homeItemsList = gemerateHomeItems();
        }

        return homeItemsList;

    }

    // Method to generate homeItemList
    private List<HomeItems> gemerateHomeItems() {
        List<Products> tempRegularProducts = getRegularProductList();
        List<Products> tempPrebookProducts = getPrebookProductList();
        List<Slider> tempSlider = getSliderList();
        List<Info> tempInfo = getInfoList();
        List<HomeItems> tempHomeItems = new ArrayList<>();


        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                tempHomeItems.add(new HomeItems(1002, "Slider-View", tempSlider));
            } else if (i == 1) {
                tempHomeItems.add(new HomeItems(1004, "Info-View", tempInfo));
            } else if (i == 2) {
                tempHomeItems.add(new HomeItems(1003, "Pre-Book-View", tempPrebookProducts));
            } else {
                tempHomeItems.add(new HomeItems(1001, "Products-View", tempRegularProducts));
            }

        }

        return tempHomeItems;

    }


    // Getter and Setter for user
    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    // Getter and Setter for payment method
    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    // Getters and Setters for tempOrderList
    public List<Order> getTempOrderList() {
        if (tempOrderList == null) {
            tempOrderList = new ArrayList<>();
        }

        return tempOrderList;
    }

    public void setTempOrderList(List<Order> orderList) {
        if (tempOrderList == null) {
            this.tempOrderList = new ArrayList<>();
        }

        this.tempOrderList = orderList;
    }

    // Method to clear tempOrderList
    public void removeTempOrderItem(int position) {
        tempOrderList.remove(position);
    }

    // Getter and Setter for Latlng
    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}

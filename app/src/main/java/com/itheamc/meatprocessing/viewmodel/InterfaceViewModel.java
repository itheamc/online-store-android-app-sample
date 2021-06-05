package com.itheamc.meatprocessing.viewmodel;

import androidx.lifecycle.ViewModel;

import com.itheamc.meatprocessing.interfaces.MainActivityInterface;

public class InterfaceViewModel extends ViewModel {
    private MainActivityInterface mainActivityInterface;


    // Getter and setter
    public MainActivityInterface getMainActivityInterface() {
        return mainActivityInterface;
    }

    public void setMainActivityInterface(MainActivityInterface mainActivityInterface) {
        this.mainActivityInterface = mainActivityInterface;
    }
}

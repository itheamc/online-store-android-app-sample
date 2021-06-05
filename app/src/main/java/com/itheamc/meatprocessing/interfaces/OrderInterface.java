package com.itheamc.meatprocessing.interfaces;

public interface OrderInterface {

    public void cancelOrder(int position);
    public void navigateLocation(int position);
    public void payNow(int position);
    public void generateInvoice(int position);
}

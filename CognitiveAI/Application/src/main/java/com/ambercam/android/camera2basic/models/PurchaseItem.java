package com.ambercam.android.camera2basic.models;

/**
 * Created by brianroper on 10/3/16.
 */
public class PurchaseItem {

    private String mPrice;
    private String mAmount;

    public PurchaseItem(String amount, String price) {
        mAmount = amount;
        mPrice = price;
    }

    public String getPrice() {
        return mPrice;
    }

    public String getAmount() {
        return mAmount;
    }
}

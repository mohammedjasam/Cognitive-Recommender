package com.ambercam.android.camera2basic.models;

/**
 * Created by brianroper on 8/23/16.
 */
public class CountData {

    private int mImageCount;
    private int mImageMax;

    public CountData() {
    }

    public CountData(int imageCount, int imageMax) {
        this.mImageCount = imageCount;
        this.mImageMax = imageMax;
    }

    public int getImageCount() {
        return mImageCount;
    }

    public int getImageMax() {
        return mImageMax;
    }

    public void setImageMax(int imageMax) {
        this.mImageMax = imageMax;
    }

    public void setImageCount(int imageCount) {
        this.mImageCount = imageCount;
    }
}

package com.ambercam.android.camera2basic.models;
import static com.ambercam.android.camera2basic.ui.GetStartedActivity.UID;

/**
 * Created by brianroper on 8/7/16.
 */
public class CloudImage {

    private String mUrl;
    private String mTimeStamp;
//    private static int UID = 1152;


    public CloudImage() {
    }

    public CloudImage(String url, String timeStamp) {
        this.mUrl = url;
        this.mTimeStamp = timeStamp;

    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        mTimeStamp = timeStamp;
    }
}

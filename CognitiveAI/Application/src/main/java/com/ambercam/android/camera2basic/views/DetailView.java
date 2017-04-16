package com.ambercam.android.camera2basic.views;

/**
 * Created by brianroper on 12/15/16.
 */

public interface DetailView extends View {
    void returnIntentExtras();
    void setDeviceVibration();
    void shareImage();
    void returnUserPreferences();
    void handleDeviceVibration();
}

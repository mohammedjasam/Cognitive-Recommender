package com.ambercam.android.camera2basic.presenters;

import com.ambercam.android.camera2basic.views.View;

/**
 * Created by brianroper on 12/15/16.
 */

public interface Presenter<T extends View> {
    void onCreate();
    void onStart();
    void onStop();
    void onPause();
    void attachView(T view);
}

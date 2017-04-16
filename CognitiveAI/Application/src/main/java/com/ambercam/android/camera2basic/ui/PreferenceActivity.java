package com.ambercam.android.camera2basic.ui;

import android.os.Bundle;
import android.app.Activity;

import com.ambercam.android.camera2basic.R;

public class PreferenceActivity extends android.preference.PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
    }
}

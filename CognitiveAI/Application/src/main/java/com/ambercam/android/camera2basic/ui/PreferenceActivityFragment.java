package com.ambercam.android.camera2basic.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ambercam.android.camera2basic.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PreferenceActivityFragment extends PreferenceFragment {

    public PreferenceActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}

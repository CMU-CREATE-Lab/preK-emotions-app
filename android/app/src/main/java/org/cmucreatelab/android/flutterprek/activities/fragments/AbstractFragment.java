package org.cmucreatelab.android.flutterprek.activities.fragments;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * All fragments in the project should extend from this class.
 */
public abstract class AbstractFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getInflatedLayoutResource(), container, false);
    }


    /** Get the resource ("R.layout." ...) for the extended Fragment class. */
    @LayoutRes
    public abstract int getInflatedLayoutResource();

}

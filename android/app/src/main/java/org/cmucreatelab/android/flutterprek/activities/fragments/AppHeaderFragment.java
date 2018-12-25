package org.cmucreatelab.android.flutterprek.activities.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.cmucreatelab.android.flutterprek.R;

public class AppHeaderFragment extends Fragment {


    /** Get the resource ("R.layout." ...) for the extended Fragment class. */
    public int getInflatedLayoutResource() {
        return R.layout.fragment_app_header;
    }


    public void setHeaderTransparency(boolean flag) {
        int color = flag ? Color.alpha(100) : getResources().getColor(R.color.colorPrimary);
        getView().setBackgroundColor(color);
    }


    public void setListeners() {
        // TODO button listeners for header
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getInflatedLayoutResource(), container, false);
    }

}

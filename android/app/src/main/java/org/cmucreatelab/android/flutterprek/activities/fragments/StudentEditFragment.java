package org.cmucreatelab.android.flutterprek.activities.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.R;

public class StudentEditFragment extends AbstractFragment {

    public TextView textViewBack, textViewTitle;
    public Button buttonSave;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.textViewBack = view.findViewById(R.id.textViewBack);
        this.textViewTitle = view.findViewById(R.id.textViewTitle);
        this.buttonSave = view.findViewById(R.id.buttonSave);

//        // TODO actions when icons clicked
//
//        view.findViewById(R.id.imageLogo).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i(Constants.LOG_TAG, "imageLogo.onClick");
//            }
//        });
//        view.findViewById(R.id.imageStudent).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i(Constants.LOG_TAG, "imageStudent.onClick");
//            }
//        });
//        view.findViewById(R.id.imageInfo).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i(Constants.LOG_TAG, "imageInfo.onClick");
//            }
//        });
    }


    @Override
    public int getInflatedLayoutResource() {
        return R.layout.fragment_header_student_edit;
    }


    /** Set header to be transparent when {@param flag} is true. */
    public void setHeaderTransparency(boolean flag) {
        int color = flag ? Color.alpha(100) : getResources().getColor(R.color.colorPrimary);
        getView().setBackgroundColor(color);
    }

}

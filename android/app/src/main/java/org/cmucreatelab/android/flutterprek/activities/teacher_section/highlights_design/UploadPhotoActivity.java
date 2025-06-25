package org.cmucreatelab.android.flutterprek.activities.teacher_section.highlights_design;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;

public class UploadPhotoActivity extends AbstractActivity {

    private Button buttonPlaceholderOpt1, buttonPlaceholderOpt2, buttonPlaceholderOpt3;


    @Override
    protected void onResume() {
        super.onResume();

        TextView textViewPlaceholder = findViewById(R.id.textViewPlaceholder);
        this.buttonPlaceholderOpt1 = findViewById(R.id.buttonPlaceholderOpt1);
        this.buttonPlaceholderOpt2 = findViewById(R.id.buttonPlaceholderOpt2);
        this.buttonPlaceholderOpt3 = findViewById(R.id.buttonPlaceholderOpt3);

        textViewPlaceholder.setText("UploadPhotoActivity");

        buttonPlaceholderOpt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        buttonPlaceholderOpt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(2);
                finish();
            }
        });
        buttonPlaceholderOpt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra("foo", "bar");
                setResult(3, data);
                finish();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO Student from Intent?
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._highlights_design__activity_upload_photo;
    }

}




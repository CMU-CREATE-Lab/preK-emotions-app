package org.cmucreatelab.android.flutterprek.activities.teacher_section;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.session_index.SessionIndexActivity;

public class LoginActivity extends TeacherSectionActivityWithHeader {


    private boolean checkPassword() {
        String password = ((EditText)findViewById(R.id.editTextPassword)).getText().toString();
        // TODO replace with password from SharedPreferences
        return password.equals("admin");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide the keyboard when the activity starts (selects input box by default)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButtonLogin();
            }
        });
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._teacher_section__activity_login;
    }


    @Override
    public void onClickImageStudent() {
        finish();
    }


    public void onClickButtonLogin() {
        if (checkPassword()) {
            findViewById(R.id.textViewErrorPrompt).setVisibility(View.INVISIBLE);
            // clear the text field
            ((EditText)findViewById(R.id.editTextPassword)).setText("");
//            // proceed to teacher section
//            startActivity(new Intent(this, SessionIndexActivity.class));
            startActivity(new Intent(this, StudentIndexActivity.class));
            finish();
        } else {
            findViewById(R.id.textViewErrorPrompt).setVisibility(View.VISIBLE);
        }
    }

}
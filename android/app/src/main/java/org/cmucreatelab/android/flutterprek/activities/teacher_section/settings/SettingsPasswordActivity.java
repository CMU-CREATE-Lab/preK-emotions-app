package org.cmucreatelab.android.flutterprek.activities.teacher_section.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.GlobalHandler;
import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.TeacherSectionActivityWithHeader;

public class SettingsPasswordActivity extends TeacherSectionActivityWithHeader {

    private EditText editTextNewPassword;
    private TextView textViewErrorPrompt;


    private void onSavePassword(String password) {
        // avoid setting an empty password
        if (password.isEmpty()) {
            textViewErrorPrompt.setVisibility(View.VISIBLE);
            return;
        } else {
            textViewErrorPrompt.setVisibility(View.INVISIBLE);
        }

        // TODO consider checking integrity of password written to shared prefs
        SharedPreferences sharedPreferences = GlobalHandler.getSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.PreferencesKeys.teacherPassword, password).apply();

        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.editTextNewPassword = findViewById(R.id.editTextNewPassword);
        this.textViewErrorPrompt = findViewById(R.id.textViewErrorPrompt);

        // hide the keyboard when the activity starts (selects input box by default)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = editTextNewPassword.getText().toString();
                onSavePassword(password);
            }
        });
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._teacher_section__activity_settings_change_password;
    }

}

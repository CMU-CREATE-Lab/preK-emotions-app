package org.cmucreatelab.android.flutterprek.activities.teacher_section.session_index;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.adapters.session_index.SessionAdapter;
import org.cmucreatelab.android.flutterprek.activities.teacher_section.TeacherSectionActivityWithHeader;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.session.Session;

import java.util.List;

public class SessionIndexActivity extends TeacherSectionActivityWithHeader {

    private RecyclerView sessionsRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionsRecyclerView = (RecyclerView) findViewById(R.id.sessionsRecyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        sessionsRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        sessionsRecyclerView.setLayoutManager(layoutManager);

        AppDatabase.getInstance(this).sessionDAO().getAllSessions().observe(this, new Observer<List<Session>>() {
            @Override
            public void onChanged(@Nullable List<Session> sessions) {
                mAdapter = new SessionAdapter(SessionIndexActivity.this, sessions);
                sessionsRecyclerView.setAdapter(mAdapter);
            }
        });
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._teacher_section__activity_sessions_index;
    }


    @Override
    public void onClickImageStudent() {
        finish();
    }

}

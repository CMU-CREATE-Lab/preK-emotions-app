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
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.Collections;
import java.util.List;

public class SessionIndexActivity extends TeacherSectionActivityWithHeader implements Observer<List<Session>> {

    public static final String STUDENT_KEY = "student";

    private RecyclerView sessionsRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Student student = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check for student in bundle
        if (getIntent().hasExtra(STUDENT_KEY)) {
            this.student = (Student) getIntent().getSerializableExtra(STUDENT_KEY);
        }

        sessionsRecyclerView = (RecyclerView) findViewById(R.id.sessionsRecyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        sessionsRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        sessionsRecyclerView.setLayoutManager(layoutManager);

        if (student != null) {
            //// NOTE: I mostly left these in here as a learning tool (varargs, singleton list). -tasota
            //List<String> args = Arrays.asList(new String[]{ student.getUuid() });
            //List<String> args = Arrays.asList(student.getUuid());
            List<String> args = Collections.singletonList(student.getUuid());
            AppDatabase.getInstance(this).sessionDAO().getSessionsFromStudents(args).observe(this, this);
        } else {
            AppDatabase.getInstance(this).sessionDAO().getAllSessions().observe(this, this);
        }
    }


    @Override
    public int getResourceIdForActivityLayout() {
        return R.layout._teacher_section__activity_sessions_index;
    }


    @Override
    public void onChanged(@Nullable List<Session> sessions) {
        mAdapter = new SessionAdapter(SessionIndexActivity.this, sessions);
        sessionsRecyclerView.setAdapter(mAdapter);
    }

}

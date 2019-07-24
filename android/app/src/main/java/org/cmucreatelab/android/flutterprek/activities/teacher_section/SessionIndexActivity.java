package org.cmucreatelab.android.flutterprek.activities.teacher_section;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.Util;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.SessionCopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.session.Session;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.List;

public class SessionIndexActivity extends TeacherSectionActivityWithHeader {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public static class ItemSessionRecyclerViewHolder extends RecyclerView.ViewHolder {
        public final TextView textStudent, textNumberOfCopingSkills;
        public final ImageView imageStudent, imageEmotion;

        public final Context appContext;

        public ItemSessionRecyclerViewHolder(View v) {
            super(v);
            appContext = v.getContext().getApplicationContext();

            textStudent = v.findViewById(R.id.textStudent);
            imageEmotion = v.findViewById(R.id.imageEmotion);
            imageStudent = v.findViewById(R.id.imageStudent);
            textNumberOfCopingSkills = v.findViewById(R.id.textNumberOfCopingSkills);
        }

        public synchronized void updateWithItem(SessionAdapter.Item item) {
            String textTitle = item.session.getUuid();
            String date = item.session.getStartedAt().toString();

            if (item.student != null) textStudent.setText(item.student.getName());

            if (item.studentDbFile == null) {
                imageStudent.setImageResource(R.drawable.ic_placeholder);
            } else {
                Util.setImageViewWithAsset(appContext, imageStudent, item.studentDbFile.getFilePath());
            }
            if (item.emotionDbFile != null) Util.setImageViewWithAsset(appContext, imageEmotion, item.emotionDbFile.getFilePath());
            if (item.sessionCopingSkillList != null) {
                textNumberOfCopingSkills.setText(String.valueOf(item.sessionCopingSkillList.size()));
            }
        }
    }

    public static class SessionAdapter extends RecyclerView.Adapter<ItemSessionRecyclerViewHolder> {
        private final AbstractActivity activity;
        private final List<Session> sessions;

        public class Item {
            public final Session session;
            public Student student;
            public Emotion emotion;
            public DbFile studentDbFile, emotionDbFile;
            public List<SessionCopingSkill> sessionCopingSkillList;

            public Item(Session session) {
                this.session = session;
            }
        }


        public SessionAdapter(AbstractActivity activity, List<Session> sessions) {
            this.activity = activity;
            this.sessions = sessions;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ItemSessionRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_session, parent, false);
            // debug view
            //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_session_debug, parent, false);
            ItemSessionRecyclerViewHolder vh = new ItemSessionRecyclerViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final ItemSessionRecyclerViewHolder holder, int position) {
            final Item item = new SessionAdapter.Item(sessions.get(position));
            holder.updateWithItem(item);

            AppDatabase.getInstance(activity.getApplicationContext()).studentDAO().getStudent(item.session.getStudentUuid()).observe(activity, new Observer<Student>() {
                @Override
                public void onChanged(@Nullable Student student) {
                    item.student = student;
                    holder.updateWithItem(item);

                    if (student.getPictureFileUuid() != null) {
                        AppDatabase.getInstance(activity.getApplicationContext()).dbFileDAO().getDbFile(student.getPictureFileUuid()).observe(activity, new Observer<DbFile>() {
                            @Override
                            public void onChanged(@Nullable DbFile dbFile) {
                                // TODO check if file type is asset
                                item.studentDbFile = dbFile;
                                holder.updateWithItem(item);
                            }
                        });
                    }
                }
            });
            AppDatabase.getInstance(activity.getApplicationContext()).emotionDAO().getEmotion(item.session.getEmotionUuid()).observe(activity, new Observer<Emotion>() {
                @Override
                public void onChanged(@Nullable Emotion emotion) {
                    item.emotion = emotion;
                    holder.updateWithItem(item);

                    if (emotion != null) {
                        AppDatabase.getInstance(activity.getApplicationContext()).dbFileDAO().getDbFile(emotion.getImageFileUuid()).observe(activity, new Observer<DbFile>() {
                            @Override
                            public void onChanged(@Nullable DbFile dbFile) {
                                item.emotionDbFile = dbFile;
                                holder.updateWithItem(item);
                            }
                        });
                    }
                }
            });
            AppDatabase.getInstance(activity.getApplicationContext()).intermediateTablesDAO().getSessionCopingSkillsFromSessionUuid(item.session.getUuid()).observe(activity, new Observer<List<SessionCopingSkill>>() {
                @Override
                public void onChanged(@Nullable List<SessionCopingSkill> sessionCopingSkills) {
                    item.sessionCopingSkillList = sessionCopingSkills;
                    holder.updateWithItem(item);
                }
            });
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return sessions.size();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerView = (RecyclerView) findViewById(R.id.sessionsRecyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        AppDatabase.getInstance(this).sessionDAO().getAllSessions().observe(this, new Observer<List<Session>>() {
            @Override
            public void onChanged(@Nullable List<Session> sessions) {
                mAdapter = new SessionAdapter(SessionIndexActivity.this, sessions);
                recyclerView.setAdapter(mAdapter);
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

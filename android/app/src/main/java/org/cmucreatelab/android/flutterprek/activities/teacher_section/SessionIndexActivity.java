package org.cmucreatelab.android.flutterprek.activities.teacher_section;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;
import org.cmucreatelab.android.flutterprek.database.models.session.Session;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.List;

public class SessionIndexActivity extends TeacherSectionActivityWithHeader {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public static class ItemSessionRecyclerViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView, textDate, textStudent, textEmotion;

        public ItemSessionRecyclerViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.textFlutterName);
            textDate = v.findViewById(R.id.textDate);
            textStudent = v.findViewById(R.id.textStudent);
            textEmotion = v.findViewById(R.id.textEmotion);
        }

        public synchronized void updateWithItem(SessionAdapter.Item item) {
            String textTitle = item.session.getUuid();
            String date = item.session.getStartedAt().toString();

            textView.setText(textTitle);
            textDate.setText(date);
            if (item.student != null) textStudent.setText(item.student.getName());
            if (item.emotion != null) textEmotion.setText(item.emotion.getName());
        }
    }

    public static class SessionAdapter extends RecyclerView.Adapter<ItemSessionRecyclerViewHolder> {
        private final AbstractActivity activity;
        private final List<Session> sessions;

        public class Item {
            public final Session session;
            public Student student;
            public Emotion emotion;

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
                }
            });
            AppDatabase.getInstance(activity.getApplicationContext()).emotionDAO().getEmotion(item.session.getEmotionUuid()).observe(activity, new Observer<Emotion>() {
                @Override
                public void onChanged(@Nullable Emotion emotion) {
                    item.emotion = emotion;
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

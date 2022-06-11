package org.cmucreatelab.android.flutterprek.activities.adapters.session_index;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.cmucreatelab.android.flutterprek.R;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.database.AppDatabase;
import org.cmucreatelab.android.flutterprek.database.models.customization.Customization;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.SessionCopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.session.Session;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.List;

public class SessionAdapter extends RecyclerView.Adapter<ItemSessionRecyclerViewHolder> {

    private final AbstractActivity activity;
    private final List<Session> sessions;

    public static class Item {
        public final Session session;
        public Student student;
        public Emotion emotion;
        public DbFile studentDbFile, emotionDbFile;
        public List<SessionCopingSkill> sessionCopingSkillList;
        public List<Customization> customizationList;

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
        ItemSessionRecyclerViewHolder vh = new ItemSessionRecyclerViewHolder(activity, v);
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

        AppDatabase.getInstance(activity.getApplicationContext()).customizationDAO().getCustomizationsOwnedBy(item.session.getUuid()).observe(activity, new Observer<List<Customization>>() {
            @Override
            public void onChanged(@Nullable List<Customization> customizations) {
                item.customizationList = customizations;
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
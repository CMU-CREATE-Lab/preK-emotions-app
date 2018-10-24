package org.cmucreatelab.android.flutterprek.database;

import org.cmucreatelab.android.flutterprek.database.models.classroom.Classroom;
import org.cmucreatelab.android.flutterprek.database.models.coping_skill.CopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.customization.Customization;
import org.cmucreatelab.android.flutterprek.database.models.db_file.DbFile;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.EmotionCopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.ItineraryItem;
import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.SessionCopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.session.Session;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.List;

public class GsonDatabaseParser {

    public List<Classroom> classrooms;

    public List<Student> students;

    public List<CopingSkill> copingSkills;

    public List<Customization> customizations;

    public List<DbFile> dbFiles;

    public List<Emotion> emotions;

    public List<Session> sessions;

    public List<EmotionCopingSkill> emotionCopingSkills;

    public List<ItineraryItem> itineraryItems;

    public List<SessionCopingSkill> sessionCopingSkills;

}

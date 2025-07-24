package org.cmucreatelab.android.flutterprek.database.models.embedded_models.session_coping_skills;

import androidx.room.Embedded;
import androidx.room.Relation;

import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.SessionCopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.session.Session;

import java.util.List;


public class SessionWithSessionCopingSkills {

    @Embedded
    public Session session;

    @Relation(parentColumn = "uuid", entityColumn = "session_uuid", entity = SessionCopingSkill.class)
    public List<SessionCopingSkill> sessionCopingSkills;

}




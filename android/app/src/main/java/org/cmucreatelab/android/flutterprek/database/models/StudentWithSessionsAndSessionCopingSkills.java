package org.cmucreatelab.android.flutterprek.database.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import org.cmucreatelab.android.flutterprek.database.models.intermediate_tables.SessionCopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.session.Session;
import org.cmucreatelab.android.flutterprek.database.models.student.Student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StudentWithSessionsAndSessionCopingSkills {

    @Embedded
    public Student student;

    @Relation(parentColumn = "uuid", entityColumn = "student_uuid", entity = Session.class)
    public List<SessionWithSessionCopingSkills> sessions;


    // Helper class (Maps CopingSkill + Emotion -> Count)

    public static class CopingSkillEmotion {
        public String copingSkillUuid;
        public String emotionUuid;

        public CopingSkillEmotion(String copingSkillUuid, String emotionUuid) {
            this.copingSkillUuid = copingSkillUuid;
            this.emotionUuid = emotionUuid;
        }

        // Override equals and hashCode for correct behavior in HashMap
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CopingSkillEmotion)) return false;
            CopingSkillEmotion copingSkillEmotion = (CopingSkillEmotion) o;
            return copingSkillUuid.equals(copingSkillEmotion.copingSkillUuid) && emotionUuid.equals(copingSkillEmotion.emotionUuid);
        }

        @Override
        public int hashCode() {
            return Objects.hash(copingSkillUuid, emotionUuid);
        }

        @Override
        public String toString() {
            return String.format("CopingSkillEmotion(%s, %s)", copingSkillUuid, emotionUuid);
        }
    }


    public static Map<CopingSkillEmotion, Integer> countSessionCopingSkillsWithEmotion(List<StudentWithSessionsAndSessionCopingSkills> list) {
        Map<CopingSkillEmotion, Integer> result = new HashMap<>();

        for (StudentWithSessionsAndSessionCopingSkills studentSessions: list) {
            for (SessionWithSessionCopingSkills session: studentSessions.sessions) {
                String emotionUuid = session.session.getEmotionUuid();
                for (SessionCopingSkill sessionCopingSkill: session.sessionCopingSkills) {
                    CopingSkillEmotion key = new CopingSkillEmotion(sessionCopingSkill.getCopingSkillUuid(), emotionUuid);
                    // requires API 24...
                    //result.put(key, result.getOrDefault(key, 0) + 1);
                    // ...
                    if (result.containsKey(key)) {
                        result.put(key, result.get(key)+1);
                    } else {
                        result.put(key, 1);
                    }
                }
            }
        }

        return result;
    }

}




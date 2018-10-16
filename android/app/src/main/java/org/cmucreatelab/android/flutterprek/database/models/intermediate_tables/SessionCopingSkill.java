package org.cmucreatelab.android.flutterprek.database.models.intermediate_tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.cmucreatelab.android.flutterprek.database.models.coping_skill.CopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.session.Session;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by tasota on 10/10/2018.
 *
 * SessionCopingSkill
 *
 * SessionCopingSkill entity for a Room database. See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
@Entity(tableName = "sessions_coping_skills", indices = {@Index("session_uuid"), @Index("coping_skill_uuid")})
public class SessionCopingSkill {

    @PrimaryKey
    @NonNull
    private String uuid;

    @NonNull
    @ColumnInfo(name="session_uuid")
    private String sessionUuid;

    @NonNull
    @ColumnInfo(name="coping_skill_uuid")
    private String copingSkillUuid;

    @NonNull
    @ColumnInfo(name="started_at")
    private Date startedAt;

}

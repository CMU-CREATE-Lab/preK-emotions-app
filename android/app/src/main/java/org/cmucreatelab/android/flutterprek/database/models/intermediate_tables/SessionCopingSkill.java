package org.cmucreatelab.android.flutterprek.database.models.intermediate_tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.UUID;

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


    @Ignore
    public SessionCopingSkill(@NonNull String sessionUuid, @NonNull String copingSkillUuid, @NonNull Date startedAt) {
        this(UUID.randomUUID().toString(), sessionUuid, copingSkillUuid, startedAt);
    }


    public SessionCopingSkill(@NonNull String uuid, @NonNull String sessionUuid, @NonNull String copingSkillUuid, @NonNull Date startedAt) {
        this.uuid = uuid;
        this.sessionUuid = sessionUuid;
        this.copingSkillUuid = copingSkillUuid;
        this.startedAt = startedAt;
    }


    @NonNull
    public String getUuid() {
        return uuid;
    }


    @NonNull
    public String getSessionUuid() {
        return sessionUuid;
    }


    @NonNull
    public String getCopingSkillUuid() {
        return copingSkillUuid;
    }


    @NonNull
    public Date getStartedAt() {
        return startedAt;
    }

}

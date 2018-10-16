package org.cmucreatelab.android.flutterprek.database.models.session;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;

/**
 * Created by tasota on 10/3/2018.
 */
@Entity(tableName = "sessions", indices = {@Index("student_uuid"), @Index("emotion_uuid")})
public class Session {

    @PrimaryKey
    @NonNull
    private String uuid;

    @NonNull
    @ColumnInfo(name="student_uuid")
    private String studentUuid;

    @NonNull
    @ColumnInfo(name="started_at")
    private Date startedAt;

    @Nullable
    @ColumnInfo(name="ended_at")
    private Date endedAt;

    @Nullable
    @ColumnInfo(name="emotion_uuid")
    private String emotionUuid;


    public Session(@NonNull String uuid, @NonNull String studentUuid, @NonNull Date startedAt) {
        this.uuid = uuid;
        this.studentUuid = studentUuid;
        this.startedAt = startedAt;
    }


    @NonNull
    public String getUuid() {
        return uuid;
    }


    @NonNull
    public String getStudentUuid() {
        return studentUuid;
    }


    @NonNull
    public Date getStartedAt() {
        return startedAt;
    }


    @Nullable
    public Date getEndedAt() {
        return endedAt;
    }


    @Nullable
    public String getEmotionUuid() {
        return emotionUuid;
    }


    public void setStudentUuid(@NonNull String studentUuid) {
        this.studentUuid = studentUuid;
    }


    public void setEndedAt(@Nullable Date endedAt) {
        this.endedAt = endedAt;
    }


    public void setEmotionUuid(@Nullable String emotionUuid) {
        this.emotionUuid = emotionUuid;
    }

}

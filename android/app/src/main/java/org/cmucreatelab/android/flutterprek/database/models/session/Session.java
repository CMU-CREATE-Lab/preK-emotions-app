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

}

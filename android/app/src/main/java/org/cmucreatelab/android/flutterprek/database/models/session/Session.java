package org.cmucreatelab.android.flutterprek.database.models.session;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by tasota on 10/3/2018.
 */
public class Session {

    @PrimaryKey
    @NonNull
    private String uuid;

    @NonNull
    @ColumnInfo(name="student_uuid")
    private String studentUuid;

    @Nullable
    @ColumnInfo(name="emotion_uuid")
    private String emotionUuid;

}

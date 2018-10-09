package org.cmucreatelab.android.flutterprek.database.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by tasota on 10/9/2018.
 *
 * CopingSkillEmotion
 *
 * CopingSkillEmotion entity for a Room database. See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
@Entity(tableName = "coping_skills_emotions",
    foreignKeys = {
            @ForeignKey(entity = CopingSkill.class, parentColumns = "uuid", childColumns = "coping_skill_uuid", onDelete = CASCADE),
            @ForeignKey(entity = Emotion.class, parentColumns = "uuid", childColumns = "emotion_uuid", onDelete = CASCADE),
    })
public class CopingSkillEmotion {

    @PrimaryKey
    @NonNull
    private String uuid;

    @Nullable
    @ColumnInfo(name="owner_uuid")
    private String ownerUuid;

    @NonNull
    @ColumnInfo(name="emotion_uuid")
    private String emotionUuid;

    @NonNull
    @ColumnInfo(name="coping_skill_uuid")
    private String copingSkillUuid;

}

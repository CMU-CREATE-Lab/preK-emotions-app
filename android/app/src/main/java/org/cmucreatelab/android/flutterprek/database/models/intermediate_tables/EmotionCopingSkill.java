package org.cmucreatelab.android.flutterprek.database.models.intermediate_tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.cmucreatelab.android.flutterprek.database.models.coping_skill.CopingSkill;
import org.cmucreatelab.android.flutterprek.database.models.emotion.Emotion;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by tasota on 10/9/2018.
 *
 * EmotionCopingSkill
 *
 * EmotionCopingSkill entity for a Room database. See Room persistence library documentation for details:
 *   https://developer.android.com/training/data-storage/room/accessing-data
 */
@Entity(tableName = "emotions_coping_skills",
    indices = {
            @Index("owner_uuid"),
            @Index("emotion_uuid"),
            @Index("coping_skill_uuid")
    },
    foreignKeys = {
            @ForeignKey(entity = CopingSkill.class, parentColumns = "uuid", childColumns = "coping_skill_uuid", onDelete = CASCADE),
            @ForeignKey(entity = Emotion.class, parentColumns = "uuid", childColumns = "emotion_uuid", onDelete = CASCADE),
    })
public class EmotionCopingSkill {

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


    public EmotionCopingSkill(@NonNull String uuid, @NonNull String emotionUuid, @NonNull String copingSkillUuid) {
        this.uuid = uuid;
        this.emotionUuid = emotionUuid;
        this.copingSkillUuid = copingSkillUuid;
    }


    @NonNull
    public String getUuid() {
        return uuid;
    }


    @Nullable
    public String getOwnerUuid() {
        return ownerUuid;
    }


    @NonNull
    public String getEmotionUuid() {
        return emotionUuid;
    }


    @NonNull
    public String getCopingSkillUuid() {
        return copingSkillUuid;
    }


    public void setOwnerUuid(@Nullable String ownerUuid) {
        this.ownerUuid = ownerUuid;
    }


    public void setEmotionUuid(@NonNull String emotionUuid) {
        this.emotionUuid = emotionUuid;
    }


    public void setCopingSkillUuid(@NonNull String copingSkillUuid) {
        this.copingSkillUuid = copingSkillUuid;
    }

}

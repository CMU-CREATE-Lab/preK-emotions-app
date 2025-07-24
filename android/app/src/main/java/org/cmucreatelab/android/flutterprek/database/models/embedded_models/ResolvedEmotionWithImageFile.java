package org.cmucreatelab.android.flutterprek.database.models.embedded_models;

public class ResolvedEmotionWithImageFile {

    public String uuid;

    public String ownerUuid;

    public String name;

    public String resolvedImageFileUuid;


    public ResolvedEmotionWithImageFile(String uuid, String ownerUuid, String name, String resolvedImageFileUuid) {
        this.uuid = uuid;
        this.ownerUuid = ownerUuid;
        this.name = name;
        this.resolvedImageFileUuid = resolvedImageFileUuid;
    }

}

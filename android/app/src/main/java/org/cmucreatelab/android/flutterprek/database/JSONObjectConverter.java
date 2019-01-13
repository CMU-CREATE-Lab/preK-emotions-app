package org.cmucreatelab.android.flutterprek.database;

import android.arch.persistence.room.TypeConverter;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONObjectConverter {


    @TypeConverter
    public static JSONObject toJson(String rawString) {
        try {
            return rawString == null ? null : new JSONObject(rawString);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    @TypeConverter
    public static String toRawString(JSONObject jsonObject) {
        return jsonObject == null ? null : jsonObject.toString();
    }

}
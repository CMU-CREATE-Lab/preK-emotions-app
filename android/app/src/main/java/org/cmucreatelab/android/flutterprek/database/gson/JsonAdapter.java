package org.cmucreatelab.android.flutterprek.database.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class JsonAdapter extends TypeAdapter<JSONObject> {

    @Override
    public void write(JsonWriter out, JSONObject value) throws IOException {
        out.value(value == null ? null : value.toString());
    }

    @Override
    public JSONObject read(JsonReader in) throws IOException {
        String jsonBlob = in.nextString();
        try {
            return jsonBlob == null ? null : new JSONObject(jsonBlob);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
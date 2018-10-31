package org.cmucreatelab.android.flutterprek.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Date;

public class DateTypeAdapter extends TypeAdapter<Date> {

    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        out.value(value == null ? null : value.getTime());
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        Long timestamp = in.nextLong();
        return timestamp == null ? null : new Date(timestamp);
    }

}
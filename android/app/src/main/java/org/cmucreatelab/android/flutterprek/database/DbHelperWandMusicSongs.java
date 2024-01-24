package org.cmucreatelab.android.flutterprek.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.Constants;
import org.cmucreatelab.android.flutterprek.activities.AbstractActivity;
import org.cmucreatelab.android.flutterprek.database.models.customization.Customization;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DbHelperWandMusicSongs {

    public static class MusicFileJson {
        public int selected;
        public String dbfileuuid;
        public String songName;
    };

    public interface Listener {
        void onReadFromDb(ArrayList<MusicFileJson> arrayListJson);
    }

    private static String DB_KEY = "music_files";

    // given 'ownerUuid' pull all customizations (specifically only care about 'music_files' key)
    // parse and write JSONArray/JSONObject
    // this should also deal with settings list, enable/disable, filename

    private Customization customizationWandMusicSong;
    //private ArrayList<MusicFileJson> arrayListJson;

    private String ownerUuid;
    private AbstractActivity activity;


    public DbHelperWandMusicSongs(AbstractActivity activity, String ownerUuid) {
        this.activity = activity;
        this.ownerUuid = ownerUuid;
    }


    public void readFromDb(Listener dbListener) {
        LiveData<List<Customization>> liveData = AppDatabase.getInstance(activity.getApplicationContext()).customizationDAO().getCustomizationsOwnedBy(ownerUuid);
        //AppDatabase.getInstance(activity.getApplicationContext()).customizationDAO().getCustomizationsOwnedBy(ownerUuid).observe(activity, new Observer<List<Customization>>() {
        liveData.observe(activity, new Observer<List<Customization>>() {
            @Override
            public void onChanged(@Nullable List<Customization> customizations) {
                liveData.removeObserver(this);

                for (Customization customization: customizations) {
                    if (customization.getKey().equals(DB_KEY)) {
                        DbHelperWandMusicSongs.this.customizationWandMusicSong = customization;
                        break;
                    }
                }

                if (customizationWandMusicSong != null) {
                    ArrayList<MusicFileJson> result = new ArrayList<>();
                    JSONObject arrayObject = JSONObjectConverter.toJson(customizationWandMusicSong.getValue());
                    Log.v(Constants.LOG_TAG, String.format("Read value from customization %s: ```%s```", DB_KEY, JSONObjectConverter.toRawString(arrayObject)));
                    Iterator<String> keys = arrayObject.keys();
                    while (keys.hasNext()) {
                        String nextKey = keys.next();
                        try {
                            JSONObject jsonObject = arrayObject.getJSONObject(nextKey);
                            MusicFileJson musicFileJson = new MusicFileJson();
                            musicFileJson.dbfileuuid = jsonObject.getString("dbfileuuid");
                            musicFileJson.songName = jsonObject.getString("songName");
                            musicFileJson.selected = jsonObject.getInt("selected");
                            result.add(musicFileJson);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
//                    JSONArray array = JSONObjectConverter.toJsonArray(customizationWandMusicSong.getValue());
//                    int length = array.length();
//                    for (int i=0; i<length; i++) {
//                        try {
//                            JSONObject jsonObject = array.getJSONObject(i);
//                            MusicFileJson musicFileJson = new MusicFileJson();
//                            musicFileJson.dbFieldUuid = jsonObject.getString("dbFieldUuid");
//                            musicFileJson.songName = jsonObject.getString("songName");
//                            musicFileJson.selected = jsonObject.getInt("selected");
//                            result.add(musicFileJson);
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }

                    if (dbListener != null) dbListener.onReadFromDb(result);
                }
            }
        });
    }


    public void writeToDb(ArrayList<MusicFileJson> arrayListJson) {
        LiveData<List<Customization>> liveData = AppDatabase.getInstance(activity.getApplicationContext()).customizationDAO().getCustomizationsOwnedBy(ownerUuid);
        //AppDatabase.getInstance(activity.getApplicationContext()).customizationDAO().getCustomizationsOwnedBy(ownerUuid).observe(activity, new Observer<List<Customization>>() {
        liveData.observe(activity, new Observer<List<Customization>>() {
            @Override
            public void onChanged(@Nullable List<Customization> customizations) {
                liveData.removeObserver(this);

                for (Customization customization: customizations) {
                    if (customization.getKey().equals(DB_KEY)) {
                        DbHelperWandMusicSongs.this.customizationWandMusicSong = customization;
                        break;
                    }
                }

                if (customizationWandMusicSong != null) {
                    JSONObject value = new JSONObject();
                    int length = arrayListJson.size();
                    for (int i=0; i<length; i++) {
                        JSONObject jsonObject = new JSONObject();
                        MusicFileJson musicFileJson = arrayListJson.get(i);

                        try {
                            jsonObject.put("dbfileuuid", musicFileJson.dbfileuuid);
                            jsonObject.put("songName", musicFileJson.songName);
                            jsonObject.put("selected", musicFileJson.selected);

                            value.put(new Integer(i).toString(), jsonObject);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    String valueToString = JSONObjectConverter.toRawString(value);
                    Log.v(Constants.LOG_TAG, String.format("preparing to write value to customization %s: ```%s```", DB_KEY, valueToString));
                    customizationWandMusicSong.setValue(valueToString);
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            AppDatabase.getInstance(activity.getApplicationContext()).customizationDAO().update(customizationWandMusicSong);
                        }
                    });
                }
            }
        });
    }

}

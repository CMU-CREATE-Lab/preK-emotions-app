package org.cmucreatelab.android.flutterprek.activities.student_section.coping_skills.coping_skill_wand;

import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

import org.cmucreatelab.android.flutterprek.Constants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WandSpeedTracker {

    private WandCopingSkillActivity activity;
    private int[] vals1;
    private int[] vals2;
    private int[] vals3;
    private long[] time;
    private int index = 0;
    private int sgn = 0;
    private double prevVal1 = 0.0;
    private double prevSlope1 = 0.0;
    private double prevVal2 = 0.0;
    private double prevSlope2 = 0.0;
    private double prevVal3 = 0.0;
    private double prevSlope3 = 0.0;
    private int window;

    public WandSpeedTracker(WandCopingSkillActivity activity, int window) {

        this.activity = activity;
        this.window = window;
        vals1 = new int[window];
        vals2 = new int[window];
        vals3 = new int[window];
        time = new long[window];
    }

    public void writeValsToArray(int[] vals, long curTime) {
        vals1[index] = vals[0];
        vals2[index] = vals[1];
        vals3[index] = vals[2];
        time[index] = curTime;
        index++;
        if (index >= window) {
            index = 0;
        }
    }

    public void countSigns() {
        int signSum = 0;
        int sum1 = 0;
        double smooth1 = 0;
        for(int i = 0; i < vals1.length; i++) {
            sum1 += vals1[i];
        }
        smooth1 = sum1/vals1.length;
        double slope1 = smooth1 - prevVal1;
        if (slope1*prevSlope1 < 0) {
            signSum++;
        }
        prevVal1 = smooth1;
        prevSlope1 = slope1;

        int sum2 = 0;
        int smooth2 = 0;
        for(int i = 0; i < vals1.length; i++) {
            sum2 += vals1[i];
        }
        smooth2 = sum2/vals1.length;
        double slope2 = smooth2 - prevVal2;
        if (slope2*prevSlope2 < 0) {
            signSum++;
        }
        prevVal2 = smooth2;
        prevSlope2 = slope2;

        int sum3 = 0;
        int smooth3 = 0;
        for(int i = 0; i < vals1.length; i++) {
            sum3 += vals1[i];
        }
        smooth3 = sum3/vals1.length;
        double slope3 = smooth3 - prevVal3;
        if (slope3*prevSlope3 < 0) {
            signSum++;
        }
        prevVal3 = smooth3;
        prevSlope3 = slope3;

        if(signSum >= 2) {
            sgn++;
        }
    }

    public int getSpeed() {
        int speed = -1;

        if(sgn <= 5 && sgn >= 1) {
            // Moving sowly
            speed = 1;
        } else if (sgn > 5) {
            // Moving fast
            speed = 2;
        } else {
            speed = 0;
        }

        //Log.e(Constants.LOG_TAG, "Sign count was: "+sgn);
        sgn = 0;

        return speed;
    }

    private void writeArraysToFile (String fileName, ArrayList[] lists) {
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return;
        } else {
            // Is writable
            String path = activity.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString() + File.separator  + "Wand";
            Log.e(Constants.LOG_TAG, path);
            // Create the folder.
            File folder = new File(path);
            // Create the file.
            File file = new File(folder, fileName);

            try
            {
                if (!file.exists()) {
                    file.createNewFile();
                }

                FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
                BufferedWriter bw = new BufferedWriter(fw);
                //run through arrays and write all values
                for (int i = 0; i < lists[0].size(); i++) {
                    bw.write(lists[0].get(i).toString()+","+lists[1].get(i).toString()+","+lists[2].get(i).toString()+","+lists[3].get(i).toString()+"\n");
                }

                bw.close();

                MediaScannerConnection.scanFile(activity, new String[] {file.toString()}, null, null);
            }
            catch (IOException e)
            {
                Log.e("Exception", "File write failed: " + e.toString());
            }
        }
    }

    private void writeRangeToFile (int range) {
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return;
        } else {
            // Is writable
            String path = activity.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString() + File.separator  + "Wand";
            Log.e(Constants.LOG_TAG, path);
            // Create the folder.
            File folder = new File(path);
            // Create the file.
            File file = new File(folder, "range.txt");

            try
            {
                if (!file.exists()) {
                    file.createNewFile();
                }

                FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
                BufferedWriter bw = new BufferedWriter(fw);
                //run through arrays and write all values
                bw.write(range+"\n");

                bw.close();

                MediaScannerConnection.scanFile(activity, new String[] {file.toString()}, null, null);
            }
            catch (IOException e)
            {
                Log.e("Exception", "File write failed: " + e.toString());
            }
        }
    }
}

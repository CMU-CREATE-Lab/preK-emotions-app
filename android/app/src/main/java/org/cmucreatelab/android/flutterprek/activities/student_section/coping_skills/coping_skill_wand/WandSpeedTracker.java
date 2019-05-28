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
import java.util.Collections;

public class WandSpeedTracker {

    private WandCopingSkillActivity activity;
    private ArrayList<Integer> vals1;
    private ArrayList<Integer> vals2;
    private ArrayList<Integer> vals3;
    private ArrayList<Long> time;

    public WandSpeedTracker(WandCopingSkillActivity activity) {

        this.activity = activity;
        vals1 = new ArrayList<>(1);
        vals2 = new ArrayList<>(1);
        vals3 = new ArrayList<>(1);
        time = new ArrayList<>(1);
    }

    public void writeValsToArray(int[] vals, long curTime) {
        vals1.add(vals[0]);
        vals2.add(vals[1]);
        vals3.add(vals[2]);
        time.add(curTime);
    }

    public int getSpeed() {
        int speed = -1;
        /*
        // Find the maximum range in the data
        int maxRange1 = Math.abs(Collections.max(vals1) - Collections.min(vals1));
        int maxRange2 = Math.abs(Collections.max(vals2) - Collections.min(vals2));
        int maxRange3 = Math.abs(Collections.max(vals3) - Collections.min(vals3));
        int avgRange = (maxRange1 + maxRange2 + maxRange3)/3;

        Log.e(Constants.LOG_TAG, "Vals1: " + vals1.toString());
        Log.e(Constants.LOG_TAG, "Vals2: " + vals2.toString());
        Log.e(Constants.LOG_TAG, "Vals3: " + vals3.toString());
        Log.e(Constants.LOG_TAG, "Vals1 Range: " + maxRange1);
        Log.e(Constants.LOG_TAG, "Vals2 Range: " + maxRange2);
        Log.e(Constants.LOG_TAG, "Vals3 Range: " + maxRange3);
        Log.e(Constants.LOG_TAG, "Avg Range: " + avgRange);


        if(avgRange > 500 && avgRange < 4900) {
            // Wand moving slowly
            speed = 1;
        } else if(avgRange >= 4900) {
            // Wand moving fast
            speed = 2;
        } else {
            speed = 0;
        }

        writeRangeToFile(avgRange);

        */

        Log.e(Constants.LOG_TAG, "Smoothing");
        // Smooth the data
        ArrayList<Double> smoothVals1 = movingAverage(vals1);
        ArrayList<Double> smoothVals2 = movingAverage(vals2);
        ArrayList<Double> smoothVals3 = movingAverage(vals3);

        Log.e(Constants.LOG_TAG, "Counting Signs");
        // Count the number of sign changes in each of the values
        int sgn1 = signChanges(smoothVals1);
        int sgn2 = signChanges(smoothVals1);
        int sgn3 = signChanges(smoothVals1);

        int sgn = (sgn1 + sgn2 + sgn3)/3;

        Log.e(Constants.LOG_TAG, "Vals1: " + smoothVals1.toString());
        Log.e(Constants.LOG_TAG, "Vals2: " + smoothVals2.toString());
        Log.e(Constants.LOG_TAG, "Vals3: " + smoothVals3.toString());
        Log.e(Constants.LOG_TAG, "Vals1 Sign Count: " + sgn1);
        Log.e(Constants.LOG_TAG, "Vals2 Sign Count: " + sgn2);
        Log.e(Constants.LOG_TAG, "Vals3 Sign Count: " + sgn3);
        Log.e(Constants.LOG_TAG, "Avg Sign Count: " + sgn);

        if(sgn <= 3 && sgn >= 1) {
            // Moving sowly
            speed = 1;
        } else if (sgn > 3) {
            // Moving fast
            speed = 2;
        } else {
            speed = 0;
        }


        ArrayList[] lists = {vals1, vals2, vals3, time};
        writeArraysToFile("slow.txt", lists);

        ArrayList[] lists2 = {smoothVals1, smoothVals2, smoothVals3, time};
        writeArraysToFile("slow_smooth.txt", lists2);

        resetArrays();

        return speed;
    }

    private ArrayList<Double> movingAverage (ArrayList<Integer> data) {
        int n = data.size();
        ArrayList<Double> smooth = new ArrayList<>(n);

        Log.e(Constants.LOG_TAG, "Made Array");

        double average;
        int count;
        for (int i = 0; i < n; i++) {
            average = 0;
            count = 0;
            for(int j = i-3; j < i+3; j++) {
                if(j >= 0 && j < n) {
                    average += (double) data.get(j);
                    count++;
                }
            }
            average = average/count;
            smooth.set(i, average);
        }

        Log.e(Constants.LOG_TAG, "finished for");

        return smooth;
    }

    private int signChanges(ArrayList<Double> data) {
        int sgns = 0;
        double prev = data.get(0);
        double prevSlope = 0.0;

        for(int i = 0; i < data.size(); i++) {
            double slope = data.get(i) - prev;
            if (slope * prevSlope < 0) {
                sgns++;
            }
            prevSlope = slope;
            prev = data.get(i);
        }

        return sgns;
    }

    private void resetArrays () {
        Log.e(Constants.LOG_TAG, "The size of the final array was "+time.size());
        time = new ArrayList<>(1);
        vals1 = new ArrayList<>(1);
        vals2 = new ArrayList<>(1);
        vals3 = new ArrayList<>(1);
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

    private double[] fit_sine (double[] sample) {
        double [] ans = {0.0};
        return ans;
    }
}

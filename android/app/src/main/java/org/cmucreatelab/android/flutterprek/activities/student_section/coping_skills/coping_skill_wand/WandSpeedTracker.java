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
    private double[] vals1;
    private double[] vals2;
    private double[] vals3;
    private long[] time;
    private int index = 0;
    private int window;
    int fileNumber;

    private double upperThresh = 200.0;
    private double lowerThresh = 50.0;
    private double max_mag = 0.0;

    public WandSpeedTracker(WandCopingSkillActivity activity, int window) {
        this.activity = activity;
        this.window = window;
        vals1 = new double[window];
        vals2 = new double[window];
        vals3 = new double[window];
        time = new long[window];
        time = new long[window];
        Constants.fileStart++;
        fileNumber = Constants.fileStart;
    }

    public void updateMaxMag(int[] vals){
        double v0 = (double) vals[0];
        double v1 = (double) vals[1];
        double v2 = (double) vals[2];
        double mag = Math.sqrt(v0*v0 + v1*v1 + v2*v2);

        if (mag > max_mag) {
            max_mag = mag;
        }
    }

    public int getSpeed() {
        int speed = -1;

        //TODO is the zero a good cutoff
        if(max_mag <= upperThresh && max_mag >= lowerThresh) {
            // Moving slowly
            speed = 1;
        } else if (max_mag > upperThresh) {
            // Moving fast
            speed = 2;
        } else {
            speed = 0;
        }

        Log.e(Constants.LOG_TAG, "Max count was: "+max_mag);
        //int temp = sgn;
        double temp = max_mag;
        writeRangeToFile(temp);
        max_mag = 0.0;

        return speed;
    }

    public void writeValsToArray(int[] vals, long curTime) {
        double v0 = (double) vals[0];
        double v1 = (double) vals[1];
        double v2 = (double) vals[2];
        double mag = Math.sqrt(v0*v0 + v1*v1 + v2*v2);
        vals1[index] = v0;
        vals2[index] = v1;
        vals3[index] = v2;
        time[index] = curTime;
        index++;
        if (index >= window) {
            index = 0;
            String fileName = "testVals" + fileNumber + ".txt";
            ArrayList arrays = new ArrayList();
            arrays.add(vals1);
            arrays.add(vals2);
            arrays.add(vals3);
            arrays.add(time);
            writeArraysToFile(fileName, arrays);
        }
    }

    private void writeArraysToFile (String fileName, ArrayList lists) {
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return;
        } else {
            // Is writable
            String path = activity.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString(); // + File.separator  + "Wand";
            //Log.e(Constants.LOG_TAG, path);
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
                double[] v1 = (double[]) lists.get(0);
                double[] v2 = (double[]) lists.get(1);
                double[] v3 = (double[]) lists.get(2);
                long[] t = (long[]) lists.get(3);
                for (int i = 0; i < v1.length; i++) {
                    bw.write(v1[i]+","+v2[i]+","+v3[i]+","+t[i]+"\n");
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

    private void writeRangeToFile (double range) {
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return;
        } else {
            // Is writable
            String path = activity.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString();// + File.separator  + "New Folder";
            //Log.e(Constants.LOG_TAG, path);
            // Create the folder.
            File folder = new File(path);
            // Create the file.
            String filename = "signs"+fileNumber+".txt";
            //Log.e(Constants.LOG_TAG, filename);
            File file = new File(folder, filename);

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

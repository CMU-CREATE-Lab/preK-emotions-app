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
    private int[] gyro1;
    private int[] gyro2;
    private int[] gyro3;
    private long[] time;
    private long[] gyro_time;
    private int index = 0;
    private int gyro_index = 0;
    //private int sgn = 0;
    private double sgn = 0.0;
    private double prevVal1 = 0.0;
    private double prevSlope1 = 0.0;
    private double prevVal2 = 0.0;
    private double prevSlope2 = 0.0;
    private double prevVal3 = 0.0;
    private double prevSlope3 = 0.0;
    private int window;
    int fileNumber;

    private double prevExtreme1 = 0.0;
    private double prevExtreme2 = 0.0;
    private double prevExtreme3 = 0.0;
    private double threshold = 0.06; // CHANGED MAYBE CHANGE BACK TO 0.12
    private double signThreshold = 3.0;

    public WandSpeedTracker(WandCopingSkillActivity activity, int window) {

        this.activity = activity;
        this.window = window;
        vals1 = new double[window];
        vals2 = new double[window];
        vals3 = new double[window];
        time = new long[window];
        gyro1 = new int[window];
        gyro2 = new int[window];
        gyro3 = new int[window];
        gyro_time = new long[window];
        time = new long[window];
        Constants.fileStart++;
        fileNumber = Constants.fileStart;
    }

    public void writeValsToArray(int[] vals, long curTime) {
        double v0 = (double) vals[0];
        double v1 = (double) vals[1];
        double v2 = (double) vals[2];
        double mag = Math.sqrt(v0*v0 + v1*v1 + v2*v2);
        v0 = v0/mag;
        v1 = v1/mag;
        v2 = v2/mag;
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

    public void writeGyroToArray(int[] vals, long curTime) {
        gyro1[gyro_index] = vals[0];
        gyro2[gyro_index] = vals[1];
        gyro3[gyro_index] = vals[2];
        gyro_time[gyro_index] = curTime;
        gyro_index++;
        if (gyro_index >= window) {
            gyro_index = 0;
            String fileName = "gyroVals" + fileNumber + ".txt";
            ArrayList arrays = new ArrayList();
            arrays.add(gyro1);
            arrays.add(gyro2);
            arrays.add(gyro3);
            arrays.add(gyro_time);
            Log.e(Constants.LOG_TAG, "Writting gyro arrays to file");
            writeArraysToFile(fileName, arrays);
        }
    }

    public void countSigns() {
        //TODO try thresholdng the change in slope so it doesn't register if it's too small

        double signSum = 0;
        double sum1 = 0;
        double smooth1 = 0;
        for(int i = 0; i < vals1.length; i++) {
            sum1 += vals1[i];
        }
        smooth1 = sum1/vals1.length;
        double slope1 = smooth1 - prevVal1;
        if (slope1*prevSlope1 < 0) {
            if( (prevExtreme1 == 0.0) || (Math.abs(smooth1 - prevExtreme1) > threshold) ) {
                signSum++;
            }
            prevExtreme1 = smooth1;
        }
        prevVal1 = smooth1;
        prevSlope1 = slope1;


        double sum2 = 0;
        double smooth2 = 0;
        for(int i = 0; i < vals1.length; i++) {
            sum2 += vals1[i];
        }
        smooth2 = sum2/vals1.length;
        double slope2 = smooth2 - prevVal2;
        if (slope2*prevSlope2 < 0) {
            if( (prevExtreme2 == 0.0) || (Math.abs(smooth2 - prevExtreme2) > threshold) ) {
                signSum++;
            }
            prevExtreme2 = smooth2;
        }
        prevVal2 = smooth2;
        prevSlope2 = slope2;


        double sum3 = 0;
        double smooth3 = 0;
        for(int i = 0; i < vals1.length; i++) {
            sum3 += vals1[i];
        }
        smooth3 = sum3/vals1.length;
        double slope3 = smooth3 - prevVal3;
        if (slope3*prevSlope3 < 0) {
            if( (prevExtreme3 == 0.0) || (Math.abs(smooth3 - prevExtreme3) > threshold) ) {
                signSum++;
            }
            prevExtreme3 = smooth3;
        }
        prevVal3 = smooth3;
        prevSlope3 = slope3;

        /*
        if(signSum >= 2) {
            sgn++;
        }
        */
        if (signSum > 0) {
            Log.e(Constants.LOG_TAG, "signSum: " + signSum);
        }
        sgn += signSum/3.0;
        //Log.e(Constants.LOG_TAG, "sgn = "+sgn);
    }

    public int getSpeed() {
        int speed = -1;

        //TODO is the zero a good cutoff
        if(sgn <= signThreshold && sgn >= 0.1) {
            // Moving slowly
            speed = 1;
        } else if (sgn > signThreshold) {
            // Moving fast
            speed = 2;
        } else {
            speed = 0;
        }

        Log.e(Constants.LOG_TAG, "Sign count was: "+sgn);
        //int temp = sgn;
        double temp = sgn;
        writeRangeToFile(temp);
        sgn = 0.0;

        return speed;
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

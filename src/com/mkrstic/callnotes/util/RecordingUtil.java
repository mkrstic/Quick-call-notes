package com.mkrstic.callnotes.util;

/*
 * The application needs to have the permission to write to external storage
 * if the output file is written to the external storage, and also the
 * permission to record audio. These permissions must be set in the
 * application's AndroidManifest.xml file, with something like:
 *
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.RECORD_AUDIO" />
 *
 */

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.mkrstic.callnotes.model.CallInfo;

import java.io.File;
import java.io.IOException;


public class RecordingUtil {
    static final private double EMA_FILTER = 0.6;

    private double EMA = 0.0;
    private final String filePath;
    private MediaRecorder recorder = null;
    private Context context;

    public static boolean removeRecording(String filePath) {
        File file = new File(filePath);
        return file.delete();
    }

    public RecordingUtil(final Context context, final CallInfo callInfo) {
        this.context = context;
        String appDirStr = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + context.getApplicationInfo().packageName;
        File appDir = new File(appDirStr);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        filePath = appDirStr + "/" + "voice_message" + "_" + callInfo.getDateTimeInMillis() + ".3gp";
    }

    public void startNewRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(filePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e("RecordingUtil", "prepare() failed", e);
        }
        recorder.start();
    }

    public void stopRecording() {
        try {
            recorder.stop();
            recorder.release();
        } catch (IllegalStateException ise) {
            ise.printStackTrace();
        } catch (NullPointerException npe) {
        } finally {
            recorder = null;
        }
    }

    public void discardRecording() {
        if (recorder != null) {
            stopRecording();
        }
        removeRecording(filePath);
    }


    public String getRecordedFilepath() {
        return filePath;
    }

    public double getAmplitude() {
        if (recorder == null) {
            return 0;
        }
        // getMaxAmplitude interval [0, 32767]
        return  recorder.getMaxAmplitude() /2700.0;

    }

    public double getAmplitudeEMA() {
        double amp = getAmplitude();
        EMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * EMA;
        return EMA;
    }

    public static boolean hasMicrophone(final Context context) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion > 7) {
            return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
        }
        return true; // pretpostavka da mikrofon postoji jer se ne moze proveriti
    }




}
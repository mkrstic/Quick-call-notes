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
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import com.mkrstic.callnotes.model.CallInfo;

import java.io.File;
import java.io.IOException;


public class RecordingHelper {

    private final String filePath;

    private MediaRecorder recorder = null;

//    private MediaPlayer player = null;
    public static boolean removeRecording(String filePath) {
        File file = new File(filePath);
        return file.delete();
    }

    public RecordingHelper(final Context context, final CallInfo callInfo) {
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
            Log.e(RecordingHelper.class.getName(), "prepare() failed");
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
            //TODO
        } finally {
            recorder = null;
        }
    }

    public void discardRecording() {
        if (recorder != null) {
            stopRecording();
        }
        File file = new File(filePath);
        file.delete();
    }


    public String getRecordedFilepath() {
        return filePath;
    }

    public static void startPlaying(String filePath) {
        MediaPlayer player = new MediaPlayer();
        try {
            player.setDataSource(filePath);
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public void stopPlaying() {
        player.release();
        player = null;
    }*/
}
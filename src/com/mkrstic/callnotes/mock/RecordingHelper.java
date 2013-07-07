package com.mkrstic.callnotes.mock;

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

    public static boolean removeRecording(String filePath) {
        return true;
    }

    public RecordingHelper(final Context context, final CallInfo callInfo) {

        filePath = "/sdcard" + "/" + "voice_message" + "_" + callInfo.getDateTimeInMillis() + ".3gp";
    }

    public void startNewRecording() {

    }

    public void stopRecording() {

    }

    public void discardRecording() {

    }

    public String getRecordedFilepath() {
        return filePath;
    }

    public static void startPlaying(String filePath) {

    }

    /*public void stopPlaying() {
        player.release();
        player = null;
    }*/
}
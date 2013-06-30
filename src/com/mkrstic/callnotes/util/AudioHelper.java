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

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;


public class AudioHelper {

    private static String mFileName = null;

    private MediaRecorder recorder = null;

    private MediaPlayer player = null;

    public AudioHelper(String fileName) {
        this.mFileName = fileName;
    }

    public void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(mFileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(AudioHelper.class.getName(), "prepare() failed");
        }
    }

    public void stopPlaying() {
        player.release();
        player = null;
    }

    public void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(mFileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(AudioHelper.class.getName(), "prepare() failed");
        }

        recorder.start();
    }

    public void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    public void pauseRecording() {
        recorder.stop();
    }

    public void resumeRecording() {
        recorder.start();
    }


}
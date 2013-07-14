package com.mkrstic.callnotes.util;


import android.media.MediaPlayer;

import java.io.IOException;


public class PlayingUtil {

    private final String filePath;
    private MediaPlayer player = null;
    private MediaPlayer.OnCompletionListener onCompletionListener;

    public PlayingUtil(final String filePath) {
        this.filePath = filePath;
    }


    public void startPlaying() {
        try {
            player = new MediaPlayer();
            player.setDataSource(filePath);
            player.prepare();
            if (onCompletionListener != null) {
                player.setOnCompletionListener(onCompletionListener);
            }
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopPlaying() {
        if (player == null) {
            return;
        }
        player.stop();
        player.release();
        player = null;
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
        this.onCompletionListener = onCompletionListener;
    }
}
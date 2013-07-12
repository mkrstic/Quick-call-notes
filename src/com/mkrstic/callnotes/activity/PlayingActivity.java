package com.mkrstic.callnotes.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import com.mkrstic.callnotes.R;
import com.mkrstic.callnotes.util.PlayingHelper;
import com.mkrstic.callnotes.util.SharedPrefsHelper;

/**
 * Created by Mladen on 7/12/13.
 */
public class PlayingActivity extends Activity implements View.OnClickListener, MediaPlayer.OnCompletionListener {
    private String LOGTAG = "PlayingActivity";
    private Button closeBtn;
    private Chronometer durationChronometer;
    private PlayingHelper player;
    private boolean isFinishedPlaying;
    private String filePath;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_playing);
        Long id = getExtraId(getIntent());
        if (id == null) {
            Log.i(LOGTAG, "ID is null");
            finish();
        }
        filePath = getFilePath(id);
        if ("".equals(filePath)) {
            Toast.makeText(PlayingActivity.this, "File not found", Toast.LENGTH_LONG).show();
            finish();
        }
        bindViews();
        startPlaying();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.playing_btn_close) {
            if (player != null) {
                player.stopPlaying();
            }
            finish();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        durationChronometer.stop();
        isFinishedPlaying = true;
    }

    private Long getExtraId(final Intent intent) {
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            final String lastPathSegment = intent.getData().getLastPathSegment();
            long id = Long.parseLong(lastPathSegment);
            return id;
        }
        return null;
    }

    private String getFilePath(long fileId) {
        final SharedPrefsHelper sharedPrefsHelper = new SharedPrefsHelper(PlayingActivity.this);
        return sharedPrefsHelper.getFile(fileId);
    }

    private void startPlaying() {
        player = new PlayingHelper(filePath);
        player.setOnCompletionListener(this);
        player.startPlaying();
        durationChronometer.start();
    }

    private void bindViews() {
        durationChronometer = (Chronometer) findViewById(R.id.playing_chronometer_duration);
        closeBtn = (Button) findViewById(R.id.playing_btn_close);
        closeBtn.setOnClickListener(PlayingActivity.this);
    }
}
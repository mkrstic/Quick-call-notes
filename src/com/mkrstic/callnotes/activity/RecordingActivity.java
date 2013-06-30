package com.mkrstic.callnotes.activity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.mkrstic.callnotes.R;
import com.mkrstic.callnotes.model.Call;
import com.mkrstic.callnotes.util.AudioHelper;



/**
 * Created by mladen on 6/27/13.
 */
public class RecordingActivity extends SherlockFragmentActivity implements View.OnClickListener {
    private ImageButton stopBtn;
    private Chronometer chronometer;
    private AudioHelper recorder;
    private Call call;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);
        bindViews();
        stopBtn.setOnClickListener(this);
        call = mockReadCallExtra();
        setupRecorder();
        startRecording();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopRecording();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.recording_btn_record) {
            stopRecording();
        }
    }

    private void startRecording() {
        recorder.startRecording();
        chronometer.setBase(0);
        chronometer.start();
    }
    private void stopRecording() {
        chronometer.stop();
        recorder.stopRecording();
        recorder.startPlaying();
    }

    private void setupRecorder() {
        final String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        final String filename = sdcardPath + "/" + getApplication().getPackageName() + "_" + call.getName() + "_" + call.getDate() + ".3gp";
        recorder = new AudioHelper(filename);
    }

    private Call readCallExtra() {
        if (!getIntent().hasExtra(AfterCallActivity.EXTRA_CALL)) {
            finish();
        }
        return (Call) getIntent().getSerializableExtra(AfterCallActivity.EXTRA_CALL);
    }
    private Call mockReadCallExtra() {
        Call call = new Call();
        call.setDate(System.currentTimeMillis());
        call.setDuration(1024);
        call.setName("Ivana");
        call.setNumber("+38166202157");
        return call;
    }


    private void bindViews() {
        stopBtn = (ImageButton) findViewById(R.id.recording_btn_record);
        chronometer = (Chronometer) findViewById(R.id.recording_chronometer_duration);
    }


}
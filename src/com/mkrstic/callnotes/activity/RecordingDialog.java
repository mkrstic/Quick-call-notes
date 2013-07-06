package com.mkrstic.callnotes.activity;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

import com.mkrstic.callnotes.R;
import com.mkrstic.callnotes.model.CallInfo;
import com.mkrstic.callnotes.model.RecordingListener;
import com.mkrstic.callnotes.util.RecordingHelper;

/**
 * Created by mladen on 7/6/13.
 */
public class RecordingDialog extends DialogFragment implements View.OnClickListener {

    private Button stopBtn;
    private Chronometer durationChronometer;
    private RecordingHelper recordingHelper;
    private CallInfo callInfo;
    private boolean hasBeenPaused;
    private boolean isSavePressed;
    private RecordingListener recordingListener;

    static RecordingDialog newInstance(final CallInfo callInfo) {
        RecordingDialog instance = new RecordingDialog();
        Bundle args = new Bundle();
        args.putSerializable(AfterCallActivity.EXTRA_CALL, callInfo);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callInfo = (CallInfo) getArguments().getSerializable(AfterCallActivity.EXTRA_CALL);
        recordingHelper = new RecordingHelper(getActivity(), callInfo);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Record note");
        View layout = inflater.inflate(R.layout.dialog_recording, container);
        bindViews(layout);
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hasBeenPaused) {
            startRecording();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        pauseRecording();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!isSavePressed) {
            recordingHelper.discardRecording();
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.recording_btn_stop) {
            if (hasBeenPaused) {
                resumeRecording();
            } else {
                isSavePressed = true;
                recordingListener.onRecordingFinished(recordingHelper.getRecordedFilepath());
                dismiss();
            }
        }
    }

    private void startRecording() {
        recordingHelper.startNewRecording();
        //durationChronometer.setBase(0);
        durationChronometer.start();
        hasBeenPaused = false;
    }
    private void pauseRecording() {
        recordingHelper.pauseRecording();
        durationChronometer.stop();
        hasBeenPaused = true;
        stopBtn.setText(getString(R.string.action_resume));
    }

    private void resumeRecording() {
        recordingHelper.resumeRecording();
        durationChronometer.start();
        stopBtn.setText(getString(R.string.action_done));
    }


    private void bindViews(View layout) {
        durationChronometer = (Chronometer) layout.findViewById(R.id.recording_chronometer_duration);
        stopBtn = (Button) layout.findViewById(R.id.recording_btn_stop);
        stopBtn.setOnClickListener(RecordingDialog.this);
    }

    public void setRecordingListener(RecordingListener recordingListener) {
        this.recordingListener = recordingListener;
    }
}

package com.mkrstic.callnotes.activity;


import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.mkrstic.callnotes.R;
import com.mkrstic.callnotes.model.CallInfo;
import com.mkrstic.callnotes.model.RecordingListener;
import com.mkrstic.callnotes.util.CalendarHelper;
import com.mkrstic.callnotes.util.RecordingHelper;

import java.io.File;
import java.io.IOException;


/**
 * Created by mladen on 7/3/13.
 */
public class CreateNoteActivity extends SherlockFragmentActivity implements View.OnClickListener, RecordingListener {

    private CallInfo mCallInfo;
    private EditText noteEditText;
    private String LOGTAG = CreateNoteActivity.class.getName();
    private ImageView removeRecordingBtn;
    private TextView recordingInfoTxt;
    private String recordedFile;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        setupActionBar();
        mCallInfo = (CallInfo) getIntent().getSerializableExtra(AfterCallActivity.EXTRA_CALL);
        if (mCallInfo == null) {
            Toast.makeText(CreateNoteActivity.this, "Error. Call info not found", Toast.LENGTH_SHORT).show();
            finish();
        }
        bindViews();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.activity_create_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.createnote_menuitem_discard:
                finish();
                return true;
            case R.id.createnote_menuitem_record:
                showRecordingDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_done:
                addNote();
                break;
            case R.id.createnote_imageview_delete_recording:
                deleteRecording();
                break;
        }
    }

    @Override
    public void onRecordingFinished(String filename) {
        recordedFile = filename;
        removeRecordingBtn.setVisibility(View.VISIBLE);
        String shortName = "Audio message added";//filename.substring(filename.lastIndexOf("/"));
        recordingInfoTxt.setText(shortName);
        recordingInfoTxt.setVisibility(View.VISIBLE);
        RecordingHelper.startPlaying(filename);

    }

    private void setupActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        LayoutInflater inflater = (LayoutInflater) actionBar.getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(
                R.layout.actionbar_custom_view_done, null);
        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(this);

        // Show the custom action bar view and hide the normal Home icon and title.

        actionBar.setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView);
    }
    private void addNote() {
        String note = noteEditText.getText().toString().trim();
        if (recordedFile != null) {
            note += "\nRecording:\nfile://" + recordedFile;
        }
        mCallInfo.setNote(note);
        new AddNoteTask().execute(mCallInfo);
        finish();
    }

    private void deleteRecording() {
        if (recordedFile == null) {
            throw new NullPointerException("Recording file is null.");
        }
        File file = new File(recordedFile);
        boolean deleted = file.delete();
        if (deleted) {
            recordedFile = null;
            recordingInfoTxt.setVisibility(View.GONE);
            removeRecordingBtn.setVisibility(View.GONE);
        } else {
            Toast.makeText(CreateNoteActivity.this, "Error! File not removed", Toast.LENGTH_SHORT).show();
        }

    }

    private void showRecordingDialog() {
        if (recordedFile != null) {
            deleteRecording();
        }
        final String dialogTag = "dialog";
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment prev = fragmentManager.findFragmentByTag(dialogTag);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }
        fragmentTransaction.addToBackStack(null);
        RecordingDialog recordingDialog = RecordingDialog.newInstance(mCallInfo);
        recordingDialog.setRecordingListener(CreateNoteActivity.this);
        recordingDialog.show(fragmentManager, dialogTag);
    }


    private void bindViews() {
        noteEditText = (EditText) findViewById(R.id.createnote_edittext_note);
        recordingInfoTxt = (TextView) findViewById(R.id.createnote_txt_recording_msg);
        recordingInfoTxt.setVisibility(View.GONE);
        removeRecordingBtn = (ImageView) findViewById(R.id.createnote_imageview_delete_recording);
        removeRecordingBtn.setOnClickListener(this);
        removeRecordingBtn.setVisibility(View.GONE);
    }

    class AddNoteTask extends AsyncTask<CallInfo, Void, Integer> {

        private final Context context = getApplicationContext();

        @Override
        protected Integer doInBackground(CallInfo... params) {
            CalendarHelper calHelper = new CalendarHelper(context);
            CallInfo callInfo = params[0];
            return calHelper.addEvent(callInfo);
        }

        @Override
        protected void onPostExecute(Integer eventId) {
            super.onPostExecute(eventId);
            if (eventId != null) {
                Toast.makeText(CreateNoteActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
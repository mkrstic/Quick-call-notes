package com.mkrstic.callnotes.activity;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.mkrstic.callnotes.R;
import com.mkrstic.callnotes.model.CallInfo;
import com.mkrstic.callnotes.util.CalendarHelper;


/**
 * Created by mladen on 7/3/13.
 */
public class CreateNoteActivity extends SherlockActivity {

    private CallInfo mCallInfo;
    private EditText noteEditText;
    private String LOGTAG = CreateNoteActivity.class.getName();

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
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        LayoutInflater inflater = (LayoutInflater) actionBar.getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(
                R.layout.actionbar_custom_view_done, null);
        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addNote();
                    }
                });

        // Show the custom action bar view and hide the normal Home icon and title.

        actionBar.setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView);
    }
    private void addNote() {
        mCallInfo.setNote(noteEditText.getText().toString());
        new AddNoteTask().execute(mCallInfo);
        finish();
    }


    private void bindViews() {
        noteEditText = (EditText) findViewById(R.id.createnote_edittext_note);
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
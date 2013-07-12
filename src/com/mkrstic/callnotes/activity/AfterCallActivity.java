package com.mkrstic.callnotes.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mkrstic.callnotes.R;
import com.mkrstic.callnotes.model.CallInfo;
import com.mkrstic.callnotes.util.ContactHelper;

import java.io.InputStream;
import java.util.Date;


public class AfterCallActivity extends Activity implements View.OnClickListener {
    public static final String EXTRA_CALL = "AfterCallActivity_extra_call";

    private TextView nameTxtView;
    private TextView phoneTxtView;
    private TextView timeTxtView;
    private ProgressBar imageProgressBar;
    private CallInfo mCallInfo;
    private ImageView contactImage;
    private Button btnDiscard;
    private Button btnCreateNote;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_after_call);
        bindViews();
        loadInfoFromIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        loadInfoFromIntent(intent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.aftercall_btn_discard) {
            finish();
        }
        else if (v.getId() == R.id.aftercall_btn_new) {
            Intent intent = new Intent(AfterCallActivity.this, CreateNoteActivity.class);
            intent.putExtra(EXTRA_CALL, mCallInfo);
            startActivity(intent);
            finish();
        }
    }


    private void loadInfoFromIntent(final Intent intent) {
        mCallInfo = (CallInfo) intent.getSerializableExtra(EXTRA_CALL);
        if (mCallInfo == null) {
            finish();
        }
        populateViews(mCallInfo);
    }

    private void populateViews(final CallInfo callInfo) {
        phoneTxtView.setText(callInfo.getPhoneNumber());
        final String name = callInfo.getContactName();
        if (TextUtils.isEmpty(name)) {
            nameTxtView.setVisibility(View.INVISIBLE);
        } else {
            nameTxtView.setText(name);
        }
        Date callDate = new Date(callInfo.getDateTimeInMillis());
        java.text.DateFormat dateFormat = DateFormat.getTimeFormat(AfterCallActivity.this);
        timeTxtView.setText(dateFormat.format(callDate));
        new AddContactThumbTask().execute(callInfo.getPhoneNumber());
    }

    private void bindViews() {
        contactImage = (ImageView) findViewById(R.id.aftercall_imageview_contact);
        nameTxtView = (TextView) findViewById(R.id.aftercall_txt_name);
        phoneTxtView = (TextView) findViewById(R.id.aftercall_txt_phone);
        timeTxtView = (TextView) findViewById(R.id.aftercall_txt_time);
        imageProgressBar = (ProgressBar) findViewById(R.id.aftercall_progressbar_contact);
        btnDiscard = (Button) findViewById(R.id.aftercall_btn_discard);
        btnDiscard.setOnClickListener(this);
        btnCreateNote = (Button) findViewById(R.id.aftercall_btn_new);
        btnCreateNote.setOnClickListener(this);

    }

    class AddContactThumbTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imageProgressBar.setVisibility(View.VISIBLE);
            contactImage.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            final String phoneNumber = params[0];
            ContactHelper contactHelper = new ContactHelper(AfterCallActivity.this);
            Long contactId = contactHelper.fetchContact(phoneNumber);
            if (contactId != null) {
                InputStream is = contactHelper.fetchPhoto(contactId);
                if (is != null) {
                    return BitmapFactory.decodeStream(is);
                }

            }
            return null;

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageProgressBar.setVisibility(ProgressBar.GONE);
            if (result == null) {
                contactImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_contact));
            } else {
                contactImage.setImageBitmap(result);
            }
            contactImage.setVisibility(View.VISIBLE);
        }

    }

}

package com.mkrstic.callnotes.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.mkrstic.callnotes.R;
import com.mkrstic.callnotes.model.Call;
import com.mkrstic.callnotes.util.ContactHelper;

import java.io.InputStream;


public class AfterCallActivity extends Activity implements View.OnClickListener {
    public static final String EXTRA_CALL = "AfterCallActivity_extra_call";

    private TextView nameTxtView;
    private TextView phoneTxtView;
    private TextView timeTxtView;
    private ProgressBar imageProgressBar;
    private Call call;
    private ImageView contactImage;
    private Button btnDiscard;
    private Button btnCreateNote;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_call);
        bindViews();
        call = mockReadCallExtra();
        populateViews();
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.aftercall_btn_discard) {
            finish();
        }
        else if (v.getId() == R.id.aftercall_btn_new) {
            Intent intent = new Intent(AfterCallActivity.this, CreateNoteActivity.class);
            intent.putExtra(EXTRA_CALL, call);
            startActivity(intent);
            finish();
        }
    }

    private Call readCallExtra() {
        if (!getIntent().hasExtra(EXTRA_CALL)) {
            finish();
        }
        return (Call) getIntent().getSerializableExtra(EXTRA_CALL);
    }

    private Call mockReadCallExtra() {
        Call call = new Call();
        call.setDate(System.currentTimeMillis());
        call.setDuration(1024);
//        call.setName("Mladen");
//        call.setNumber("+38166200229");
        //call.setName("Ivana");
        call.setNumber("+381662021570");
        return call;
    }

    private void bindViews() {
        contactImage = (ImageView) findViewById(R.id.aftercall_imageview_contact);
        nameTxtView = (TextView) findViewById(R.id.aftercall_txt_name);
        phoneTxtView = (TextView) findViewById(R.id.aftercall_txt_phone);
        timeTxtView = (TextView) findViewById(R.id.aftercall_txt_time);
        imageProgressBar = (ProgressBar) findViewById(R.id.aftercall_progressbar_contact);
        btnDiscard = (Button) findViewById(R.id.aftercall_btn_discard);
        btnCreateNote = (Button) findViewById(R.id.aftercall_btn_new);
        btnCreateNote.setOnClickListener(this);
        btnDiscard.setOnClickListener(this);
    }

    private void populateViews() {
        phoneTxtView.setText(call.getPhoneNumber());
        final String name = call.getName();
        if (TextUtils.isEmpty(name)) {
            nameTxtView.setVisibility(View.INVISIBLE);
        } else {
            nameTxtView.setText(name);
        }
        String callDateTime = DateUtils.formatDateTime(AfterCallActivity.this, call.getDate(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_NO_YEAR | DateUtils.FORMAT_ABBREV_MONTH);
        timeTxtView.setText(callDateTime);
        imageProgressBar.setVisibility(View.VISIBLE);
        contactImage.setVisibility(View.INVISIBLE);
        new AddContactThumbTask().execute(call.getPhoneNumber());
    }


    class AddContactThumbTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            String phoneNumber = params[0];
            ContactHelper contactHelper = new ContactHelper(AfterCallActivity.this);
            Long contactId = contactHelper.fetchContactIdByPhone(phoneNumber);
            if (contactId != null) {
                InputStream is = contactHelper.fetchPhoto(contactId.longValue());
                if (is != null) {
                    return BitmapFactory.decodeStream(is);
                }

            }
            return null;

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageProgressBar.setVisibility(ProgressBar.INVISIBLE);
            if (result == null) {
                contactImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_contact));
            } else {
                contactImage.setImageBitmap(result);
            }
            contactImage.setVisibility(View.VISIBLE);
        }

    }

}

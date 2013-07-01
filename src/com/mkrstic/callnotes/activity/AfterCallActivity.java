package com.mkrstic.callnotes.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.mkrstic.callnotes.R;
import com.mkrstic.callnotes.com.mkrstic.callnotes.view.LinedEditText;
import com.mkrstic.callnotes.model.Call;
import com.mkrstic.callnotes.util.ContactHelper;

import java.io.InputStream;


public class AfterCallActivity extends SherlockFragmentActivity {
    public static final String EXTRA_CALL = "AfterCallActivity_extra_call";

    private TextView nameView;
    private TextView phoneView;
    private TextView timeView;
    private LinedEditText linedEditText;
    private ProgressBar progressContactImage;
    private Call call;
    private ImageView contactPhoto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_call_2);
        bindViews();
        call = mockReadCallExtra();
        final String phoneNumber = call.getPhoneNumber();
        phoneView.setText(call.getPhoneNumber());
        final String name = call.getName();
        if (TextUtils.isEmpty(name)) {
            nameView.setVisibility(View.INVISIBLE);
        } else {
            nameView.setText(name);
        }
        String callDateTime = DateUtils.formatDateTime(AfterCallActivity.this, call.getDate(),
                                                       DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_NO_YEAR | DateUtils.FORMAT_ABBREV_MONTH);
        timeView.setText(callDateTime);
        progressContactImage.setVisibility(View.VISIBLE);
        contactPhoto.setVisibility(View.INVISIBLE);
        linedEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(AfterCallActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        new AddContactThumbTask().execute(call.getPhoneNumber());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.after_call, menu);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_record:
                Toast.makeText(AfterCallActivity.this, "Record selected", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(AfterCallActivity.this, RecordingActivity.class);
//                intent.putExtra(EXTRA_CALL, call);
//                startActivity(intent);
                break;
            case R.id.menu_action_save:
                Toast.makeText(AfterCallActivity.this, "Save selected", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

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
        contactPhoto = (ImageView) findViewById(R.id.aftercall_imageview_contact);
        nameView = (TextView) findViewById(R.id.aftercall_txt_name);
        phoneView = (TextView) findViewById(R.id.aftercall_txt_phone);
        timeView = (TextView) findViewById(R.id.aftercall_txt_time);
        progressContactImage = (ProgressBar) findViewById(R.id.aftercall_progressbar_contact);
        linedEditText = (LinedEditText) findViewById(R.id.aftercall_edittext_note);
    }


    class AddContactThumbTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
//			QuickContactHelper contactHelper = new QuickContactHelper(AfterCallActivity.this, phoneNumber);
//			final Integer thumbnailId = contactHelper.fetchThumbnailId();
//			if (thumbnailId != null) {
//				final Bitmap thumbnail = contactHelper.fetchThumbnail(thumbnailId);
//				return thumbnail;
//			}
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
            progressContactImage.setVisibility(ProgressBar.INVISIBLE);
            if (result == null) {
                contactPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_contact));
            } else {
                contactPhoto.setImageBitmap(result);
            }
            contactPhoto.setVisibility(View.VISIBLE);
        }

    }

}

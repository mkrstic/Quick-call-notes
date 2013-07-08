package com.mkrstic.callnotes.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mkrstic.callnotes.model.CallInfo;

/**
 * Created by mladen on 7/4/13.
 */
public class LauncherActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, AfterCallActivity.class);
        intent.putExtra(AfterCallActivity.EXTRA_CALL, mockReadCallExtra());
        startActivity(intent);
        finish();
    }

    private CallInfo mockReadCallExtra() {
        CallInfo callInfo = new CallInfo();
        callInfo.setDateTimeInMillis(System.currentTimeMillis());
        callInfo.setDurationInSeconds(1024);
        callInfo.setContactName("Clemens Raphael");
        callInfo.setNumber("(541) 754-3010");
        return callInfo;
    }
}
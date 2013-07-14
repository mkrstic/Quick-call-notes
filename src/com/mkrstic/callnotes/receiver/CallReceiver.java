package com.mkrstic.callnotes.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.mkrstic.callnotes.activity.AfterCallActivity;
import com.mkrstic.callnotes.model.CallInfo;

/**
 * Created by mladen on 7/6/13.
 */
public class CallReceiver extends BroadcastReceiver {

    private Context mContext;
    private Intent mIntent;
    private static int prevState;
    public String LOGTAG = "CallReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        mIntent = intent;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        tm.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
    }

    private class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            handleStateChange(state);
            prevState = state;
        }
        private void handleStateChange(int curState) {
            if (prevState == TelephonyManager.CALL_STATE_OFFHOOK && curState == TelephonyManager.CALL_STATE_IDLE) {
                Handler handler = new Handler();
                handler.postDelayed(new StartAfterCallActivity(), 500);
                
            }
        }
    }
    class StartAfterCallActivity implements Runnable {

        @Override
        public void run() {
            final CallInfo lastCallInfo = findLastCall();
            if (lastCallInfo == null || lastCallInfo.getType() == CallLog.Calls.MISSED_TYPE) {
                return;
            }
            Intent intent = new Intent(mContext, AfterCallActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(AfterCallActivity.EXTRA_CALL, lastCallInfo);
            mContext.startActivity(intent);
        }

        private CallInfo findLastCall() {
            final String[] projection = new String[] { CallLog.Calls.NUMBER,
                    CallLog.Calls.CACHED_NAME, CallLog.Calls.DATE, CallLog.Calls.DURATION, CallLog.Calls.TYPE };
            final String sortBy = CallLog.Calls.DATE + " desc limit 1";
            final Cursor cursor = mContext.getContentResolver().query(
                    CallLog.Calls.CONTENT_URI, projection, null, null, sortBy);
            if (cursor != null && cursor.moveToFirst()) {
                CallInfo lastCallInfo = new CallInfo();
                lastCallInfo.setNumber(cursor.getString(0));
                lastCallInfo.setContactName(cursor.getString(1));
                lastCallInfo.setDateTimeInMillis(cursor.getLong(2));
                lastCallInfo.setDurationInSeconds(cursor.getInt(3));
                lastCallInfo.setType(cursor.getInt(4));
                cursor.close();
                return lastCallInfo;
            }
            return null;
        }

    }


}

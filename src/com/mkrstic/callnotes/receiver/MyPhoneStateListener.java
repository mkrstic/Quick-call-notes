package com.mkrstic.callnotes.receiver;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.mkrstic.callnotes.activity.AfterCallActivity;
import com.mkrstic.callnotes.model.CallInfo;

public class MyPhoneStateListener extends PhoneStateListener {
	private static final String TAG = MyPhoneStateListener.class.getName();

	private boolean isPhoneCalling = false;
	private final Context context;

	public MyPhoneStateListener(Context context) {
		this.context = context;
	}

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		super.onCallStateChanged(state, incomingNumber);
		if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
			Log.i(TAG, "OFFHOOK");
			isPhoneCalling = true;
		} else if (state == TelephonyManager.CALL_STATE_IDLE) {
			Log.i(TAG, "IDLE");
			if (isPhoneCalling) {
                isPhoneCalling = false;
                Handler handler = new Handler();
                handler.postDelayed(new StartAfterCallActivity(context), 240);
                if (!TextUtils.isEmpty(incomingNumber)) {
                    Log.i(TAG, "incomingNumber="+incomingNumber);
                }
			}
		}
	}

	class StartAfterCallActivity implements Runnable {

		private final Context context;

		public StartAfterCallActivity(Context context) {
			this.context = context;
		}

		@Override
		public void run() {
			final CallInfo lastCallInfo = findLastCall();
            if (lastCallInfo == null || lastCallInfo.getType() == CallLog.Calls.MISSED_TYPE) {
                return;
            }
			Intent intent = new Intent(context, AfterCallActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(AfterCallActivity.EXTRA_CALL, lastCallInfo);
			Log.d(TAG, "lastCallInfo=" + lastCallInfo);
			context.startActivity(intent);
		}

		private CallInfo findLastCall() {
			final String[] projection = new String[] { Calls.NUMBER,
					Calls.CACHED_NAME, Calls.DATE, Calls.DURATION, Calls.TYPE };
			final String sortBy = Calls.DATE + " desc limit 1";
			final Cursor cursor = context.getContentResolver().query(
					Calls.CONTENT_URI, projection, null, null, sortBy);
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

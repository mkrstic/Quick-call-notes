package com.mkrstic.callnotes.receiver;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.provider.CallLog.Calls;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.mkrstic.callnotes.activity.AfterCallActivity;
import com.mkrstic.callnotes.model.Call;

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
				handler.postDelayed(new StartAfterCallActivity(context), 500);
			}
		}
	}

	class StartAfterCallActivity implements Runnable {

		private Context context;

		public StartAfterCallActivity(Context context) {
			this.context = context;
		}

		@Override
		public void run() {
			final Call lastCall = findLastCall();
			if (lastCall != null) {
				Intent intent = new Intent(context, AfterCallActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra(AfterCallActivity.EXTRA_CALL, lastCall);
				Log.d(TAG, "lastCall=" + lastCall);
				context.startActivity(intent);
			}
		}

		private Call findLastCall() {
			final String[] projection = new String[] { Calls.NUMBER,
					Calls.CACHED_NAME, Calls.DATE, Calls.DURATION, Calls.TYPE };
			final String sortBy = Calls.DATE + " desc limit 1";
			final Cursor cursor = context.getContentResolver().query(
					Calls.CONTENT_URI, projection, null, null, sortBy);
			if (cursor.moveToFirst()) {
				Call lastCall = new Call();
				lastCall.setNumber(cursor.getString(cursor
						.getColumnIndex(Calls.NUMBER)));
				lastCall.setDate(cursor.getLong(cursor
						.getColumnIndex(Calls.DATE)));
				lastCall.setDuration(cursor.getLong(cursor
						.getColumnIndex(Calls.DURATION)));
				lastCall.setName(cursor.getString(cursor
						.getColumnIndex(Calls.CACHED_NAME)));
				lastCall.setType(cursor.getInt(cursor
						.getColumnIndex(Calls.TYPE)));
				cursor.close();
				return lastCall;
			}
			return null;
		}

	}

}

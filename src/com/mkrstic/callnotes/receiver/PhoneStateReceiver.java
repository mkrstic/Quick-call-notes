package com.mkrstic.callnotes.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class PhoneStateReceiver extends BroadcastReceiver {
	private static PhoneStateListener phoneStateListener;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (phoneStateListener == null) {
			phoneStateListener = new MyPhoneStateListener(context);
			final TelephonyManager manager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			manager.listen(phoneStateListener,
					PhoneStateListener.LISTEN_CALL_STATE);
		}
	}
}

package com.mkrstic.callnotes.mock;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.text.TextUtils;

import com.mkrstic.callnotes.R;
import com.mkrstic.callnotes.model.CallInfo;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by mladen on 7/4/13.
 */
public class CalendarHelper {



    public CalendarHelper(Context context) {

    }

    public int getCalendar() {
        return 1;
    }

    public int addEvent(CallInfo callInfo) {
        return 0;
    }

    private int addEvent14(CallInfo callInfo) {
        return 1;
    }


    private int addEventPre14(final Uri contentUri, CallInfo callInfo) {
        return 45;
    }

    private int getOrCreateAppCalendar() {
        return 2;
    }

    private int getSelectedCalendarPre14(Uri contentUri) {
        return 3;
    }



}

package com.mkrstic.callnotes.util;

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
public class CalendarUtil {

    private final Context context;

    public CalendarUtil(Context context) {
        this.context = context;
    }

    public int getCalendar() {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion < 14) {
            final String contentUriStr = sdkVersion < 8 ? "content://calendar" : "content://com.android.calendar";
            final Uri contentUri = Uri.parse(contentUriStr);
            return getSelectedCalendarPre14(contentUri);
        } else {
            return getOrCreateAppCalendar();
        }
    }

    public int addEvent(CallInfo callInfo) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion < 14) {
            final String contentUriStr = sdkVersion < 8 ? "content://calendar" : "content://com.android.calendar";
            final Uri contentUri = Uri.parse(contentUriStr);
            return addEventPre14(contentUri, callInfo);
        } else {
            return addEvent14(callInfo);
        }
    }

    private int addEvent14(CallInfo callInfo) {
        //int calendarId = getOrCreateAppCalendar();
        int calendarId = 1;
        final ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, callInfo.getDateTimeInMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(callInfo.getDateTimeInMillis());
        calendar.add(Calendar.SECOND, callInfo.getDurationInSeconds());
        values.put(CalendarContract.Events.DTEND, calendar.getTimeInMillis());
        String contactName = TextUtils.isEmpty(callInfo.getContactName()) ? "" : callInfo.getContactName();
        values.put(CalendarContract.Events.TITLE, context.getString(R.string.calendar_event_title, contactName, callInfo.getPhoneNumber()));
        values.put(CalendarContract.Events.DESCRIPTION, callInfo.getNote());
        values.put(CalendarContract.Events.CALENDAR_ID, calendarId);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getDisplayName());
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        if (uri == null) {
            throw new RuntimeException("Could not create event to calendar ID " + calendarId);
        }
        return Integer.parseInt(uri.getLastPathSegment());
    }


    private int addEventPre14(final Uri contentUri, CallInfo callInfo) {
        int calendarId = getSelectedCalendarPre14(contentUri);
        final Uri eventsUri = Uri.withAppendedPath(contentUri, "events");
        ContentValues values = new ContentValues();
        values.put("dtstart", callInfo.getDateTimeInMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(callInfo.getDateTimeInMillis());
        calendar.add(Calendar.SECOND, callInfo.getDurationInSeconds());
        values.put("dtend", calendar.getTimeInMillis());
        String contactName = TextUtils.isEmpty(callInfo.getContactName()) ? "" : callInfo.getContactName();
        values.put("title", context.getString(R.string.calendar_event_title, contactName, callInfo.getPhoneNumber()));
        values.put("description", callInfo.getNote());
        values.put("calendar_id", calendarId);
        Uri uri = context.getContentResolver().insert(eventsUri, values);
        if (uri == null) {
            throw new RuntimeException("Could not create event to calendar ID " + calendarId);
        }
        return Integer.parseInt(uri.getLastPathSegment());
    }

    private int getOrCreateAppCalendar() {
        // TODO: Test this method
        if (true) {
            return 1;
        }
        final String[] projection =  {CalendarContract.Calendars._ID};
        final String selection = "(" + CalendarContract.Calendars.NAME + " = ?" + ")";
        String calendarName = context.getString(R.string.app_name);
        final String[] selectionArgs = {calendarName};
        final ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(CalendarContract.Calendars.CONTENT_URI, projection, selection, selectionArgs, null);
        int calendarId;
        if (cursor != null && cursor.moveToFirst()) {
            calendarId = cursor.getInt(0);
            cursor.close();
        } else {
            ContentValues cv = new ContentValues();
            cv.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
            cv.put(CalendarContract.Calendars.NAME, calendarName);
            cv.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, calendarName);
            cv.put(CalendarContract.Calendars.VISIBLE, 1);
            cv.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, TimeZone.getDefault().getID());
            Uri result = cr.insert(CalendarContract.Calendars.CONTENT_URI, cv);
            if (result == null) {
                throw new RuntimeException("Could not create calendar");
            }
            calendarId = Integer.parseInt(result.getLastPathSegment());
        }
        return calendarId;
    }

    private int getSelectedCalendarPre14(Uri contentUri) {
        Uri calendarsUri = Uri.withAppendedPath(contentUri, "calendars");
        String[] projection = {"_id"};
        String selection = "selected = ?";
        String[] selectionArgs = {"1"};
        Cursor cursor = context.getContentResolver().query(calendarsUri, projection, selection, selectionArgs, null);
        if (cursor != null && cursor.moveToFirst()) {
            int calendarId = cursor.getInt(0);
            cursor.close();
            return calendarId;
        } else {
            throw new RuntimeException("Could not get calendar");
        }
    }



}

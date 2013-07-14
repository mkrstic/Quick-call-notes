package com.mkrstic.callnotes.mock;

import android.content.Context;
import android.net.Uri;

import com.mkrstic.callnotes.model.CallInfo;

/**
 * Created by mladen on 7/4/13.
 */
public class CalendarUtil {



    public CalendarUtil(Context context) {

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

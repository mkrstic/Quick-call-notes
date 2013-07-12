package com.mkrstic.callnotes.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Mladen on 7/12/13.
 */
public class SharedPrefsHelper {

    private final static String PREFS_NAME = "CALL_NOTES_PREFS";
    private final static String KEY_NUM_EVENTS = "KEY_NUM_EVENTS";
    private final Context context;
    private final SharedPreferences sharedPreferences;

    public SharedPrefsHelper(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public long countFiles() {
        return sharedPreferences.getLong(KEY_NUM_EVENTS, 0);
    }

    public long putFile(String filename) {
        long nextFileId = countFiles()+1;
        boolean saved = sharedPreferences.edit().putString(String.valueOf(nextFileId), filename).putLong(KEY_NUM_EVENTS, nextFileId).commit();
        if (!saved) {
            throw new RuntimeException("SharedPreferences not saved, commit() returned false. filename=" + filename + ", nextFileId=" + nextFileId);
        }
        return nextFileId;
    }

    public String getFile(final long fileId) {
        return sharedPreferences.getString(String.valueOf(fileId), "");
    }
}

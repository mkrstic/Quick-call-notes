package com.mkrstic.callnotes.mock;

import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by mladen on 6/30/13.
 */
public class ContactHelper {


    public ContactHelper(Context context) {

    }

    public Long fetchContact(final String phoneNumber) {
        return null;

    }

    public InputStream fetchPhoto(long contactId) {
        return null;
    }

    private InputStream openDisplayPhoto(long contactId) {
        return null;
    }

    private InputStream openPhoto(long contactId) {
        return null;
    }

    public Long getContactId() {
        return null;
    }

    public String getDisplayName() {
        return null;
    }
}



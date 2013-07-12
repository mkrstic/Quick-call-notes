
package com.mkrstic.callnotes;

import android.app.Application;
import android.os.Environment;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

@ReportsCrashes(formKey = "YOUR_FORM_KEY")
public class AfterCallNotesApplication
        extends Application {

    private String homeDir;
    private String audioDir;

    @Override
    public void onCreate() {
        super.onCreate();
        //ACRA.init(this);

    }


}

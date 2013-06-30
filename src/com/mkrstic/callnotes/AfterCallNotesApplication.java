
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
        //ACRA.init(this);
        super.onCreate();
    }


    public String getHomeDir() {
        if (homeDir == null) {
            homeDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_folder_name) + "/";
        }
        return homeDir;
    }

    public String getAudioDir() {
        if (audioDir == null) {
            audioDir = getHomeDir()+"/audio/";
        }
        return audioDir;
    }
}

package com.truedev.neon.Utils;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by Lakshay on 12-03-2015.
 */
public class ApplicationController extends Application {

    //    public static ArrayList<FileInfo> selectedImages = new ArrayList<FileInfo>();
    //public static ArrayList<String> selectedFiles = new ArrayList<>();
    private static final String TAG = "ApplicationController";
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        mContext = this;
    }


}

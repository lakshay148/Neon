package com.truedev.neon.Utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.truedev.neon.Model.ImageModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Lakshay on 17-03-2015.
 */
public class CommonUtils {

    public static void createNotification(Context context, int smallIcon, String title, String content, Intent resultIntent, int imageUploadNotifId) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(smallIcon)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(content);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(imageUploadNotifId, mBuilder.build());
    }

    public static String getStringSharedPreference(Context context, String key, String defaultValue) {
        SharedPreferences preferences = context.getApplicationContext().
                getSharedPreferences(
                        Constants.APP_SHARED_PREFERENCE,
                        Context.MODE_PRIVATE
                );

        if (preferences.contains(key)) {
            return preferences.getString(key, defaultValue);
        } else {
            return defaultValue;
        }
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        return width;
    }

    public static ArrayList<FileInfo> removeFileInfo(ArrayList<FileInfo> source, FileInfo fileInfo) {
        for (FileInfo fileInfo1 : source) {
            if (fileInfo.getFilePath().equals(fileInfo1.getFilePath())) {
                source.remove(fileInfo);
                break;
            }
        }
        return source;
    }

    public static void removeFileInfo(ArrayList<ImageModel> source, ArrayList<FileInfo> removeFiles, Boolean flag) {
        if (source == null)
            return;
        for (FileInfo fileInfo : removeFiles) {
            for(ImageModel model:source){
                if(model.getImagePath().equals(fileInfo.getFilePath())){
                  source.remove(model);
                }
            }

        }
    }

    public static void addFileInfo(ArrayList<FileInfo> source, FileInfo fileInfo) {
        Boolean alreadyPresent = false;
        for (FileInfo fileInfo1 : source) {
            if (fileInfo.getFilePath().equals(fileInfo1.getFilePath())) {
                alreadyPresent = true;
                break;
            }
        }
        if (!alreadyPresent)
            source.add(fileInfo);
    }

    public static void removeFileInfo(ArrayList<FileInfo> source, String filePath) {
        for (FileInfo fileInfo : source) {
            if (filePath.equals(fileInfo.getFilePath())) {
                source.remove(fileInfo);
                break;
            }
        }
    }

    public static void removeFileInfo(ArrayList<FileInfo> source, ArrayList<FileInfo> fileInfos) {
        ArrayList<FileInfo> toBeDeleted = new ArrayList<>();
        for (FileInfo fileInfo : source) {
            for (FileInfo fileInfo1 : fileInfos) {
                if (fileInfo1.getFilePath().equals(fileInfo.getFilePath())) {
                    toBeDeleted.add(fileInfo);
                    break;
                }
            }
        }
        source.removeAll(toBeDeleted);
    }

    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static int getExifRotation(File imageFile) {
        if (imageFile == null) return 0;
        try {
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            // We only recognize a subset of orientation tag values
            switch (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
                default:
                    return ExifInterface.ORIENTATION_UNDEFINED;
            }
        } catch (IOException e) {
            Log.e("Error getting Exif data", e.getMessage());
            return 0;
        }
    }

   public static int Orientation=1;
    public static boolean setExifRotation(File sourceFile,int counter) {
         Log.e("HIMMMMM",getExifRotation(sourceFile)+"");
        try {
            ExifInterface exifSource = new ExifInterface(sourceFile.getAbsolutePath());
            int Orientation= getExifRotation(sourceFile);//Integer.parseInt(exifSource.getAttribute(ExifInterface.TAG_ORIENTATION));
            int changedOrientation=getExifRotation(sourceFile);

            switch (Orientation){
                case ExifInterface.ORIENTATION_NORMAL:
                    changedOrientation=ExifInterface.ORIENTATION_ROTATE_270;
                break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    changedOrientation=ExifInterface.ORIENTATION_ROTATE_180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    changedOrientation=ExifInterface.ORIENTATION_ROTATE_270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    changedOrientation=ExifInterface.ORIENTATION_NORMAL;
                    break;

            }

            exifSource.setAttribute(ExifInterface.TAG_ORIENTATION, changedOrientation + "") ;
            exifSource.saveAttributes();
            return true;
        } catch (IOException e) {
            Log.e("Error copying Exif data", e.getMessage());
            return false;
        }
    }


    public static Uri getImageStoreUri() {
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }
}

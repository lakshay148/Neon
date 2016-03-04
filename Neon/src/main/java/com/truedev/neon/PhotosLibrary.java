package com.truedev;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.truedev.neon.Activity.CameraPriorityActivity;
import com.truedev.neon.Activity.NeutralActivity;
import com.truedev.neon.Fragments.CameraPriorityFragment;
import com.truedev.neon.Utils.PhotoParams;

/**
 * Created by Lakshay on 21-05-2015.
 */
public class PhotosLibrary  {
    public static final int REQUEST_CODE_UPLOAD_PHOTOS = 2004;

    public static void collectPhotos(Context context, PhotoParams params){

        switch (params.getMode()){
            case CAMERA_PRIORITY:
                Intent newIntent = new Intent(context, CameraPriorityActivity.class);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.putExtra("photoParams", params);
                ((Activity)context).startActivityForResult(newIntent, params.getRequestCode());
                ((Activity) context).overridePendingTransition(R.anim.slide_in_bottom,R.anim.do_nothing);
                break;

            case GALLERY_PRIORITY:
                break;

            case NEUTRAL:
                params.setOrientation(PhotoParams.CameraOrientation.LANDSCAPE);
                Intent cameraNeutralIntent = new Intent(context, NeutralActivity.class);
                cameraNeutralIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                cameraNeutralIntent.putExtra("photoParams", params);
                ((Activity)context).startActivityForResult(cameraNeutralIntent, REQUEST_CODE_UPLOAD_PHOTOS);
                break;
        }
    }

    public static Fragment getPhotosFragment(PhotoParams params){
        Fragment fragment = null;
        switch (params.getMode()){
            case CAMERA_PRIORITY:
                fragment = CameraPriorityFragment.getInstance(params);
                break;
            case GALLERY_PRIORITY:
                break;
            case NEUTRAL:
                break;
        }
        return fragment;
    }
}

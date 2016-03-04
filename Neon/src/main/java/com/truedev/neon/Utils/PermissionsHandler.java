package com.truedev.neon.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.HashMap;

/**
 * Created by lakshaygirdhar on 12/1/16.
 */
public class PermissionsHandler {


    private static int requestCodeCamera = 100;
    private static int requestCodeStorage = 101;

    public static String CAMERA = "camera";
    public static String STORAGE = "storage";

    private static HashMap<String , Integer> permissionMap = new HashMap<>();

    public static HashMap<String, Integer> getPermissionMap() {
        permissionMap.put(CAMERA, requestCodeCamera);
        permissionMap.put(STORAGE, requestCodeStorage);
        return permissionMap;
    }

    public static void checkPermission(Activity activity, String permission){

        if(ContextCompat.checkSelfPermission(activity,permission) != PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(activity, new String[]{permission}, getPermissionMap().get(permission));
        }
    }

}

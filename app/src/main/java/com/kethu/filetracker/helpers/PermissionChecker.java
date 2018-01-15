package com.kethu.filetracker.helpers;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by administrator1 on 23-Feb-17.
 */

public class PermissionChecker {


    @SuppressLint("WrongConstant")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context,String[] permissions)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            int res = 0;
            for (String str : permissions) {

                res = context.checkCallingOrSelfPermission(str);

                if (!(res == PackageManager.PERMISSION_GRANTED)) {
                    return false;
                }
            }
            return true;


        } else {
            return true;
        }
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void reqPermissions(final Activity context, final String[] permissions) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.requestPermissions(permissions, 100);
        }
    }
}
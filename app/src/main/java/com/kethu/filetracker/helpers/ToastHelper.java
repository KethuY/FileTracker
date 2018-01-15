package com.kethu.filetracker.helpers;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by LOGICON on 13-12-2017.
 */

public class ToastHelper {
    public static void showToastLenShort(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
    public static void showToastLenLong(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
    public static void noInternet(final Context context){
        ToastHelper.showToastLenShort(context,"No internet connection");
    }

   }

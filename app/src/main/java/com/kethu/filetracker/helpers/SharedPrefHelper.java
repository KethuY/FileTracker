package com.kethu.filetracker.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.kethu.filetracker.R;

/**
 * Created by satya on 06-Jan-18.
 */

public class SharedPrefHelper {
    public static void seFirstTimeLogin(Context context, boolean authToken) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.is_first_time_login), authToken);
        editor.apply();
    }

    public static boolean isFirstTime(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(context.getString(R.string.is_first_time_login), false);
    }
}

package com.kethu.filetracker.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by LOGICON on 13-12-2017.
 */

public class NetworkHelper {

    public static boolean hasNetworkConnection(Context context) {

        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;

            if (cm != null) {
                networkInfo = cm.getActiveNetworkInfo();

                if (networkInfo != null) {

                    if ((ConnectivityManager.TYPE_WIFI == networkInfo.getType() || ConnectivityManager.TYPE_MOBILE == networkInfo.getType()) && networkInfo.isConnected())
                        return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }

}

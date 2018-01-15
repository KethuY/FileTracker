package com.kethu.filetracker.helpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by LOGICON on 20-12-2017.
 */

public class SnackbarHelper {

    public static void noInternet(View view, final Context context) {

        Snackbar snackbar = Snackbar
                .make(view, "No internet connection!", Snackbar.LENGTH_LONG)
                .setAction("Turn On", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();

        /*Snackbar snackbar = Snackbar
        .make(coordinatorLayout, "Message is deleted", Snackbar.LENGTH_LONG)
        .setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "Message is restored!", Snackbar.LENGTH_SHORT);
                snackbar1.show();
            }
        });

snackbar.show();*/
    }



    public static void showSnackBar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View snackView = snackbar.getView();
        TextView tv = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.parseColor("#f8f206"));
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackView.getLayoutParams();
        params.gravity = Gravity.CENTER;
        params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        snackView.setLayoutParams(params);
        snackbar.show();
    }
}

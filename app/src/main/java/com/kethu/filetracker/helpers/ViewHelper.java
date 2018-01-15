package com.kethu.filetracker.helpers;

import android.annotation.SuppressLint;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by LOGICON on 13-12-2017.
 */

public class ViewHelper {

    @SuppressLint("RestrictedApi")
   public static void removeShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
        }
    }

    public static String getString(TextInputLayout textInputLayout) {
        String text = "";

        if (textInputLayout != null && textInputLayout.getEditText() != null) {
            text = textInputLayout.getEditText().getText().toString().trim();
        }

        return text;
    }
    public static String getString(EditText textInputLayout) {
        String text = "";

        if (textInputLayout != null ) {
            text = textInputLayout.getText().toString().trim();
        }

        return text;
    }
    public static String getString(TextView textInputLayout) {
        String text = "";

        if (textInputLayout != null ) {
            text = textInputLayout.getText().toString().trim();
        }

        return text;
    }

    public static String getString(Spinner spinner) {
        String text = "";

        try {
            if (spinner != null ) {
                text = spinner.getSelectedItem().toString().trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return text;
    }
    public static boolean isNull(EditText textInputLayout) {
        return textInputLayout == null;
    }
}

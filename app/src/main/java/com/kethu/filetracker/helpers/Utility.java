package com.kethu.filetracker.helpers;

import java.util.Random;

/**
 * Created by satya on 30-Dec-17.
 */

public class Utility {

    public static int randInt(int min, int max) {
        // Usually this should be a field rather than a method variable so
        // that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static String isNull(String value) {
        if (value != null && value.length() > 0){
            return value.trim();
        }


        return "";
    }
}

package com.mark.pictureselector;

import android.util.Log;

public class L {

    private static boolean debug = true;

    public static void setDebug(boolean d) {
        debug = d;
    }

    public static void e(String tag, String msg) {
        if (debug) {
            Log.e(tag, msg);
        }
    }
}

package com.test.scanner.utils.android;

import android.content.res.Resources;

public class DimensionUtils {

    public static float dpToPixels(int dp) {
        return Resources.getSystem().getDisplayMetrics().density * dp;
    }

}

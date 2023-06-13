package com.gh.sammie.ghanawater.Utils;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;

public class ScreenUtility {
    private Activity activity;
    private float dpWidth, dpHeight;

    public ScreenUtility(Activity activity) {
        this.activity = activity;

        //display class
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();

        display.getMetrics(outMetrics);

        float density = activity.getResources().getDisplayMetrics().density;

        //find dpH nd width
        dpHeight = outMetrics.heightPixels / density;
        dpWidth = outMetrics.widthPixels / density;

    }

    public float getDpWidth() {
        return dpWidth;
    }

    public float getDpHeight() {
        return dpHeight;
    }
}

package com.gh.sammie.ghanawater.Common;

import android.content.Context;
import android.content.SharedPreferences;

public class OnceMoMO {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;

    // shared pref mode
    private int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "Welcome-Momo6";

    private static final String IS_FIRST_TIME_FRAGMENT = "IsFirstTimeMomoPay6";

    public OnceMoMO(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_FRAGMENT, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_FRAGMENT, true);
    }

}
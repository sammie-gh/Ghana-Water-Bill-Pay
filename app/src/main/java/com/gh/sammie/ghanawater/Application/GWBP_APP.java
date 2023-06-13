package com.gh.sammie.ghanawater.Application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

public class GWBP_APP extends Application {

    public static String MERCHANT_KEY = "1522254751831";

    public static Activity currentActivity;
    private static GWBP_APP mInstance;
    // email or mobile number associated with the merchant account
    public static String EMAIL_OR_MOBILE_NUMBER = "ofori.d.evans@gmail.com";


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception e) {
            Log.e("APPLICATION", "onCreate: I Know Why Hehehe");
        }


    }
}

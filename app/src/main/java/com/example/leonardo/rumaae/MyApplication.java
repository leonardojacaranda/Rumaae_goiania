package com.example.leonardo.rumaae;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Created by Leonardo on 18/07/2016.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}

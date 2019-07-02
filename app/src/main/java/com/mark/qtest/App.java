package com.mark.qtest;

import android.app.Application;

import com.mark.pictureselector.PictureSelector;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PictureSelector.init(this);
    }
}

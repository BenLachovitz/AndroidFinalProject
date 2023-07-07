package com.example.a23b_11345_b_finalproject.Utillities;

import android.app.Application;

public class App  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SignalGenerator.init(this);
    }
}

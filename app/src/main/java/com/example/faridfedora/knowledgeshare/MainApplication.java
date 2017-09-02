package com.example.faridfedora.knowledgeshare;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by faridfedora on 6/9/17.
 */

public class MainApplication extends Application {


    public static SharedPreferences setting;
    public static SharedPreferences.Editor editor;
    @Override
    public void onCreate() {
        super.onCreate();
        setting=getSharedPreferences("sh",MODE_PRIVATE);
        editor=setting.edit();



    }
}

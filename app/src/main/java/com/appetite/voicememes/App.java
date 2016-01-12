package com.appetite.voicememes;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;

import com.appetite.Database.Database;

/**
 * Created by vignesh on 25/09/15.
 */
public class App extends Application {

    public static Database db;
    public static SharedPreferences shared;
    public static SharedPreferences.Editor editor;
    public static  int screenWidth;
    public static  int screenHeight;

    @Override
    public void onCreate() {
        super.onCreate();
        shared=getSharedPreferences("voicememes", MODE_PRIVATE);
        editor=shared.edit();
        db=new Database(getApplicationContext());

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
    }
}
package com.example.batterymetter;

import android.graphics.drawable.Drawable;

public class AppIcons {

    CharSequence appName;
    Drawable icon;
    String packageName;



    public AppIcons(CharSequence appName, Drawable icon, String packageName) {
        this.appName = appName;
        this.icon = icon;
        this.packageName = packageName;
    }

    public CharSequence getName() {
        return appName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getPackageName() {
        return packageName;
    }

}

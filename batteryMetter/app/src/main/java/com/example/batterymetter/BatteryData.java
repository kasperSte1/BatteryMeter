package com.example.batterymetter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BatteryData implements Serializable {
    private String appName;
    private String appPackage;
    private List<Float> values;
    private long timestamp;
    private String phoneModel;
    private String  androidVersion;

    // Pusty konstruktor potrzebny do deserializacji przez Firestore
    public BatteryData() {}

    public BatteryData(String appName, String appPackage, List<Float> values, long timestamp, String phoneModel, String androidVersion) {
        this.appName = appName;
        this.appPackage = appPackage;
        this.values = values;
        this.timestamp = timestamp;
        this.androidVersion = androidVersion;
        this.phoneModel = phoneModel;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public List<Float> getValues() {
        return values;
    }

    public void setValues(List<Float> values) {
        this.values = values;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public String getPhoneModel() {
        return phoneModel;
    }

    public String getAndroidVersion() { return androidVersion; }

    public String getFormattedDate() {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(date);
    }
}

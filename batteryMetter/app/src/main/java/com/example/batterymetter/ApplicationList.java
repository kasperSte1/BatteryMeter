package com.example.batterymetter;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ApplicationList extends AppCompatActivity {
    private ListView appsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_list);

        appsListView = findViewById(R.id.appsListView);
        List<AppIcons> appNames = getInstalledAppNames();
        AppAdapter adapter = new AppAdapter(this,appNames);
        appsListView.setAdapter(adapter);

// przekazywanie nazwy aplikacji do batteryActivity
        appsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppIcons selectedApp = (AppIcons) parent.getItemAtPosition(position);
                Intent intent = new Intent(ApplicationList.this,BatteryActivity.class);
                intent.putExtra("APP_NAME",selectedApp.getName().toString());
                intent.putExtra("APP_PACKAGE",selectedApp.getPackageName());
                startActivity(intent);
            }
        });
    }
//LISTUJE JEDYNIE NASZĄ APLIKACJĘ
//    private List<String> getInstalledAppNames() {
//        List<String> appNames = new ArrayList<>();
//        PackageManager packageManager = getPackageManager();
//        List<ApplicationInfo> apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
//
//        for (ApplicationInfo app : apps) {
//
//            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 0 || (app.packageName.contains("youtube"))) {
//                String appName = (String) packageManager.getApplicationLabel(app);
//                appNames.add(appName);
//            }
//        }
//        return appNames;
//    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ApplicationList.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        super.onBackPressed();
    }


    private List<AppIcons> getInstalledAppNames() {
        List<AppIcons> apps = new ArrayList<>();
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo app : packages) {
            apps.add(new AppIcons(
                    pm.getApplicationLabel(app),
                    pm.getApplicationIcon(app),
                    app.packageName
            ));
        }
        return apps;
    }


}

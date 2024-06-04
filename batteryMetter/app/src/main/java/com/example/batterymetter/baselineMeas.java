package com.example.batterymetter;

import static java.lang.Math.abs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class baselineMeas extends AppCompatActivity {
    public Button goback, baselinebtn;
    public TextView Meas;
    public float MeasVal;
    public Runnable baselinerunnable;
    private Handler handler;
    public float returnval;
    SharedPreferences sp;
    public float measValue = 0;
    private ProgressBar progressBar;


    private float checkBatteryLevel(BatteryManager batteryManager) {
        if (batteryManager != null) {
            float batteryLevelFloat = (float) batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW) / 1000;
            batteryLevelFloat = abs(batteryLevelFloat);
            return batteryLevelFloat;
        }
        return 0;
    }

    private void startMeasurement(BatteryManager batteryManager) {
        List<Float> tab = new ArrayList<Float>();
        List<Float> tab2 = new ArrayList<Float>();
        returnval = 0;

        progressBar.setProgress(0);



            baselinerunnable = new Runnable() {
                @Override
                public void run() {

                    tab.add(checkBatteryLevel(batteryManager));
                    progressBar.setProgress(tab.size() + tab2.size() * 20);
                    if (tab.size() == 20) {
                        for (int i = 0; i < 20; i++) {
                            measValue += tab.get(i);
                        }
                        measValue = measValue / 20;
                        measValue = measValue/3600;
                        tab.clear();
                        tab2.add(measValue);
                        measValue = 0;

                    }
                    if (tab2.size() == 10){
                        for (int i = 0; i < 10; i++) {
                            returnval += tab2.get(i);

                        }
                        returnval = returnval / 10;
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putFloat("Baseline",returnval);
                        editor.apply();
                        Meas.setText("Wynik pomiaru to: " + returnval);
                        handler.removeCallbacks(baselinerunnable);
                    }

                    handler.postDelayed(this, 50);
                }
            };
            handler.post(baselinerunnable);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        handler = new Handler();
        BatteryManager batteryManager = (BatteryManager) getSystemService(Context.BATTERY_SERVICE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baseline_meas);
        goback = findViewById(R.id.Goback);
        baselinebtn = findViewById(R.id.PomiarBase);
        Meas = findViewById(R.id.Wynik);
        progressBar = findViewById(R.id.progressbar);

        sp = getSharedPreferences("BaselineMeas",Context.MODE_PRIVATE);
        goback.setOnClickListener((View v) -> {
                Intent intent = new Intent(baselineMeas.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

        });
        baselinebtn.setOnClickListener(v -> {
            startMeasurement(batteryManager);
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(baselineMeas.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        super.onBackPressed();
    }

}
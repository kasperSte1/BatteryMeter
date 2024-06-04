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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.List;

public class BatteryActivity extends AppCompatActivity {

    public float meas_val;
    public EditText editText;
    boolean alreadySaved = false;

    public float baselinemeas;
    public int measfreq;

    public int increment_val;

    private List<Float> measurementValues = new ArrayList<>();
    private ChartManager chartManager;
    private Handler handler;
    private Runnable batteryRunnable;

    private Button startButton,Reset,Save;
    //private List<Entry> entries = new ArrayList<>();
    private boolean isMeasuring = false;
    MeasReciever measReciever;
    private FirebaseUser user;
    private FirebaseFirestore db;

    private String appName;
    private String appPackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batterybeta);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("BaselineMeas", Context.MODE_PRIVATE);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        increment_val = 0;
        baselinemeas = sp.getFloat("Baseline", 0.0f);
        measReciever = new MeasReciever();
        IntentFilter filter = new IntentFilter();
        filter.addAction("Measure");
        filter.addAction("Stop");
        registerReceiver(measReciever, filter, Context.RECEIVER_EXPORTED);

        // Odebranie nazwy oraz ikony aplikacji
        appName = getIntent().getStringExtra("APP_NAME");
        appPackage = getIntent().getStringExtra("APP_PACKAGE");

        PackageManager pm = getPackageManager();
        Drawable appIcon = null;
        try {
            appIcon = pm.getApplicationIcon(appPackage);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        TextView appNameTextView = findViewById(R.id.AppTextView);
        appNameTextView.setText(appName);

        if (appIcon != null) {
            Drawable scaledIcon = scaleDrawable(appIcon, 75, 75);
            appNameTextView.setCompoundDrawablesWithIntrinsicBounds(scaledIcon, null, null, null);
            appNameTextView.setCompoundDrawablePadding(10); // Odstęp między ikoną a tekstem
        }

        BarChart chart = findViewById(R.id.mychart);
        chartManager = new ChartManager(chart);
        editText = findViewById(R.id.freq);
        Save = findViewById(R.id.SaveBtn);
        Reset = findViewById(R.id.ResetBtn);
        startButton = findViewById(R.id.StartButton);
        //resetButton = findViewById(R.id.resetButton);

        handler = new Handler();

        // Listener dla przycisku start
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                measfreq = Integer.parseInt(editText.getText().toString());
                if(measfreq < 1){
                    Toast.makeText(BatteryActivity.this, "Ustaw poprawną wartość częstotliwości pomiaru", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent startIntent = new Intent(BatteryActivity.this, MeasurementService.class);
                startIntent.setAction("Start");
                startService(startIntent);
            }
        });
        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chartManager.clearChart();
                measurementValues.clear();
                Toast.makeText(BatteryActivity.this, "Wykres został wyczyszczony", Toast.LENGTH_SHORT).show();

            }
        });
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alreadySaved == false) {
                    saveMeasurement();
                    alreadySaved = true;
                }
                else
                {
                    Toast.makeText(BatteryActivity.this, "Pomiar został już zapisany", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void toggleMeasurement() {
        if (!isMeasuring) {
            startMeasurement();
        } else {
            stopMeasurement();
        }
    }

    private void startMeasurement() {

        BatteryManager batteryManager = (BatteryManager) getSystemService(Context.BATTERY_SERVICE);
        List<Float> tab = new ArrayList<>();
        alreadySaved = false;

        if (!isMeasuring) {
            isMeasuring = true;

            chartManager.clearChart();
            batteryRunnable = new Runnable() {
                @Override
                public void run() {
                    tab.add(checkBatteryLevel(batteryManager));
                    if (tab.size() == measfreq * 10) {
                        for (int i = 0; i < measfreq * 10; i++) {
                            meas_val += tab.get(i);
                        }
                        meas_val = meas_val / measfreq * 10;
                        increment_val++;
                        meas_val = meas_val / 3600;
                        meas_val = meas_val - baselinemeas;
                        if (meas_val < 0) {
                            meas_val = 0;
                        }
                        measurementValues.add(meas_val);

                        tab.clear();
                        meas_val = 0;
                    }

                    handler.postDelayed(this, 100);
                    chartManager.setEntries(measurementValues);
                }
            };
            handler.post(batteryRunnable);
        }
    }

    private void saveMeasurement() {
        if (user != null) {
            long timestamp = System.currentTimeMillis();
            String phoneModel = Build.MODEL;
            String androidVersion = String.valueOf(Build.VERSION.SDK_INT);
            BatteryData data = new BatteryData(appName, appPackage, measurementValues, timestamp, phoneModel, androidVersion);

            db.collection("users")
                    .document(user.getUid())
                    .collection("measurements")
                    .add(data)
                    .addOnSuccessListener(documentReference -> Toast.makeText(BatteryActivity.this, "Pomiary zapisane", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(BatteryActivity.this, "Błąd zapisu pomiarów: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void stopMeasurement() {
        if (isMeasuring) {
            handler.removeCallbacks(batteryRunnable);
            isMeasuring = false;


        }
    }

    private float checkBatteryLevel(BatteryManager batteryManager) {
        if (batteryManager != null) {
            float batteryLevelFloat = (float) batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW) / 1000;
            batteryLevelFloat = abs(batteryLevelFloat);
            return batteryLevelFloat;
        }
        return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (chartManager != null) {
            chartManager.clearChart();
        }
        if (isMeasuring) {
            handler.removeCallbacks(batteryRunnable);
        }
    }

    private class MeasReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("Measure".equals(action)) {
                Toast.makeText(context, "Pomiar rozpoczęty", Toast.LENGTH_SHORT).show();
                toggleMeasurement();
            } else if ("Stop".equals(action)) {
                Toast.makeText(context, "Pomiar zakończony", Toast.LENGTH_SHORT).show();
                stopMeasurement();
            }
        }
    }

    private Drawable scaleDrawable(Drawable drawable, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return new BitmapDrawable(getResources(), bitmap);
    }
}

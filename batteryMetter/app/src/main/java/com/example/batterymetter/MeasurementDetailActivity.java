package com.example.batterymetter;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;

import java.util.List;

public class MeasurementDetailActivity extends AppCompatActivity {

    private BarChart chart;
    private ChartManager chartManager;
    private TextView appNameTextView, dateTextView;
    private String appName, phoneModel, androidVersion;
    private long timestamp;
    private List<Float> values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_graph);

        chart = findViewById(R.id.chart);
        appNameTextView = findViewById(R.id.AppTextView);
        dateTextView = findViewById(R.id.DateTextView);

        appName = getIntent().getStringExtra("appName");
        timestamp = getIntent().getLongExtra("timestamp", 0);
        values = (List<Float>) getIntent().getSerializableExtra("values");
        phoneModel = getIntent().getStringExtra("phoneModel");
        androidVersion = getIntent().getStringExtra("androidVersion"); //Ustawić info w layoucie

        appNameTextView.setText(appName);
        dateTextView.setText(getTime(timestamp));

        chartManager = new ChartManager(chart);
        chartManager.clearChart();
        chartManager.setEntries(values);
    }
    private String getTime(long timestamp) {
        java.text.DateFormat dateFormat = java.text.DateFormat.getDateTimeInstance();
        return dateFormat.format(timestamp);
    }
}

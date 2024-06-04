package com.example.batterymetter;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class ChartManager {
    private BarChart chart;
    private List<BarEntry> entries;

    public ChartManager(BarChart chart) {
        this.chart = chart;
        this.entries = new ArrayList<>();
        initializeChart();
    }

    private void initializeChart() {

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f);
        xAxis.setDrawLabels(true);
        xAxis.setTextColor(Color.WHITE); // Ustawienie koloru etykiet osi X na biały

        LimitLine xTitle = new LimitLine(0f, "Wartości pomiarów (mAh):");
        xTitle.setLineColor(Color.TRANSPARENT);
        xTitle.setTextColor(Color.WHITE);
        xTitle.setTextSize(12f);
        xAxis.addLimitLine(xTitle);

        xAxis.setTextColor(Color.WHITE);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularity(1f);
        leftAxis.setTextColor(Color.WHITE);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        LimitLine yTitle = new LimitLine(leftAxis.getAxisMaximum(), "Czas (s):");
        yTitle.setLineColor(Color.TRANSPARENT);
        yTitle.setTextColor(Color.WHITE);
        yTitle.setTextSize(12f);
        yTitle.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        leftAxis.addLimitLine(yTitle);

        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setPinchZoom(true);
        chart.setScaleEnabled(true);

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(0f);

        BarData barData = new BarData(dataSet);
        chart.setData(barData);

        Legend legend = chart.getLegend();
        legend.setEnabled(true);

        LegendEntry legendEntry = new LegendEntry();
        legendEntry.label = "czas (s)";
        legendEntry.formColor = Color.TRANSPARENT;
        legendEntry.form = Legend.LegendForm.NONE;
        legendEntry.formSize = 0f;

        legend.setCustom(new LegendEntry[]{legendEntry});
        legend.setTextColor(Color.WHITE);
        legend.setTextSize(12f);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);

        chart.invalidate();
    }

    public void setEntries(List<Float> values) {
        entries.clear();
        for (int i = 0; i < values.size(); i++) {
            entries.add(new BarEntry(i, values.get(i)));
        }
        updateChart();
    }

    private void updateChart() {
        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(10f);

        BarData barData = new BarData(dataSet);
        chart.setData(barData);
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    public void clearChart() {
        entries.clear();
        updateChart();
    }
}
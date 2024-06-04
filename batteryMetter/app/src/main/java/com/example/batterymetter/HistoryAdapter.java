package com.example.batterymetter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.List;

public class HistoryAdapter extends ArrayAdapter<BatteryData> {
    public HistoryAdapter(Context context, List<BatteryData> measurements) {
        super(context, 0, measurements);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_history, parent, false);
        }

        TextView appNameTextView = convertView.findViewById(R.id.item_app_label);
        TextView appDateTextView = convertView.findViewById(R.id.item_app_date);
        ImageView appIconView = convertView.findViewById(R.id.item_app_icon);

        BatteryData data = getItem(position);
        if (data != null) {
            appNameTextView.setText(data.getAppName());
            appDateTextView.setText(data.getFormattedDate());
            Drawable appIcon = getAppIcon(data.getAppPackage());
            if (appIcon != null) {
                appIconView.setImageDrawable(appIcon);
            } else {
                appIconView.setImageDrawable(null);
            }
        }

        return convertView;
    }

    private Drawable getAppIcon(String appPackage) {
        try {
            return getContext().getPackageManager().getApplicationIcon(appPackage);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}

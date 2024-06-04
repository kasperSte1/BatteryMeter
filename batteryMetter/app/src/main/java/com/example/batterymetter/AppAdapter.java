package com.example.batterymetter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

public class AppAdapter extends ArrayAdapter<AppIcons> {
    public AppAdapter(Context context, List<AppIcons> apps) {
        super(context,0, apps);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        ImageView appIcon = convertView.findViewById(R.id.item_app_icon);
        TextView appName = convertView.findViewById(R.id.item_app_label);

        AppIcons app = getItem(position);
        appIcon.setImageDrawable(app.getIcon());
        appName.setText(app.getName());

        return convertView;
    }
}

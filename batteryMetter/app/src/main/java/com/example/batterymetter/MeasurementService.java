package com.example.batterymetter;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class MeasurementService extends Service {

    private static final int NOTIFICATION_ID = 123;
    private static final String CHANNEL_ID = "Channel1";
    private static final String ACTION_STOP = "stopMeasurement";
    private static final String ACTION_TOGGLE = "ToggleMeasurement";
    private boolean isMeasuring = false;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals("ToggleMeasurement")) {
                ToggleMeasurement();
            } else if (intent.getAction().equals("stopMeasurement")) {
                stopMeasurement();
            }
            else if (intent.getAction().equals("Start")){
                StartForeground();
            }
        }
        return START_NOT_STICKY;
    }

    private void StartForeground(){
        startForeground(NOTIFICATION_ID,createNotification());
    }

    private void ToggleMeasurement() {
        if(!isMeasuring)
        {
            isMeasuring = true;
        }
        else
        {
            isMeasuring = false;
        }
        Log.d("dziaua","TUTAJ GIT");
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("Measure");
        sendBroadcast(broadcastIntent);
    }

    private void stopMeasurement() {
        isMeasuring = false;
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("Stop");
        sendBroadcast(broadcastIntent);
        stopForeground(true);
        stopSelf();
    }

    private Notification createNotification() {
        // Tworzenie powiadomienia
        Intent stopIntent = new Intent(this, MeasurementService.class);
        Intent toggleIntent = new Intent(this, MeasurementService.class);
        stopIntent.setAction(ACTION_STOP);
        toggleIntent.setAction(ACTION_TOGGLE);
        PendingIntent pendingStopIntent = PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE);
        PendingIntent pendingToggleIntent = PendingIntent.getService(this, 0, toggleIntent, PendingIntent.FLAG_IMMUTABLE);
        Log.d("dziaua","TUTAJ GITES");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Pomiar w toku")
                .setContentText("Dotknij, aby zatrzymać pomiar")
                .setSmallIcon(R.mipmap.ic_launcher)
                .addAction(R.drawable.ic_stop, "Zatrzymaj Pomiar", pendingStopIntent)
                .addAction(R.drawable.ic_stop, "Rozpocznij Pomiar", pendingToggleIntent)
                .setAutoCancel(false);

        return builder.build();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

package com.example.homeautomation;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import com.example.homeautomation.MainActivity;


public class MyForeGroundService extends Service {

    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";

    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";

    public static String CHANNEL_1_ID = "Persistent notification";

    int phone_auto_notification_id;

    public MyForeGroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null)
        {
            String action = intent.getAction();

            switch (action)
            {
                case ACTION_START_FOREGROUND_SERVICE:
                    startForegroundService();
                    break;
                case ACTION_STOP_FOREGROUND_SERVICE:
                    stopForegroundService();
                    Toast.makeText(getApplicationContext(), "Foreground service is stopped.", Toast.LENGTH_LONG).show();
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /* Used to build and start foreground service. */
    private void startForegroundService() {

        // Create notification default intent.
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationChannel automatic_charging = new NotificationChannel(
                CHANNEL_1_ID,
                "Persistent notification",
                NotificationManager.IMPORTANCE_MIN
        );
        automatic_charging.setDescription("This notification keeps the app active");
        NotificationManager manager = getSystemService(NotificationManager.class);
        assert manager != null;
        manager.createNotificationChannel(automatic_charging);

        // Create notification builder.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setContentTitle("App is running")
                .setSmallIcon(R.drawable.ic_apartment)
                .setPriority(0);

        // Add Close button intent in notification.
        Intent pauseIntent = new Intent(this, MyForeGroundService.class);
        pauseIntent.setAction(ACTION_STOP_FOREGROUND_SERVICE);
        PendingIntent pendingPrevIntent = PendingIntent.getService(this, 0, pauseIntent, 0);
        NotificationCompat.Action prevAction = new NotificationCompat.Action(R.drawable.ic_close, "Close App", pendingPrevIntent);
        builder.addAction(prevAction);

        // Build the notification.
        phone_auto_notification_id = (int) System.currentTimeMillis();
        NotificationManager mnotificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        assert mnotificationmanager != null;

        // Start foreground service.
        startForeground(phone_auto_notification_id, builder.build());
    }

    private void stopForegroundService() {
        // Stop foreground service and remove the notification.
        stopForeground(true);

        // Stop the foreground service.
        stopSelf();
    }
}
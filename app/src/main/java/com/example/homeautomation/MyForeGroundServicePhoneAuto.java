package com.example.homeautomation;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
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


public class MyForeGroundServicePhoneAuto extends Service {

    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";

    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";

    public static String CHANNEL_1_ID = "Phone's automatic charging";

    int phone_auto_notification_id;

    Long auto_start, auto_end;

    boolean isAutoON = false, isStartReceived = false, isEndReceived = false;

    public MyForeGroundServicePhoneAuto() {
        final DatabaseReference phone_auto_firebase = FirebaseDatabase.getInstance().getReference().child("NodeMCU").child("phone_auto");
        phone_auto_firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                assert value != null;
                if (value.equals("ON")){
                    isAutoON = true;
                    if(isStartReceived && isEndReceived){
                        phone_autocharge();
                    }
                }
                else{
                    isAutoON = false;
                    stopForegroundService();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        final DatabaseReference start = FirebaseDatabase.getInstance().getReference().child("NodeMCU").child("auto_phone_begin");
        start.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long value = dataSnapshot.getValue(Long.class);
                assert value != null;
                auto_start = value;
                isStartReceived = true;
                if(isEndReceived && isAutoON){
                    phone_autocharge();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Failed to read extra's switch",Toast.LENGTH_SHORT).show();
            }
        });

        final DatabaseReference end = FirebaseDatabase.getInstance().getReference().child("NodeMCU").child("auto_phone_end");
        end.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long value = dataSnapshot.getValue(Long.class);
                assert value != null;
                auto_end = value;
                isEndReceived = true;
                if(isStartReceived && isAutoON){
                    phone_autocharge();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Failed to read extra's switch",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void phone_autocharge(){
        final DatabaseReference phone_switch_firebase = FirebaseDatabase.getInstance().getReference().child("NodeMCU").child("switch_status_phone");
        BatteryManager batterymanager = (BatteryManager)getSystemService(BATTERY_SERVICE);
        assert batterymanager != null;
        final int battery_percentage = batterymanager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        if(battery_percentage < auto_end) phone_switch_firebase.setValue("ON");
        else phone_switch_firebase.setValue("OFF");
        final CountDownTimer phone_auto_timer = new CountDownTimer(604800000, 10000) {
            public void onTick(long millisUntilFinished) {
                if(isAutoON){
                    BatteryManager batterymanager = (BatteryManager)getSystemService(BATTERY_SERVICE);
                    assert batterymanager != null;
                    final int battery_percentage = batterymanager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                    if(battery_percentage <= auto_start){
                        phone_switch_firebase.setValue("ON");
                    }

                    else if(battery_percentage >= auto_end){
                        phone_switch_firebase.setValue("OFF");
                    }
                }
                else{
                    stopForegroundService();
                    cancel();
                }
            }
            public void onFinish() {
                stopForegroundService();
            }
        };
        phone_auto_timer.start();
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
                "Phone's automatic charging",
                NotificationManager.IMPORTANCE_MIN
        );
        automatic_charging.setDescription("Persistent notification for Phone's automatic charging");
        NotificationManager manager = getSystemService(NotificationManager.class);
        assert manager != null;
        manager.createNotificationChannel(automatic_charging);

        // Create notification builder.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setContentText("Automatic charging enabled")
                .setSmallIcon(R.drawable.ic_launcher_grayscale)
                .setPriority(0);

        // Add Close button intent in notification.
        Intent pauseIntent = new Intent(this, MyForeGroundServicePhoneAuto.class);
        pauseIntent.setAction(ACTION_STOP_FOREGROUND_SERVICE);
        PendingIntent pendingPrevIntent = PendingIntent.getService(this, 0, pauseIntent, 0);
        NotificationCompat.Action prevAction = new NotificationCompat.Action(R.drawable.ic_close, "Turn off automatic charging", pendingPrevIntent);
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
        final DatabaseReference phone_auto_firebase = FirebaseDatabase.getInstance().getReference().child("NodeMCU").child("phone_auto");
        phone_auto_firebase.setValue("OFF");
        stopSelf();
    }
}
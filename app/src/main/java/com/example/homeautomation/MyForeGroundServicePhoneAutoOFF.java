package com.example.homeautomation;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyForeGroundServicePhoneAutoOFF extends Service {

    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";

    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";

    public static String CHANNEL_1_ID = "Timer";

    int phone_auto_OFF_notification_id;

    boolean isTimeRead = false;
    int time_delay_read, time_of_execution_hour, time_of_execution_min;
    String output_time_of_execution_hour, output_time_of_execution_min;

    public MyForeGroundServicePhoneAutoOFF(){
        final CountDownTimer wait_state = new CountDownTimer(3600000, 1000) {
            public void onTick(long millisUntilFinished) {
                if (isTimeRead){
                    onFinish();
                    cancel();
                }
            }
            public void onFinish() {
                final CountDownTimer timer = new CountDownTimer(time_delay_read*1000, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }
                    public void onFinish() {
                        final DatabaseReference phone_auto_firebase = FirebaseDatabase.getInstance().getReference().child("NodeMCU").child("phone_auto");
                        phone_auto_firebase.setValue("OFF");
                        stopForegroundService();
                    }
                };
                timer.start();
            }
        };
        wait_state.start();
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
                    isTimeRead = true;
                    time_delay_read = intent.getIntExtra("time_delay", 0);
                    String currentTimeHour = new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());
                    String currentTimeMin = new SimpleDateFormat("mm", Locale.getDefault()).format(new Date());
                    time_of_execution_hour = Integer.parseInt(currentTimeHour);
                    time_of_execution_min = Integer.parseInt(currentTimeMin) + (time_delay_read/60);
                    while (time_of_execution_min >= 60){
                        time_of_execution_hour += 1;
                        time_of_execution_min -= 60;
                    }
                    while (time_of_execution_hour >= 24){
                        time_of_execution_hour -= 24;
                    }
                    if (time_of_execution_min < 10){
                        output_time_of_execution_min = "0" + time_of_execution_min;
                    }
                    else output_time_of_execution_min = String.valueOf(time_of_execution_min);
                    if (time_of_execution_hour < 10){
                        output_time_of_execution_hour = "0" + time_of_execution_hour;
                    }
                    else output_time_of_execution_hour = String.valueOf(time_of_execution_hour);
                    startForegroundService();
                    break;
                case ACTION_STOP_FOREGROUND_SERVICE:
                    stopForegroundService();
                    Toast.makeText(getApplicationContext(), "Foreground service is stopped.", Toast.LENGTH_LONG).show();
                    break;
            }


//            time_delay_read = intent.getStringExtra("time_delay_read");
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
                "Timer",
                NotificationManager.IMPORTANCE_MIN
        );
        automatic_charging.setDescription("Persistant notification for timers");
        NotificationManager manager = getSystemService(NotificationManager.class);
        assert manager != null;
        manager.createNotificationChannel(automatic_charging);

        // Create notification builder.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setContentTitle("Phone's automatic charging will turn OFF at " + output_time_of_execution_hour + ":" + output_time_of_execution_min)
                .setSmallIcon(R.drawable.ic_launcher_grayscale)
                .setPriority(0);

        // Add Close button intent in notification.
        Intent pauseIntent = new Intent(this, MyForeGroundServicePhoneAutoON.class);
        pauseIntent.setAction(ACTION_STOP_FOREGROUND_SERVICE);
        PendingIntent pendingPrevIntent = PendingIntent.getService(this, 0, pauseIntent, 0);
        NotificationCompat.Action prevAction = new NotificationCompat.Action(R.drawable.ic_close, "Cancel timer", pendingPrevIntent);
        builder.addAction(prevAction);

        // Build the notification.
        phone_auto_OFF_notification_id = (int) System.currentTimeMillis();
        NotificationManager mnotificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        assert mnotificationmanager != null;

        // Start foreground service.
        startForeground(phone_auto_OFF_notification_id, builder.build());
    }

    private void stopForegroundService() {
        // Stop foreground service and remove the notification.
        stopForeground(true);

        // Stop the foreground service.
        stopSelf();
    }
}
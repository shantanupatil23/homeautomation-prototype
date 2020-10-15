package com.example.homeautomation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

public class timer extends AppCompatActivity {

    int calc_hour, calc_min, calc_sec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        Toolbar my_toolbar = (Toolbar) findViewById(R.id.action_bar);
        my_toolbar.setTitle("");
        my_toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_menu));
        setSupportActionBar(my_toolbar);

        ImageButton phone_auto_on = findViewById(R.id.phone_auto);
        phone_auto_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(timer.this, MyForeGroundServicePhoneAutoON.class);
                intent.setAction(MyForeGroundServicePhoneAutoON.ACTION_START_FOREGROUND_SERVICE);
                intent.putExtra("time_delay", find_delay());
                startService(intent);
                startActivity(new Intent(timer.this, MainActivity.class));
            }
        });

        ImageButton phone_auto_off = findViewById(R.id.phone_auto_off);
        phone_auto_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(timer.this, MyForeGroundServicePhoneAutoOFF.class);
                intent.setAction(MyForeGroundServicePhoneAutoOFF.ACTION_START_FOREGROUND_SERVICE);
                intent.putExtra("time_delay", find_delay());
                startService(intent);
                startActivity(new Intent(timer.this, MainActivity.class));
            }
        });

        Button phone_on = findViewById(R.id.phone_on);
        phone_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(timer.this, MyForeGroundServicePhoneON.class);
                intent.setAction(MyForeGroundServicePhoneON.ACTION_START_FOREGROUND_SERVICE);
                intent.putExtra("time_delay", find_delay());
                startService(intent);
                startActivity(new Intent(timer.this, MainActivity.class));
            }
        });

        Button phone_off = findViewById(R.id.phone_off);
        phone_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(timer.this, MyForeGroundServicePhoneOFF.class);
                intent.setAction(MyForeGroundServicePhoneOFF.ACTION_START_FOREGROUND_SERVICE);
                intent.putExtra("time_delay", find_delay());
                startService(intent);
                startActivity(new Intent(timer.this, MainActivity.class));
            }
        });

        Button laptop_on = findViewById(R.id.laptop_on);
        laptop_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(timer.this, MyForeGroundServiceLaptopON.class);
                intent.setAction(MyForeGroundServiceLaptopON.ACTION_START_FOREGROUND_SERVICE);
                intent.putExtra("time_delay", find_delay());
                startService(intent);
                startActivity(new Intent(timer.this, MainActivity.class));
            }
        });

        Button laptop_off = findViewById(R.id.laptop_off);
        laptop_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(timer.this, MyForeGroundServiceLaptopOFF.class);
                intent.setAction(MyForeGroundServiceLaptopOFF.ACTION_START_FOREGROUND_SERVICE);
                intent.putExtra("time_delay", find_delay());
                startService(intent);
                startActivity(new Intent(timer.this, MainActivity.class));
            }
        });

        Button extra_on = findViewById(R.id.extra_on);
        extra_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(timer.this, MyForeGroundServiceExtraON.class);
                intent.setAction(MyForeGroundServiceExtraON.ACTION_START_FOREGROUND_SERVICE);
                intent.putExtra("time_delay", find_delay());
                startService(intent);
                startActivity(new Intent(timer.this, MainActivity.class));
            }
        });

        Button extra_off = findViewById(R.id.extra_off);
        extra_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(timer.this, MyForeGroundServiceExtraOFF.class);
                intent.setAction(MyForeGroundServiceExtraOFF.ACTION_START_FOREGROUND_SERVICE);
                intent.putExtra("time_delay", find_delay());
                startService(intent);
                startActivity(new Intent(timer.this, MainActivity.class));
            }
        });


        Button cancel = findViewById(R.id.cancel_timer_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(timer.this, MainActivity.class));
            }
        });


    }

    public int find_delay(){
        EditText hour = findViewById(R.id.hour_edit_text);
        String str_hour = hour.getText().toString();
        if (str_hour.equals("")) calc_hour = 0;
        else calc_hour = Integer.parseInt(str_hour);
        EditText min = findViewById(R.id.minute_edit_text);
        String str_min = min.getText().toString();
        if (str_min.equals("")) calc_min = 0;
        else calc_min = Integer.parseInt(str_min);
        EditText sec = findViewById(R.id.second_edit_text);
        String str_sec = sec.getText().toString();
        if (str_sec.equals("")) calc_sec = 0;
        else calc_sec = Integer.parseInt(str_sec);
        return  (calc_hour*60*60) + (calc_min*60) + calc_sec;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}

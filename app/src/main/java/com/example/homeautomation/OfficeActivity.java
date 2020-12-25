package com.example.homeautomation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.os.CountDownTimer;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OfficeActivity extends AppCompatActivity {

    int loading = 0;
    Boolean isAutoON = false;
    View auto_bar;
    DatabaseReference phone_auto_firebase, phone_switch_firebase, fan_switch_firebase, small_light_switch_firebase, light_switch_firebase, laptop_switch_firebase, extra_switch_firebase, auto_start, auto_end, switch_phone_firebase, switch_laptop_firebase, switch_extra_firebase;
    EditText editText_auto_start, editText_auto_end, editText_socket_phone, editText_socket_laptop, editText_socket_extra;
    View popupView_auto, popupView_sockets;
    PopupWindow popupWindow_auto, popupWindow_sockets;
    LinearLayout layout_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office);
        layout_main = findViewById(R.id.activity_home);

        Toolbar my_toolbar = findViewById(R.id.action_bar);
        my_toolbar.setTitle("");
        my_toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_menu));
        setSupportActionBar(my_toolbar);

        CountDownTimer loading_timer = new CountDownTimer(7000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                TextView textView = findViewById(R.id.text_internet_hint);
                textView.setVisibility(View.VISIBLE);
            }
        };
        loading_timer.start();


        phone_auto_firebase = FirebaseDatabase.getInstance().getReference().child("NodeMCU").child("phone_auto");
        auto_bar = findViewById(R.id.auto_bar);
        phone_auto_firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                assert value != null;
                if (value.equals("ON")) {
                    Intent intent = new Intent(OfficeActivity.this, MyForeGroundServicePhoneAuto.class);
                    intent.setAction(MyForeGroundServicePhoneAuto.ACTION_START_FOREGROUND_SERVICE);
                    startService(intent);
                    isAutoON = true;
                    auto_bar.setBackgroundColor(Color.GREEN);
                } else {
                    isAutoON = false;
                    auto_bar.setBackgroundColor(Color.GRAY);
                }
                loading_func();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        ImageButton auto_image = findViewById(R.id.phone_auto);
        auto_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAutoON) {
                    phone_auto_firebase.setValue("OFF");
                } else {
                    phone_auto_firebase.setValue("ON");
                }
            }
        });

        fan_switch_firebase = FirebaseDatabase.getInstance().getReference().child("NodeMCU").child("switch_status_office_fan");
        final Switch fan_switch_view = findViewById(R.id.fan_switch);
        fan_switch_firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                assert value != null;
                if (value.equals("ON")) {
                    fan_switch_view.setChecked(true);
                } else if (value.equals("OFF")) {
                    fan_switch_view.setChecked(false);
                }
                loading_func();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to read fan's status", Toast.LENGTH_SHORT).show();
            }
        });

        fan_switch_view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fan_switch_firebase.setValue("ON");
                } else {
                    fan_switch_firebase.setValue("OFF");
                }
            }
        });

        small_light_switch_firebase = FirebaseDatabase.getInstance().getReference().child("NodeMCU").child("switch_status_office_small_light");
        final Switch small_light_switch_view = findViewById(R.id.small_light_switch);
        small_light_switch_firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                assert value != null;
                if (value.equals("ON")) {
                    small_light_switch_view.setChecked(true);
                } else if (value.equals("OFF")) {
                    small_light_switch_view.setChecked(false);
                }
                loading_func();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to read small_light's status", Toast.LENGTH_SHORT).show();
            }
        });

        small_light_switch_view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    small_light_switch_firebase.setValue("ON");
                } else {
                    small_light_switch_firebase.setValue("OFF");
                }
            }
        });

        light_switch_firebase = FirebaseDatabase.getInstance().getReference().child("NodeMCU").child("switch_status_office_light");
        final Switch light_switch_view = findViewById(R.id.light_switch);
        light_switch_firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                assert value != null;
                if (value.equals("ON")) {
                    light_switch_view.setChecked(true);
                } else if (value.equals("OFF")) {
                    light_switch_view.setChecked(false);
                }
                loading_func();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to read light's status", Toast.LENGTH_SHORT).show();
            }
        });

        light_switch_view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    light_switch_firebase.setValue("ON");
                } else {
                    light_switch_firebase.setValue("OFF");
                }
            }
        });

        phone_switch_firebase = FirebaseDatabase.getInstance().getReference().child("NodeMCU").child("switch_status_phone");
        final Switch phone_switch_view = findViewById(R.id.phone_switch);
        phone_switch_firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                assert value != null;
                if (value.equals("ON")) {
                    phone_switch_view.setChecked(true);
                } else if (value.equals("OFF")) {
                    phone_switch_view.setChecked(false);
                }
                loading_func();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to read phone's status", Toast.LENGTH_SHORT).show();
            }
        });

        phone_switch_view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    phone_switch_firebase.setValue("ON");
                } else {
                    phone_switch_firebase.setValue("OFF");
                }
            }
        });

        laptop_switch_firebase = FirebaseDatabase.getInstance().getReference().child("NodeMCU").child("switch_status_laptop");
        final Switch laptop_switch_view = findViewById(R.id.laptop_switch);
        laptop_switch_firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                assert value != null;
                if (value.equals("ON")) {
                    laptop_switch_view.setChecked(true);
                } else if (value.equals("OFF")) {
                    laptop_switch_view.setChecked(false);
                }
                loading_func();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to read laptop's status", Toast.LENGTH_SHORT).show();
            }
        });

        laptop_switch_view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    laptop_switch_firebase.setValue("ON");
                } else {
                    laptop_switch_firebase.setValue("OFF");
                }
            }
        });

        extra_switch_firebase = FirebaseDatabase.getInstance().getReference().child("NodeMCU").child("switch_status_extra");
        final Switch extra_switch_view = findViewById(R.id.extra_switch);
        extra_switch_firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                assert value != null;
                if (value.equals("ON")) {
                    extra_switch_view.setChecked(true);
                } else if (value.equals("OFF")) {
                    extra_switch_view.setChecked(false);
                }
                loading_func();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to read extra's status", Toast.LENGTH_SHORT).show();
            }
        });

        extra_switch_view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    extra_switch_firebase.setValue("ON");
                } else {
                    extra_switch_firebase.setValue("OFF");
                }
            }
        });

        Button timer = findViewById(R.id.timer_button);
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OfficeActivity.this, timer.class));
            }
        });
    }

    private void loading_func() {
        loading += 25;
        ProgressBar progressBarLoading = findViewById(R.id.loading_progress_bar);
        progressBarLoading.setProgress(loading);
        if (loading == 100) {
            RelativeLayout loading_layout = findViewById(R.id.loading_screen);
            loading_layout.setVisibility(View.GONE);
        }
    }


}

package com.example.homeautomation;

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
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_office, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.change_auto_details) {
            LayoutInflater auto_config_inflater = (LayoutInflater) OfficeActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            popupView_auto = auto_config_inflater.inflate(R.layout.popup_autocharge_config, null);
            popupWindow_auto = new PopupWindow(popupView_auto, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            popupWindow_auto.setElevation(100);
            popupWindow_auto.showAtLocation(layout_main, Gravity.CENTER, 0, 0);

            auto_start = FirebaseDatabase.getInstance().getReference().child("NodeMCU").child("auto_phone_begin");
            editText_auto_start = popupView_auto.findViewById(R.id.edittext_auto_start);
            auto_start.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Integer value = dataSnapshot.getValue(Integer.class);
                    assert value != null;
                    editText_auto_start.setHint(String.valueOf(value));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            auto_end = FirebaseDatabase.getInstance().getReference().child("NodeMCU").child("auto_phone_end");
            editText_auto_end = popupView_auto.findViewById(R.id.edittext_auto_end);
            auto_end.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Integer value = dataSnapshot.getValue(Integer.class);
                    assert value != null;
                    editText_auto_end.setHint(String.valueOf(value));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });


            Button save_button_auto = popupView_auto.findViewById(R.id.button_auto_save);
            save_button_auto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str_start = editText_auto_start.getText().toString(), str_end = editText_auto_end.getText().toString();
                    int auto_start_int, auto_end_int;
                    if (!str_start.equals("")) {
                        auto_start_int = Integer.parseInt(str_start);
                        auto_start.setValue(auto_start_int);
                    }
                    if (!str_end.equals("")) {
                        auto_end_int = Integer.parseInt(str_end);
                        auto_end.setValue(auto_end_int);
                    }
                    popupWindow_auto.dismiss();
                    Toast.makeText(getApplicationContext(), "Changes Saved", Toast.LENGTH_SHORT).show();
                }
            });

            Button cancel_button_auto = popupView_auto.findViewById(R.id.button_auto_cancel);
            cancel_button_auto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow_auto.dismiss();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }
}

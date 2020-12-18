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
    DatabaseReference phone_auto_firebase, phone_switch_firebase, laptop_switch_firebase, extra_switch_firebase, auto_start, auto_end, switch_phone_firebase, switch_laptop_firebase, switch_extra_firebase;
    EditText editText_auto_start, editText_auto_end, editText_socket_phone, editText_socket_laptop,editText_socket_extra;
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
        my_toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_menu));
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
                if (value.equals("ON")){
                    Intent intent = new Intent(OfficeActivity.this, MyForeGroundServicePhoneAuto.class);
                    intent.setAction(MyForeGroundServicePhoneAuto.ACTION_START_FOREGROUND_SERVICE);
                    startService(intent);
                    isAutoON = true;
                    auto_bar.setBackgroundColor(Color.GREEN);
                }
                else{
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
                if(isAutoON){
                    phone_auto_firebase.setValue("OFF");
                }
                else{
                    phone_auto_firebase.setValue("ON");
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
                if(value.equals("ON")){
                    phone_switch_view.setChecked(true);
                }
                else if(value.equals("OFF")){
                    phone_switch_view.setChecked(false);
                }
                loading_func();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Failed to read phone's status",Toast.LENGTH_SHORT).show();
            }
        });

        phone_switch_view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    phone_switch_firebase.setValue("ON");
                }
                else {
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
                if(value.equals("ON")){
                    laptop_switch_view.setChecked(true);
                }
                else if(value.equals("OFF")){
                    laptop_switch_view.setChecked(false);
                }
                loading_func();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Failed to read laptop's status",Toast.LENGTH_SHORT).show();
            }
        });

        laptop_switch_view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    laptop_switch_firebase.setValue("ON");
                }
                else {
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
                if(value.equals("ON")){
                    extra_switch_view.setChecked(true);
                }
                else if(value.equals("OFF")){
                    extra_switch_view.setChecked(false);
                }
                loading_func();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Failed to read extra's status",Toast.LENGTH_SHORT).show();
            }
        });

        extra_switch_view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    extra_switch_firebase.setValue("ON");
                }
                else {
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

    private void loading_func(){
        loading += 25;
        ProgressBar progressBarLoading = findViewById(R.id.loading_progress_bar);
        progressBarLoading.setProgress(loading);
        if (loading==100){
            RelativeLayout loading_layout = findViewById(R.id.loading_screen);
            loading_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.config_sockets:
                LayoutInflater config_sockets_inflater = (LayoutInflater) OfficeActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                popupView_sockets = config_sockets_inflater.inflate(R.layout.popup_switch_config,null);
                popupWindow_sockets = new PopupWindow(popupView_sockets, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
                popupWindow_sockets.setElevation(100);
                popupWindow_sockets.showAtLocation(layout_main, Gravity.CENTER, 0, 0);

                editText_socket_phone = popupView_sockets.findViewById(R.id.edittext_phone_socket);
                switch_phone_firebase = FirebaseDatabase.getInstance().getReference().child("NodeMCU").child("switch_id_phone");
                switch_phone_firebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Integer value = dataSnapshot.getValue(Integer.class);
                        assert value != null;
                        editText_socket_phone.setHint(String.valueOf(value));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });

                editText_socket_laptop = popupView_sockets.findViewById(R.id.edittext_laptop_socket);
                switch_laptop_firebase = FirebaseDatabase.getInstance().getReference().child("NodeMCU").child("switch_id_laptop");
                switch_laptop_firebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Integer value = dataSnapshot.getValue(Integer.class);
                        assert value != null;
                        editText_socket_laptop.setHint(String.valueOf(value));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });

                editText_socket_extra = popupView_sockets.findViewById(R.id.edittext_extra_socket);
                switch_extra_firebase = FirebaseDatabase.getInstance().getReference().child("NodeMCU").child("switch_id_extra");
                switch_extra_firebase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Integer value = dataSnapshot.getValue(Integer.class);
                        assert value != null;
                        editText_socket_extra.setHint(String.valueOf(value));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });


                Button save_button_switch = popupView_sockets.findViewById(R.id.button_switch_save);
                save_button_switch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String str_phone = editText_socket_phone.getText().toString(), str_laptop = editText_socket_laptop.getText().toString(), str_extra = editText_socket_extra.getText().toString();
                        int switch_phone, switch_laptop, switch_extra;
                        boolean isValid = true;
                        if (!str_phone.equals("")){
                            if (!str_phone.equals(str_laptop) && !str_phone.equals(str_extra)){
                                switch_phone = Integer.parseInt(str_phone);
                                switch_phone_firebase.setValue(switch_phone);
                            }
                            else{
                                isValid = false;
                                Toast.makeText(getApplicationContext(), "Two switches can not be same", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (!str_laptop.equals("")){
                            if (!str_laptop.equals(str_phone) && !str_laptop.equals(str_extra)){
                                switch_laptop = Integer.parseInt(str_laptop);
                                switch_laptop_firebase.setValue(switch_laptop);
                            }
                            else{
                                isValid = false;
                                Toast.makeText(getApplicationContext(), "Two switches can not be same", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (!str_extra.equals("")){
                            if (!str_extra.equals(str_phone) && !str_extra.equals(str_laptop)){
                                switch_extra = Integer.parseInt(str_extra);
                                switch_extra_firebase.setValue(switch_extra);
                            }
                            else{
                                isValid = false;
                                Toast.makeText(getApplicationContext(), "Two switches can not be same", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (isValid){
                            popupWindow_sockets.dismiss();
                            Toast.makeText(getApplicationContext(), "Changes Saved", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Button cancel_button_switch = popupView_sockets.findViewById(R.id.button_switch_cancel);
                cancel_button_switch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow_sockets.dismiss();
                    }
                });

                break;

            case R.id.change_auto_details:
                LayoutInflater auto_config_inflater = (LayoutInflater) OfficeActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                popupView_auto = auto_config_inflater.inflate(R.layout.popup_autocharge_config,null);
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
                    public void onCancelled(@NonNull DatabaseError error) {}
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
                    public void onCancelled(@NonNull DatabaseError error) {}
                });


                Button save_button_auto = popupView_auto.findViewById(R.id.button_auto_save);
                save_button_auto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String str_start = editText_auto_start.getText().toString(), str_end = editText_auto_end.getText().toString();
                        int auto_start_int, auto_end_int;
                        if (!str_start.equals("")){
                            auto_start_int = Integer.parseInt(str_start);
                            auto_start.setValue(auto_start_int);
                        }
                        if (!str_end.equals("")){
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
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

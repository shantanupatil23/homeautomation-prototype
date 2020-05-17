package com.example.homeautomation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.os.CountDownTimer;
import android.os.BatteryManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    int loading = 0;
    Boolean isAutoON = false;
    View auto_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar my_toolbar = findViewById(R.id.action_bar);
        my_toolbar.setTitle("");
        my_toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_menu));
        setSupportActionBar(my_toolbar);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        CountDownTimer loading_timer = new CountDownTimer(7000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                TextView textView = findViewById(R.id.text_internet_hint);
                ViewGroup.LayoutParams params = textView.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                textView.setLayoutParams(params);
            }
        };
        loading_timer.start();



        final DatabaseReference phone_auto_firebase = FirebaseDatabase.getInstance().getReference().child("NodeMCU").child("phone_auto");
        auto_bar = findViewById(R.id.auto_bar);
        phone_auto_firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                assert value != null;
                if (value.equals("ON")){
                    Intent intent = new Intent(MainActivity.this, MyForeGroundServicePhoneAuto.class);
                    intent.setAction(MyForeGroundService.ACTION_START_FOREGROUND_SERVICE);
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

        final DatabaseReference phone_switch_firebase = FirebaseDatabase.getInstance().getReference().child("NodeMCU").child("switch_status_phone");
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

        final DatabaseReference laptop_switch_firebase = FirebaseDatabase.getInstance().getReference().child("NodeMCU").child("switch_status_laptop");
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

        final DatabaseReference extra_switch_firebase = FirebaseDatabase.getInstance().getReference().child("NodeMCU").child("switch_status_extra");
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
                startActivity(new Intent(MainActivity.this, timer.class));
            }
        });
    }

    private void loading_func(){
        loading += 25;
        ProgressBar progressBarLoading = findViewById(R.id.loading_progress_bar);
        progressBarLoading.setProgress(loading);
        if (loading==100){
            RelativeLayout loading_layout = findViewById(R.id.loading_screen);
            loading_layout.getLayoutParams().height = 0;
            loading_layout.requestLayout();
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
        return super.onOptionsItemSelected(item);
    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    public void checkIP(){
//        EditText editText = (EditText) findViewById(R.id.ip);
//        if(editText.getText().toString().matches(""));
//        else url = Integer.parseInt(editText.getText().toString());
//        editText.setText("");
//    }
//
//    public int invert (int val){
//        if (val == 0) return 1;
//        else return 0;
//    }
//
//    public void refresh(View view){
//        checkIP();
//        closeKeyBoard();
//        WebView myWebView = (WebView) findViewById(R.id.webview);
//        myWebView.loadUrl( "http://192.168.0." + url + "/05");
//    }
//
//    public void evaluateTime(){
//        checkIP();
//        EditText editText = (EditText) findViewById(R.id.delay);
//        charValue = editText.getText().toString();
//        if(charValue.matches("")) charValue = "0";
//        originalTime = Integer.parseInt(charValue);
//        editText.setText("");
//    }
//
//    public void toast(){
//        Toast.makeText(getApplicationContext(),"Cancel or Wait for previous instruction",Toast.LENGTH_SHORT).show();
//    }
//
//    public void closeKeyBoard(){
//        View view = this.getCurrentFocus();
//        if( view != null){
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//    }
//
//    public void autoPhone(){
//        final CountDownTimer timer7;
//        ImageButton phone_auto_button = (ImageButton)findViewById(R.id.phone_auto);
//        phone_auto_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                autoStatus = 0;
//                statusForAuto1 = true;
//                BatteryManager bm = (BatteryManager)getSystemService(BATTERY_SERVICE);
//                int batLevel1 = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
//                if(batLevel1<=auto_end){
//                    WebView myWebView = (WebView) findViewById(R.id.webview);
//                    myWebView.loadUrl( "http://192.168.0." + url + "/0" + phone_switch);
//                    change1 = true;
//                    phoneStatus = 1;
//                    buttonClick.setBackgroundResource(R.drawable.phone_auto_green);
//                }
//                else{
//                    WebView myWebView = (WebView) findViewById(R.id.webview);
//                    myWebView.loadUrl( "http://192.168.0." + url + "/1" + phone_switch);
//                    change1 = false;
//                    phoneStatus = 0;
//                    buttonClick.setBackgroundResource(R.drawable.phone_auto_red);
//                }
//                closeKeyBoard();
//                timer7 = new CountDownTimer(604800000, 1000) {
//                    public void onTick(long millisUntilFinished) {
//                        System.out.println("Clock is Checking");
//                        if(autoStatus == 1){
//                            System.out.println("Clock is ticking");
//                            BatteryManager bm = (BatteryManager)getSystemService(BATTERY_SERVICE);
//                            int batLevel1 = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
//                            if(batLevel1<=auto1Beginning){
//                                if(change1 == false){
//                                    WebView myWebView = (WebView) findViewById(R.id.webview);
//                                    myWebView.loadUrl( "http://192.168.0." + url + "/0" + phone_switch);
//                                    change1 = true;
//                                    buttonClick.setBackgroundResource(R.drawable.phone_auto_green);
//                                }
//                            }
//                            else if(batLevel1>=auto_end){
//                                if(change1 == true){
//                                    WebView myWebView = (WebView) findViewById(R.id.webview);
//                                    myWebView.loadUrl( "http://192.168.0." + url + "/1" + phone_switch);
//                                    change1 = false;
//                                    System.out.println(change1);
//                                    buttonClick.setBackgroundResource(R.drawable.phone_auto_red);
//                                }
//                            }
//                        }
//                        else if(autoStatus == 2){
//                            autoStatus = 3;
//                            buttonClick.setBackgroundResource(R.drawable.phone_red);
//                            timer7 = null;
//                        }
//                    }
//                    public void onFinish() {
//                        Toast.makeText(getApplicationContext(),"Auto has ended for phone.",Toast.LENGTH_LONG).show();
//                        buttonClick.setBackgroundResource(R.drawable.phone_red);
//                    }
//                };
//                timer7.start();
//            }
//        });
//    }
//
//    public void phone (View view) {
//        closeKeyBoard();
//        phoneStatus = invert(phoneStatus);
//        autoStatus++;
//        if(autoStatus == 2 ) phoneStatus = 1;
//        if(autoStatus !=1 ) {
//            if (phoneStatus == 0){
//                if(i1count==0){
//                    i1count = 1;
//                    phoneOn();
//                }
//                else toast();
//            }
//            else if (phoneStatus == 1){
//                if(i2count==0){
//                    i2count = 1;
//                    phoneOff();
//                }
//                else toast();
//            }
//        }
//    }
//
//    public void phoneOn(){
//        evaluateTime();
//        i1 = true;
//        bt1 =0;
//        timer1 = new CountDownTimer(originalTime*60000, 500) {
//            public void onTick(long millisUntilFinished) {
//                long hour, min;
//                String h, m;
//                hour = millisUntilFinished/3600000;
//                if(hour<10) h = "0";
//                else h = "";
//                millisUntilFinished %= 3600000;
//                min = millisUntilFinished/60000;
//                if(min<10) m = "0";
//                else m = "";
//                TextView instruction1 = (TextView) findViewById(R.id.instruction1);
//                if(bt1 == 0) {
//                    b1 = ":";
//                }
//                else if(bt1 == 3){
//                    b1 = " ";
//                }
//                else if(bt1 == 4){
//                    bt1 = -1;
//                }
//                bt1++;
//                System.out.println(bt1);
//                instruction1.setText(h + hour + " " + b1 + " " + m + min);
//                if(!i1 || autoStatus==1) {
//                    instruction1.setText("");
//                    i1count = 0;
//                    cancel();
//                }
//            }
//            public void onFinish() {
//                WebView myWebView = (WebView) findViewById(R.id.webview);
//                myWebView.loadUrl( "http://192.168.0." + url + "/0" + phone_switch);
//                ImageButton buttonClick1 = (ImageButton)findViewById(R.id.phone);
//                buttonClick1.setBackgroundResource(R.drawable.phone_green);
//                TextView instruction1 = (TextView) findViewById(R.id.instruction1);
//                instruction1.setText("");
//                i1count = 0;
//            }
//        };
//        timer1.start();
//    }   //for phone on
//
//    public void phoneOnCancel(){
//        closeKeyBoard();
//        Button button1Click = (Button)findViewById(R.id.instruction1);
//        button1Click.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                i1 = false;
//                return false;
//            }
//        });
//    }
//
//    public void phoneOff(){
//        evaluateTime();
//        i2 = true;
//        bt2 = 0;
//        timer2 = new CountDownTimer(originalTime*60000, 500) {
//            public void onTick(long millisUntilFinished) {
//                long hour, min;
//                String h, m;
//                hour = millisUntilFinished/3600000;
//                if(hour<10) h = "0";
//                else h = "";
//                millisUntilFinished %= 3600000;
//                min = millisUntilFinished/60000;
//                if(min<10) m = "0";
//                else m = "";
//                if(bt2 == 0) {
//                    b2 = ":";
//                }
//                else if(bt2 == 3){
//                    b2 = " ";
//                }
//                else if(bt2 == 4){
//                    bt2 = -1;
//                }
//                bt2++;
//                TextView instruction2 = (TextView) findViewById(R.id.instruction2);
//                instruction2.setText(h + hour + " " + b2 + " " + m + min);
//                if(!i2 || autoStatus == 1) {
//                    instruction2.setText("");
//                    i2count = 0;
//                    cancel();
//                }
//            }
//            public void onFinish() {
//                WebView myWebView = (WebView) findViewById(R.id.webview);
//                myWebView.loadUrl( "http://192.168.0." + url + "/1" + phone_switch);
//                ImageButton buttonClick1 = (ImageButton)findViewById(R.id.phone);
//                buttonClick1.setBackgroundResource(R.drawable.phone_red);
//                TextView instruction2 = (TextView) findViewById(R.id.instruction2);
//                instruction2.setText("");
//                i2count = 0;
//            }
//        };
//        timer2.start();
//    }   //for phone off
//
//    public void phoneOffCancel(){
//        closeKeyBoard();
//        Button button2Click = (Button)findViewById(R.id.instruction2);
//        button2Click.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                i2 = false;
//                return false;
//            }
//        });
//    }
//
//    public void laptop (View view) {
//        closeKeyBoard();
//        laptopStatus = invert(laptopStatus);
//        if(laptopStatus == 0) {
//            if(i3count==0){
//                i3count = 1;
//                laptopOn();
//            }
//            else toast();
//        }
//        else if(laptopStatus == 1){
//            if(i4count==0){
//                i4count =1;
//                laptopOff();
//            }
//            else toast();
//        }
//    }
//
//    public void laptopOn(){
//        evaluateTime();
//        i3 = true;
//        bt3 = 0;
//        timer3 = new CountDownTimer(originalTime*60000, 500) {
//            public void onTick(long millisUntilFinished) {
//                long hour, min;
//                String h, m;
//                hour = millisUntilFinished/3600000;
//                if(hour<10) h = "0";
//                else h = "";
//                millisUntilFinished %= 3600000;
//                min = millisUntilFinished/60000;
//                if(min<10) m = "0";
//                else m = "";
//                if(bt3 == 0) {
//                    b3 = ":";
//                }
//                else if(bt3 == 3){
//                    b3 = " ";
//                }
//                else if(bt3 == 4){
//                    bt3 = -1;
//                }
//                bt3++;
//                TextView instruction3 = (TextView) findViewById(R.id.instruction3);
//                instruction3.setText(h + hour + " " + b3 + " " + m + min);
//                if(!i3){
//                    instruction3.setText("");
//                    i3count = 0;
//                    cancel();
//                }
//            }
//            public void onFinish() {
//                WebView myWebView = (WebView) findViewById(R.id.webview);
//                myWebView.loadUrl( "http://192.168.0." + url + "/0" + laptop_switch);
//                ImageButton buttonClick2 = (ImageButton)findViewById(R.id.laptop);
//                buttonClick2.setBackgroundResource(R.drawable.laptop_green);
//                TextView instruction3 = (TextView) findViewById(R.id.instruction3);
//                instruction3.setText("");
//                i3count = 0;
//            }
//        };
//        timer3.start();
//    }   //for laptop on
//
//    public void laptopOnCancel(){
//        closeKeyBoard();
//        Button button2Click = (Button)findViewById(R.id.instruction3);
//        button2Click.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                i3 = false;
//                return false;
//            }
//        });
//    }
//
//    public void laptopOff(){
//        evaluateTime();
//        i4 = true;
//        bt4 = 0;
//        timer4 = new CountDownTimer(originalTime*60000, 500) {
//            public void onTick(long millisUntilFinished) {
//                long hour, min;
//                String h, m;
//                hour = millisUntilFinished/3600000;
//                if(hour<10) h = "0";
//                else h = "";
//                millisUntilFinished %= 3600000;
//                min = millisUntilFinished/60000;
//                if(min<10) m = "0";
//                else m = "";
//                if(bt4 == 0) {
//                    b4 = ":";
//                }
//                else if(bt4 == 3){
//                    b4 = " ";
//                }
//                else if(bt4 == 4){
//                    bt4 = -1;
//                }
//                bt4++;
//                TextView instruction4 = (TextView) findViewById(R.id.instruction4);
//                instruction4.setText(h + hour + " " + b4 + " " + m + min);
//                if(!i4){
//                    instruction4.setText("");
//                    i4count = 0;
//                    cancel();
//                }
//            }
//            public void onFinish() {
//                WebView myWebView = (WebView) findViewById(R.id.webview);
//                myWebView.loadUrl( "http://192.168.0." + url + "/1" + laptop_switch);
//                ImageButton buttonClick2 = (ImageButton)findViewById(R.id.laptop);
//                buttonClick2.setBackgroundResource(R.drawable.laptop_red);
//                TextView instruction4 = (TextView) findViewById(R.id.instruction4);
//                instruction4.setText("");
//                i4count = 0;
//            }
//        };
//        timer4.start();
//    }   //for laptop off
//
//    public void laptopOffCancel(){
//        closeKeyBoard();
//        Button button2Click = (Button)findViewById(R.id.instruction4);
//        button2Click.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                i4 = false;
//                return false;
//            }
//        });
//    }
//
//    public void extra (View view) {
//        closeKeyBoard();
//        extraStatus = invert(extraStatus);
//        if(extraStatus == 0){
//            if(i5count == 0){
//                i5count = 1;
//                extraOn();
//            }
//            else toast();
//        }
//        else if(extraStatus == 1){
//            if(i6count == 0){
//                i6count =1;
//                extraOff();
//            }
//            else toast();
//        }
//    }
//
//    public void extraOn(){
//        evaluateTime();
//        i5 = true;
//        bt5 = 0;
//        timer5 = new CountDownTimer(originalTime*60000, 500) {
//            public void onTick(long millisUntilFinished) {
//                long hour, min;
//                String h, m;
//                hour = millisUntilFinished / 3600000;
//                if (hour < 10) h = "0";
//                else h = "";
//                millisUntilFinished %= 3600000;
//                min = millisUntilFinished / 60000;
//                if (min < 10) m = "0";
//                else m = "";
//                if(bt5 == 0) {
//                    b5 = ":";
//                }
//                else if(bt5 == 3){
//                    b5 = " ";
//                }
//                else if(bt5 == 4){
//                    bt5 = -1;
//                }
//                bt5++;
//                TextView instruction5 = (TextView) findViewById(R.id.instruction5);
//                instruction5.setText(h + hour + " " + b5 + " " + m + min);
//                if(!i5){
//                    instruction5.setText("");
//                    i5count = 0;
//                    cancel();
//                }
//            }
//            public void onFinish() {
//                WebView myWebView = (WebView) findViewById(R.id.webview);
//                myWebView.loadUrl( "http://192.168.0." + url + "/0" + extra_switch);
//                ImageButton buttonClick3 = (ImageButton)findViewById(R.id.extra);
//                buttonClick3.setBackgroundResource(R.drawable.extra_green);
//                TextView instruction5 = (TextView) findViewById(R.id.instruction5);
//                instruction5.setText("");
//                i5count = 0;
//            }
//        };
//        timer5.start();
//    }   //for extra on
//
//    public void extraOnCancel(){
//        closeKeyBoard();
//        Button button2Click = (Button)findViewById(R.id.instruction5);
//        button2Click.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                i5 = false;
//                return false;
//            }
//        });
//    }
//
//    public void extraOff(){
//        evaluateTime();
//        i6 = true;
//        bt6 = 0;
//        timer6 = new CountDownTimer(originalTime*60000, 500) {
//            public void onTick(long millisUntilFinished) {
//                long hour, min;
//                String h, m;
//                hour = millisUntilFinished/3600000;
//                if(hour<10) h = "0";
//                else h = "";
//                millisUntilFinished %= 3600000;
//                min = millisUntilFinished/60000;
//                if(min<10) m = "0";
//                else m = "";
//                if(bt6 == 0) {
//                    b6 = ":";
//                }
//                else if(bt6 == 3){
//                    b6 = " ";
//                }
//                else if(bt6 == 4){
//                    bt6 = -1;
//                }
//                bt6++;
//                TextView instruction6 = (TextView) findViewById(R.id.instruction6);
//                instruction6.setText(h + hour + " " + b6 + " " + m + min);
//                if(!i6){
//                    instruction6.setText("");
//                    i6count = 0;
//                    cancel();
//                }
//            }
//            public void onFinish() {
//                WebView myWebView = (WebView) findViewById(R.id.webview);
//                myWebView.loadUrl( "http://192.168.0." + url + "/1" + extra_switch);
//                ImageButton buttonClick3 = (ImageButton)findViewById(R.id.extra);
//                buttonClick3.setBackgroundResource(R.drawable.extra_red);
//                TextView instruction6 = (TextView) findViewById(R.id.instruction6);
//                instruction6.setText("");
//                i6count = 0;
//            }
//        };
//        timer6.start();
//    }   //for extra off
//
//    public void extraOffCancel(){
//        closeKeyBoard();
//        Button button2Click = (Button)findViewById(R.id.instruction6);
//        button2Click.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                i6 = false;
//                return false;
//            }
//        });
//    }
}

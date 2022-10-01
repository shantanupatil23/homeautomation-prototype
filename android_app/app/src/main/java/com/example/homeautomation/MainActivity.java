package com.example.homeautomation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.CountDownTimer;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    int loading = 0;
    Boolean isOfficeAutoON = false;
    View office_auto_bar;
    DatabaseReference office_auto_firebase;
    LinearLayout layout_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout_main = findViewById(R.id.activity_home);

        Toolbar my_toolbar = findViewById(R.id.action_bar);
        my_toolbar.setTitle("");
        my_toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_menu));
        setSupportActionBar(my_toolbar);

        findViewById(R.id.office_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, OfficeActivity.class));
            }
        });
        findViewById(R.id.kitchen_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, KitchenActivity.class));
            }
        });
        findViewById(R.id.living_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LivingActivity.class));
            }
        });
        findViewById(R.id.bed_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, BedActivity.class));
            }
        });


        office_auto_firebase = FirebaseDatabase.getInstance().getReference().child("NodeMCU").child("office_auto");
        office_auto_bar = findViewById(R.id.auto_bar);
        office_auto_firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                assert value != null;
                if (value.equals("ON")) {
                    Intent intent = new Intent(MainActivity.this, MyForeGroundServicePhoneAuto.class);
                    intent.setAction(MyForeGroundServicePhoneAuto.ACTION_START_FOREGROUND_SERVICE);
                    startService(intent);
                    isOfficeAutoON = true;
                    office_auto_bar.setBackgroundColor(Color.GREEN);
                } else {
                    isOfficeAutoON = false;
                    office_auto_bar.setBackgroundColor(Color.GRAY);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        ImageButton office_auto_image = findViewById(R.id.office_auto);
        office_auto_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOfficeAutoON) {
                    office_auto_firebase.setValue("OFF");
                } else {
                    office_auto_firebase.setValue("ON");
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.logout: {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
            break;
            case R.id.dev_info: {
                startActivity(new Intent(getApplicationContext(), DevelopersActivity.class));
            }
            break;

        }
        return super.onOptionsItemSelected(item);
    }
}

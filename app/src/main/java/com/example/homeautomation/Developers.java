package com.example.homeautomation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Developers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developers);

        findViewById(R.id.linkedin1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                try {
                    intent.setData(Uri.parse("https://in.linkedin.com/in/pshantanu"));
                    startActivity(intent);
                } catch (ActivityNotFoundException exception) {
                    Toast.makeText(getApplicationContext(), "Error text", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.linkedin2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                try {
                    intent.setData(Uri.parse("https://www.linkedin.com/in/vedank-pande-3b226317b"));
                    startActivity(intent);
                } catch (ActivityNotFoundException exception) {
                    Toast.makeText(getApplicationContext(), "Error text", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
package com.example.homeautomation;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.example.homeautomation.R;

public class LoginActivity extends AppCompatActivity {

    public FirebaseAuth mAuth;
    public EditText email,password;
    private static final String REGISTERED = "Register Boolean";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences(REGISTERED,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(REGISTERED,false);
        editor.apply();
        //uncomment below to automatically proceed to app if user is already signed in
        /*if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
        }*/
        email = findViewById(R.id.username);
        password = findViewById(R.id.password);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button go_to_register = findViewById(R.id.go_to_register);
        go_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EmailRegistration.class));
            }
        });
    }

    public void email_log_in(View view){
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.username);
        password = findViewById(R.id.password);
        String str_email = email.getText().toString();
        String str_password = password.getText().toString();
        mAuth.signInWithEmailAndPassword(str_email,str_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "logged in", Toast.LENGTH_SHORT).show();
                            SharedPreferences sharedPreferences = getSharedPreferences(REGISTERED,MODE_PRIVATE);
                            Boolean registered = sharedPreferences.getBoolean(REGISTERED,false);
                            // uncomment below if you do not want registration activity to appear after you have registered once
                            /*if (registered==true){
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            }
                            else{
                                startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
                            }*/
                            startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));

                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
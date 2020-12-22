package com.example.homeautomation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EmailRegistration extends AppCompatActivity {
    public FirebaseAuth mAuth;
    public EditText email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_registration);
    }

    public void RegisterEmail(View view){
        mAuth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        String str_email = email.getText().toString();
        String str_password = password.getText().toString();
        mAuth.createUserWithEmailAndPassword(str_email,str_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(EmailRegistration.this, "successfully registered", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                        else{
                            Toast.makeText(EmailRegistration.this, "could not register your email", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EmailRegistration.this, e.toString(), Toast.LENGTH_SHORT).show();
                Log.d("error log",e.toString());
            }
        });
    }
}

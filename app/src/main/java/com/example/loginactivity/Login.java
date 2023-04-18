package com.example.loginactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class Login extends AppCompatActivity {

    EditText sEmail,sPassword;
    Button sLogin;
    TextView sText;
    ProgressBar pb1;
    // int counter1=0;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        sEmail=findViewById(R.id.editEmail);
        sPassword=findViewById(R.id.editPassword);
        sLogin=findViewById(R.id.login);
        sText=findViewById(R.id.createText);
        pb1=findViewById(R.id.progressBar2);
        fAuth=FirebaseAuth.getInstance();


//        public void onStart() {
//            super.onStart();
//             Check if user is signed in (non-null) and update UI accordingly.
//            FirebaseUser currentUser = fAuth.getCurrentUser();
//            if(currentUser != null){
//                reload();
//            }

        sText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Register.class));
                finish();
            }
        });

        sLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=sEmail.getText().toString().trim();
                String password=sPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    sEmail.setError("PLEASE ENTER EMAIL");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    sPassword.setError("PLEASE ENTER PASSWORD");
                    return;
                }
                if(password.length()<6){
                    sPassword.setError("PASSWORD < 6 DIGITS");
                    return;
                }
                pb1.setVisibility(View.VISIBLE);
//                prog1();
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = fAuth.getCurrentUser();
                         //   updateUI(user);
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(Login.this, "Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                            pb1.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
//    public void prog1(){
//        pb1=findViewById(R.id.progressBar);
//
//        final Timer timer=new Timer();
//        TimerTask tt=new TimerTask() {
//            @Override
//            public void run() {
//                counter1++;
//                pb1.setProgress(counter1);
//
//                if(counter1==100){
//                    timer.cancel();
//                }
//            }
//        };
//        timer.schedule(tt,0,100);
//    }


}
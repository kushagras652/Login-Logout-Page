package com.example.loginactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Register extends AppCompatActivity {

    public static final String TAG="TAG";

    EditText name,email,password,phone;
    Button button;
    TextView loginButton;
    ProgressBar progressBar;
    int counter=0;

    FirebaseFirestore fStore;
    String userId;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        name=findViewById(R.id.fullName);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        phone=findViewById(R.id.phone);
        button=findViewById(R.id.button);
        loginButton=findViewById(R.id.logAccount);
        progressBar=findViewById(R.id.progressBar);

        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        if(fAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(Register.this,Login.class);
                startActivity(intent1);
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mEmail=email.getText().toString().trim();
                String mPassword=password.getText().toString().trim();
                final String mName=name.getText().toString();
                final String mPhone=phone.getText().toString();

                if(TextUtils.isEmpty(mEmail)){
                    email.setError("PLEASE ENTER EMAIL");
                    return;
                }

                if(TextUtils.isEmpty(mPassword)){
                    password.setError("PLEASE ENTER PASSWORD");
                    return;
                }
                if(mPassword.length()<6){
                    password.setError("PASSWORD IS WEAK MUST BE GREATER THAN 6 DIGITS");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                prog();

                fAuth.createUserWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser fUser=fAuth.getCurrentUser();
                            fUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(Register.this, "REGISTER SUCCESSFUL", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"ON failure:Email not sent"+e.getMessage());
                                }
                            });

                            Toast.makeText(Register.this, "USER CREATED", Toast.LENGTH_SHORT).show();
                            userId=fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference=fStore.collection("user").document(userId);
                            Map<String,Object> user=new HashMap<>();
                            user.put("fName",mName);
                            user.put("email",mEmail);
                            user.put("phone",mPhone);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG,"OnSuccess:user profile is created for"+userId);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"on failure:"+e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        }
                        else{
                            Toast.makeText(Register.this, "ERROR"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
   public void prog(){
        progressBar=findViewById(R.id.progressBar);

        final Timer timer=new Timer();
       TimerTask tt=new TimerTask() {
           @Override
           public void run() {
               counter++;
               progressBar.setProgress(counter);

               if(counter==100){
                   timer.cancel();
               }
           }
       };
       timer.schedule(tt,0,100);
   }
}
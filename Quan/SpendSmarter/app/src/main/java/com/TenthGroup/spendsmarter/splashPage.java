package com.TenthGroup.spendsmarter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class splashPage extends AppCompatActivity {
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_page);
        fAuth = FirebaseAuth.getInstance();
/*        SharedPreferences pass = getSharedPreferences("Pass_Mail", MODE_PRIVATE);
        String password = pass.getString("pass", "");
        SharedPreferences mail = getSharedPreferences("Mail_Pass", MODE_PRIVATE);
        String mail_log = mail.getString("mail", "");*/
        if (fAuth.getCurrentUser() != null) {
            Toast.makeText(splashPage.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
                }
            }, 3000);
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), welcomePage.class));
                    finish();
                }
            }, 3000);
        }
       /* if(password.equals("") && mail_log.equals(""))
        {
            Log.d("mail>", mail_log);
            Log.d("pass>", password);
            //startActivity(new Intent(getApplicationContext(), welcomePage.class));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), welcomePage.class));
                    finish();
                }
            }, 3000);
        }
        else{
            Log.d("log", "Lấy đc rồi");

            fAuth.signInWithEmailAndPassword(mail_log, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d("log", "Đăng nhập đc");
                        Toast.makeText(splashPage.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        Log.d("log", "lỏ vl");
                        Toast.makeText(splashPage.this, "Lỏ rồi!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), welcomePage.class));

                    }
                }
            });

        }*/
    }
}
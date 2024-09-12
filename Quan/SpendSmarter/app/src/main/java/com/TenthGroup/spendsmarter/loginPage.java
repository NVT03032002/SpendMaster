package com.TenthGroup.spendsmarter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

public class loginPage extends AppCompatActivity {
    EditText emailLogin, passLogin;
    Button loginButton;
    TextView changeformsignup, forgetpass;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        emailLogin = findViewById(R.id.email_login);
        passLogin = findViewById(R.id.pass_login);
        changeformsignup = findViewById(R.id.change_form_sign_up);
        loginButton = findViewById(R.id.login_button);
        forgetpass = findViewById(R.id.textView11);
        fAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_login = emailLogin.getText().toString().trim();
                String pass_login = passLogin.getText().toString().trim();
                if (TextUtils.isEmpty(email_login)) {
                    emailLogin.setError("Bạn chưa điền!");
                    return;
                }
                if (TextUtils.isEmpty(pass_login)) {
                    passLogin.setError("Bạn chưa điền!");
                    return;
                }
                SharedPreferences.Editor editor = getSharedPreferences("my_prefs", MODE_PRIVATE).edit();
                editor.putString("key", pass_login);
                editor.apply();

                fAuth.signInWithEmailAndPassword(email_login, pass_login).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(loginPage.this, "let go!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(loginPage.this, "Lỏ rồi!", Toast.LENGTH_SHORT).show();
                            Log.e("login_error", task.getException().getMessage());
                        }
                    }
                });
            }
        });

        changeformsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), signupPage.class));
            }
        });

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("string", emailLogin.getText().toString());
                if (TextUtils.isEmpty(emailLogin.getText().toString())) {
                    emailLogin.setError("Bạn chưa điền email!");
                    return;
                }
                else {
                    fAuth.fetchSignInMethodsForEmail(emailLogin.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            if (task.isSuccessful()) {
                                fAuth.sendPasswordResetEmail(emailLogin.getText().toString());
                                Toast.makeText(loginPage.this, "kiểm tra mail để đổi pass!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(loginPage.this, "email chưa được đăng ký!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });

    }

}
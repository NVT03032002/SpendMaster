package com.TenthGroup.spendsmarter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
        forgetpass = findViewById(R.id.forget_pass);
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
                //signinagain
                SharedPreferences.Editor editor = getSharedPreferences("my_prefs", MODE_PRIVATE).edit();
                editor.putString("key", pass_login);
                editor.apply();

                fAuth.signInWithEmailAndPassword(email_login, pass_login).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(loginPage.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            AppSystem.getAppSystem().setUID(fAuth.getCurrentUser().getUid());
                            AppSystem.getAppSystem().resetCategory();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            emailLogin.setError("kiểm tra lại email!");
                            passLogin.setError("kiểm tra lại mật khẩu!");
                            Toast.makeText(loginPage.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        });

        changeformsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), signupPage.class));
                finish();
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
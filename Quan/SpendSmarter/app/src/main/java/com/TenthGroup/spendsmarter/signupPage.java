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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class signupPage extends AppCompatActivity {

    FirebaseAuth fAuth;
    EditText vertifymail, passw, passwconfirm, nameaccount;
    Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        vertifymail = findViewById(R.id.vertify_mail);
        passw = findViewById(R.id.password);
        passwconfirm = findViewById(R.id.password_confirm);
        nameaccount = findViewById(R.id.name_account);
        buttonSignUp = findViewById(R.id.button_sign_up);

        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = vertifymail.getText().toString().trim();
                String passw_confirm = passwconfirm.getText().toString().trim();
                String pass_w = passw.getText().toString().trim();
                String name_account = nameaccount.getText().toString().trim();
                if (TextUtils.isEmpty(mail)) {
                    vertifymail.setError("Bạn chưa điền!");
                    return;
                }
                if (TextUtils.isEmpty(passw_confirm)) {
                    passwconfirm.setError("Bạn chưa điền!");
                }
                if (TextUtils.isEmpty(pass_w)) {
                    passw.setError("Bạn chưa điền!");
                }
                if (TextUtils.isEmpty(name_account)) {
                    nameaccount.setError("Bạn chưa điền!");
                }
                if (!pass_w.equals(passw_confirm)) {
                    passwconfirm.setError("mật khẩu lần 2 sai!");
                    return;
                }

                SharedPreferences.Editor editor = getSharedPreferences("my_prefs", MODE_PRIVATE).edit();
                editor.putString("key", passw_confirm);
                editor.apply();

                /*SharedPreferences.Editor getPass_Mail = getSharedPreferences("Pass_Mail", MODE_PRIVATE).edit();
                getPass_Mail.putString("pass", passw_confirm );
                getPass_Mail.apply();

                SharedPreferences.Editor getMail_Pass = getSharedPreferences("Mail_Pass", MODE_PRIVATE).edit();
                getMail_Pass.putString("mail", mail);
                getMail_Pass.apply();*/
              /*  fAuth.fetchSignInMethodsForEmail(vertifymail.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("mail:",vertifymail.getText().toString() );
                            vertifymail.setError("Mail đã được đăng ký!");
                        } else {
                            Log.d("mail_else:",vertifymail.getText().toString() );

                            fAuth.createUserWithEmailAndPassword(mail, passw_confirm).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(signupPage.this, "Tài khoản đã được tạo!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    } else {
                                        Toast.makeText(signupPage.this, "Lỏ rồi!", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }
                    }
                });*/

                fAuth.createUserWithEmailAndPassword(mail, passw_confirm).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(signupPage.this, "Tài khoản đã được tạo!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {

                            Toast.makeText(signupPage.this, "Lỏ rồi!", Toast.LENGTH_SHORT).show();
                            fAuth.fetchSignInMethodsForEmail(mail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        vertifymail.setError("Mail đã được sử dụng!");
                                    }
                                    else{
                                        Toast.makeText(signupPage.this, "Lỗi không xác định!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }
                });
            }
        });

    }
}
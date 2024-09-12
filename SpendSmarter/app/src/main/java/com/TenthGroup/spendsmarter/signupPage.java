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

public class signupPage extends AppCompatActivity {

    FirebaseAuth fAuth;
    EditText vertifymail, passw, passwconfirm, nameaccount;
    TextView changeform;
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
        changeform = findViewById(R.id.change_form_sign_in);

        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        buttonSignUp.setOnClickListener(view -> {
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

            fAuth.createUserWithEmailAndPassword(mail, passw_confirm).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(signupPage.this, "Tài khoản đã được tạo!", Toast.LENGTH_SHORT).show();
                    AppSystem.getAppSystem().setUID(fAuth.getCurrentUser().getUid());
                    AppSystem.getAppSystem().resetCategory();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(signupPage.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                    vertifymail.setError("Mail đã được sử dụng!");

/*
                    fAuth.fetchSignInMethodsForEmail(mail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            if(task.isSuccessful())
                            {
                            }
                            else{
                                Toast.makeText(signupPage.this, "Lỗi không xác định!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
*/

                }
            });

        });

        changeform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), loginPage.class));
                finish();
            }
        });

    }
}
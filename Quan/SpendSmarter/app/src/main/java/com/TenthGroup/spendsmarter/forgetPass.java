package com.TenthGroup.spendsmarter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

public class forgetPass extends AppCompatActivity {
    EditText oldpass, newpassword, newpasswordconfirm;
    TextView yeah;
    Button buttonconfirm;

    FirebaseAuth fAuth;
    FirebaseUser fUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        oldpass = findViewById(R.id.old_pass);
        newpassword = findViewById(R.id.new_password);
        newpasswordconfirm = findViewById(R.id.new_password_confirm);
        buttonconfirm = findViewById(R.id.button_confirm_change_pass);
        yeah = findViewById(R.id.yeah_Suc);
        yeah.setVisibility(View.GONE);

        fAuth = FirebaseAuth.getInstance();

        Drawable errorIcon = getResources().getDrawable(R.drawable.ic_check);
        errorIcon.setBounds(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight());
        /*SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
        String data = prefs.getString("key", "");*/

        oldpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String old_pass = oldpass.getText().toString().trim();
                //fUser = fAuth.getCurrentUser();
                /*if (fUser != null){
                    fAuth.signInWithEmailAndPassword(fUser.getEmail(), old_pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                oldpass.setError("Mật khẩu đúng", errorIcon);
                            }
                            else {
                                oldpass.setError(null);
                                oldpass.setError("sai mật khẩu cũ rồi!");
                            }
                        }
                    });

                }*/
                SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
                String data = prefs.getString("key", "");
                if (old_pass.equals(data))
                {
                    oldpass.setError("Mật khẩu đúng", errorIcon);
                }
                else{
                    oldpass.setError(null);
                    oldpass.setError("sai mật khẩu cũ rồi!");
                }


            }
        });


        buttonconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassWord = newpassword.getText().toString().trim();
                String newPassWordConfirm = newpasswordconfirm.getText().toString().trim();
                String old_pass = oldpass.getText().toString().trim();
                fUser = fAuth.getCurrentUser();
                if (TextUtils.isEmpty(newPassWord)) {
                    newpassword.setError("Bạn chưa điền!");
                    return;
                }
                if (TextUtils.isEmpty(newPassWordConfirm)) {
                    newpasswordconfirm.setError("Bạn chưa điền!");
                    return;
                }
                if (!newPassWord.equals(newPassWordConfirm)) {
                    newpasswordconfirm.setError("mật khẩu lần 2 sai!");
                    return;
                }
                if (fUser != null){
                    fAuth.signInWithEmailAndPassword(fUser.getEmail(), old_pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        fUser.updatePassword(newPassWordConfirm)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            Toast.makeText(forgetPass.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                                            FirebaseAuth.getInstance().signOut();
                                                            startActivity(new Intent(getApplicationContext(), loginPage.class));
                                                            finish();
                                                        }
                                                        else{
                                                            Toast.makeText(forgetPass.this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                    else {
                                        oldpass.setError("Sai mật khẩu cũ rồi!");
                                    }
                                }
                            });

                }
            }
        });
    }
}
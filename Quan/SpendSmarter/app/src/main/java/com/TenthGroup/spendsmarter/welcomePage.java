package com.TenthGroup.spendsmarter;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class welcomePage extends AppCompatActivity {
    Button dangnhap;
    Button dangky;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        dangnhap = findViewById(R.id.log_in_welcome);
        dangky = findViewById(R.id.sign_up_welcome);

        dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(welcomePage.this, loginPage.class );
                startActivity(intent);
            }
        });
        dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(welcomePage.this, signupPage.class );
                startActivity(intent1);
            }
        });

    }
}
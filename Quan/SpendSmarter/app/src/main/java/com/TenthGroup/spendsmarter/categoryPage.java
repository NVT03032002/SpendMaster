package com.TenthGroup.spendsmarter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class categoryPage extends AppCompatActivity {
    public void setMenu(){
        DrawerLayout drawerLayout = findViewById(R.id.categoryPage_layout);
        TextView menuImg = findViewById(R.id.menu_category);
        menuImg.setOnClickListener(v -> {
            NavigationView nav_menu = findViewById(R.id.nav_menu_category);
            drawerLayout.openDrawer(GravityCompat.START);
            nav_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.mainPage) {
                        Intent intent = new Intent(categoryPage.this, MainActivity.class);
                        startActivity(intent);
                    }
                    if (id == R.id.detailPage) {
                        Intent intent = new Intent(categoryPage.this, detailPage.class);
                        startActivity(intent);
                    }
                    if (id == R.id.settingPage) {
                        Intent intent = new Intent(categoryPage.this, settingPage.class);
                        startActivity(intent);
                    }

                    DrawerLayout drawer = findViewById(R.id.categoryPage_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
            });

        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_page);

        setMenu();
    }
}
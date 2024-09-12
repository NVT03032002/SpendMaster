package com.TenthGroup.spendsmarter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class settingPage extends AppCompatActivity {
    private int currentTheme;

    public void setMenu(){
        DrawerLayout drawerLayout = findViewById(R.id.settingPage_layout);
        TextView menuImg = findViewById(R.id.menu_setting);
        menuImg.setOnClickListener(v -> {
            NavigationView nav_menu = findViewById(R.id.nav_menu_setting);
            drawerLayout.openDrawer(GravityCompat.START);
            nav_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.mainPage) {
                        Intent intent = new Intent(settingPage.this, MainActivity.class);
                        startActivity(intent);
                    }
                    if (id == R.id.detailPage) {
                        Intent intent = new Intent(settingPage.this, detailPage.class);
                        startActivity(intent);
                    }
                    if (id == R.id.categoryPage) {
                        Intent intent = new Intent(settingPage.this, categoryPage.class);
                        startActivity(intent);
                    }
                    DrawerLayout drawer = findViewById(R.id.settingPage_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
            });

        });
    }
    LinearLayout settingFont;

    TextView viewSettingFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setAppTheme();

        setContentView(R.layout.activity_setting_page);
        setMenu();

        settingFont = findViewById(R.id.setting_font);
        viewSettingFont = findViewById(R.id.view_setting_font);

        settingFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(settingPage.this);
                builder.setTitle("Chọn font chữ");
                String[] fonts = {"montserrat_extrabold","montserrat_regular", "montserrat_semibold" };

                builder.setItems(fonts, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedFont = fonts[which];

                        SharedPreferences sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("selectedFont", selectedFont);
                        editor.commit();
                        recreate();
                        Toast.makeText(settingPage.this, "Bạn đã chọn font: " + selectedFont, Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
        setchangefont();

    }
    private void setchangefont() {
        SharedPreferences sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        String selectedFont = sharedPref.getString("selectedFont", "montserrat_extrabold"); //default font
        if (selectedFont.equals("montserrat_extrabold")) {
            viewSettingFont.setText("montserrat_extrabold");
            Log.d("font", viewSettingFont.getText().toString());
        } else if (selectedFont.equals("montserrat_regular")) {
            viewSettingFont.setText("montserrat_regular");
        } else if (selectedFont.equals("montserrat_semibold")) {
            viewSettingFont.setText("montserrat_semibold");
        }
    }
    private void setAppTheme() {
        SharedPreferences sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        String selectedFont = sharedPref.getString("selectedFont", "montserrat_extrabold"); //default font

        if (selectedFont.equals("montserrat_extrabold")) {
            setTheme(R.style.change_font);
        } else if (selectedFont.equals("montserrat_regular")) {
            setTheme(R.style.change_font1);
        } else if (selectedFont.equals("montserrat_semibold")) {
            setTheme(R.style.change_font2);
        }
    }

}
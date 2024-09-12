package com.TenthGroup.spendsmarter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class settingPage extends AppCompatActivity {
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
                        finish();
                    }
                    if (id == R.id.detailPage) {
                        Intent intent = new Intent(settingPage.this, detailPage.class);
                        startActivity(intent);
                        finish();
                    }
                    if (id == R.id.categoryPage) {
                        Intent intent = new Intent(settingPage.this, categoryPage.class);
                        startActivity(intent);
                        finish();
                    }
                    if (id == R.id.logout) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), loginPage.class));
                        finish();
                    }
                    if (id == R.id.ChangePass) {
                        startActivity(new Intent(getApplicationContext(), forgetPass.class));
                        finish();
                    }
                    DrawerLayout drawer = findViewById(R.id.settingPage_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
            });

        });
    }
    public static String NOTIFICATION_CHANNEL_ID = "2002";
    public static String default_notification_id = "default";
    public static String CURRENT_LANGUAGE = "";
    Context context;
    Resources resources;
    LinearLayout settingFont, settingNotify, settingLanguage;

    TextView viewSettingFont, viewSettingNotify, viewSettingLanguage, viewCurrentAcc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppTheme();
        setContentView(R.layout.activity_setting_page);
        setMenu();



        TextView setting_page = findViewById(R.id.setting_page);
        TextView account_current = findViewById(R.id.account_current);
        TextView setting_font_text = findViewById(R.id.setting_font_text);
        TextView language = findViewById(R.id.language);
        TextView setting_notify = findViewById(R.id.setting_notify);
        TextView delete_data = findViewById(R.id.delete_data);


        viewCurrentAcc = findViewById(R.id.viewCurrentAcc);
        viewCurrentAcc.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        settingFont = findViewById(R.id.setting_font);
        viewSettingFont = findViewById(R.id.view_setting_font);
        settingNotify = findViewById(R.id.set_notify);
        viewSettingNotify = findViewById(R.id.on_offNotify);
        settingLanguage = findViewById(R.id.setting_language);
        viewSettingLanguage = findViewById(R.id.view_setting_language);
        TextView textView = findViewById(R.id.reset_data);
        textView.setOnClickListener(view -> AppSystem.getAppSystem().resetCategory());

        settingFont.setOnClickListener(view -> {
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

        });
        setchangefont();

        saveStatusNotify();
        settingNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(settingPage.this);
                builder.setTitle("Cài đặt thông báo");
                builder.setPositiveButton("Bật", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean("notificationEnabled", true);
                        editor.apply();

                        scheduleNotification(getNotification("Hãy thêm thu chi"));
                        viewSettingNotify.setText("Đã bật thông báo");
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Tắt", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean("notificationEnabled", false);
                        editor.apply();

                        viewSettingNotify.setText("Đã tắt thông báo");
                        dialog.dismiss();
                    }
                });


                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        settingLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(settingPage.this);
                builder.setTitle("chọn ngôn ngữ");
                builder.setPositiveButton("English", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        CURRENT_LANGUAGE = "en";
                        context = LocaleHelper.setLocale(settingPage.this, "en");
                        resources = context.getResources();
                        setting_page.setText(resources.getString(R.string.setting_page));
                        account_current.setText(resources.getString(R.string.account_current));
                        setting_font_text.setText(resources.getString(R.string.setting_font));
                        language.setText(resources.getString(R.string.language));
                        setting_notify.setText(resources.getString(R.string.setting_notify));
                        delete_data.setText(resources.getString(R.string.delete_data));
                        textView.setText(resources.getString(R.string.delete_all_data));
                        viewSettingLanguage.setText("English");

                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Tiếng Việt", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CURRENT_LANGUAGE = "";
                        context = LocaleHelper.setLocale(settingPage.this, "");
                        resources = context.getResources();
                        setting_page.setText(resources.getString(R.string.setting_page));
                        account_current.setText(resources.getString(R.string.account_current));
                        setting_font_text.setText(resources.getString(R.string.setting_font));
                        language.setText(resources.getString(R.string.language));
                        setting_notify.setText(resources.getString(R.string.setting_notify));
                        delete_data.setText(resources.getString(R.string.delete_data));
                        textView.setText(resources.getString(R.string.delete_all_data));
                        viewSettingLanguage.setText("Tiếng Việt");

                        dialog.dismiss();
                    }
                });


                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
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

    //lap lich thoi gian thong bao
    private void scheduleNotification(Notification notification) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR_OF_DAY, 4);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATIONID, 1);
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    // set thông báo
    private Notification getNotification(String content){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, default_notification_id);
        builder.setContentTitle("Bạn có quên gì không?");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.logoapp);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        builder.setContentIntent(pendingIntent);

        return builder.build();
    }

    private void saveStatusNotify(){
        // Khôi phục trạng thái từ SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        boolean notificationEnabled = sharedPref.getBoolean("notificationEnabled", false);

        if (notificationEnabled) {
            viewSettingNotify.setText("Đã bật thông báo");
        } else {
            viewSettingNotify.setText("Đã tắt thông báo");
        }
    }

}
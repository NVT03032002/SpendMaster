package com.TenthGroup.spendsmarter;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.Navigation;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public void setPieChart() {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(25f, ""));
        entries.add(new PieEntry(75f, ""));

        PieDataSet dataSet = new PieDataSet(entries, "Label");
        dataSet.setColors(new int[] {Color.parseColor("#79CDCD"), Color.parseColor("#CD3333")});
        PieData data = new PieData(dataSet);
        PieChart chart = findViewById(R.id.DataChart);
        chart.setHoleRadius(65f);
        chart.setData(data);
        chart.setHoleColor(ContextCompat.getColor(this, android.R.color.transparent));
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.invalidate();
    }

    public void setDatePicker(){
        TextView day = findViewById(R.id.day);
        TextView week = findViewById(R.id.week);
        TextView month = findViewById(R.id.month);
        TextView year = findViewById(R.id.year);
        View date_picker_bar_main = findViewById(R.id.date_picker_bar_main);

        day.setOnClickListener(v -> {
            Log.e("x", day.getMeasuredWidth() + " " + day.getLeft() + " " + day.getWidth());
            date_picker_bar_main.animate()
                    .x( day.getLeft() + (day.getMeasuredWidth()/2 - dpToPx(50)/2))
                    .setDuration(500)
                    .start();
            setDateRange(1);
            setDateStyle(day, 2);
            setDateStyle(week,1);
            setDateStyle(month, 1);
            setDateStyle(year, 1);});
        week.setOnClickListener(v -> {
            setDateRange(2);
            date_picker_bar_main.animate()
                    .x( week.getLeft() + (week.getMeasuredWidth()/2 - dpToPx(50)/2))
                    .setDuration(500)
                    .start();
            setDateStyle(day, 1);
            setDateStyle(week,2);
            setDateStyle(month, 1);
            setDateStyle(year, 1);});
        month.setOnClickListener(v -> {
            setDateRange(3);
            date_picker_bar_main.animate()
                    .x( month.getX() + (month.getMeasuredWidth()/2 - dpToPx(50)/2))
                    .setDuration(500)
                    .start();
            setDateStyle(day, 1);
            setDateStyle(week,1);
            setDateStyle(month, 2);
            setDateStyle(year, 1);});
        year.setOnClickListener(v -> {
            setDateRange(3);
            date_picker_bar_main.animate()
                    .x( year.getX() + (year.getMeasuredWidth()/2 - dpToPx(50)/2))
                    .setDuration(500)
                    .start();
            setDateStyle(day, 1);
            setDateStyle(week,1);
            setDateStyle(month, 1);
            setDateStyle(year, 2);});
    }

    public void setTypePicker(){
        TextView expense = findViewById(R.id.expense_main);
        TextView income = findViewById(R.id.income_main);
        View menu_bar_main = findViewById(R.id.menu_bar_main);

        expense.setOnClickListener(v -> {
            menu_bar_main.animate()
                    .x( expense.getX() + (expense.getMeasuredWidth()/2 - dpToPx(70)/2))
                    .setDuration(500)
                    .start();

            setTypeStyle(expense, 2);
            setTypeStyle(income, 1);});

        income.setOnClickListener(v -> {
            menu_bar_main.animate()
                    .x( income.getX() + (income.getMeasuredWidth()/2 - dpToPx(70)/2))
                    .setDuration(500)
                    .start();

            setTypeStyle(expense, 1);
            setTypeStyle(income, 2);});
    }

    public int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public void setTypeStyle(TextView textView, int mode){
        int text_color = 0;
        if (mode == 1) {text_color = ContextCompat.getColor(this, R.color.type_uncheck);}
        if (mode == 2) {text_color = ContextCompat.getColor(this, R.color.type_check);}
        textView.setTextColor(text_color);
    }

    public void setDateStyle(TextView textView, int mode){
        int text_color = 0;
        if (mode == 1) {text_color = ContextCompat.getColor(this, R.color.date_uncheck);}
        if (mode == 2) {text_color = ContextCompat.getColor(this, R.color.date_check);}
        textView.setTextColor(text_color);
    }

    public String dayformat(int dd,int mm,int yyyy){
        String inputDate = dd + " " + mm + " " + yyyy;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd MM yyyy");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = inputDateFormat.parse(inputDate);
            String outputDateString = outputDateFormat.format(date);
            return outputDateString;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setDateRange(int i){
        TextView date = findViewById(R.id.dateRange);
        Calendar calendar = Calendar.getInstance();
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        int mm = calendar.get(Calendar.MONTH) + 1;
        int yyyy = calendar.get(Calendar.YEAR);
        int startdate;
        int enddate;
        String datepicked = "";

        switch (i) {
            case 1 :
                datepicked = dayformat(dd,mm,yyyy);
                break;
            case 2 :
                int weekday = calendar.get(Calendar.DAY_OF_WEEK);
                if (weekday == Calendar.SUNDAY){
                    startdate = dd + 1;
                    enddate = startdate + 6;
                } else {
                    startdate = dd - weekday + 2;
                    enddate = startdate + 6;
                }
                datepicked = dayformat(startdate,mm,yyyy) + " - " + dayformat(enddate,mm,yyyy);
                break;
            case 3 :
                int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                startdate = 1;
                datepicked = dayformat(startdate,mm,yyyy) + " - " + dayformat(day,mm,yyyy);
                break;
            case 4 :
                break;
        }
        date.setText(datepicked);
    }

    public void setMenu(){
        DrawerLayout drawerLayout = findViewById(R.id.mainPage_layout);
        ImageView menuImg = findViewById(R.id.menuImg_main);
        menuImg.setOnClickListener(v -> {
            NavigationView nav_menu = findViewById(R.id.nav_menu_main);
            drawerLayout.openDrawer(GravityCompat.START);
            nav_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.detailPage) {
                        Intent intent = new Intent(MainActivity.this, detailPage.class);
                        startActivity(intent);
                    }
                    if (id == R.id.categoryPage) {
                        Intent intent = new Intent(MainActivity.this, categoryPage.class);
                        startActivity(intent);
                    }
                    if (id == R.id.settingPage) {
                        Intent intent = new Intent(MainActivity.this, settingPage.class);
                        startActivity(intent);
                    }
                    if (id == R.id.logout) {
                        FirebaseAuth.getInstance().signOut();
                        /*SharedPreferences.Editor pass_del = getSharedPreferences("Pass_Mail", MODE_PRIVATE).edit();
                        pass_del.remove("pass");
                        pass_del.apply();
                        SharedPreferences.Editor mail_del = getSharedPreferences("Mail_Pass", MODE_PRIVATE).edit();
                        mail_del.remove("mail");
                        mail_del.apply();*/
                        startActivity(new Intent(getApplicationContext(), loginPage.class));
                        finish();
                    }
                    if (id == R.id.ChangePass) {
                        //FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), forgetPass.class));
                        finish();
                    }

                    DrawerLayout drawer = findViewById(R.id.mainPage_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
            });

        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppTheme();
        setContentView(R.layout.activity_main);

        setTypePicker();
        setDatePicker();
        setAddTransactionButton();
        setMenu();
        setPieChart();
    }

    private void setAddTransactionButton() {
        ImageView addTransactionButton = findViewById(R.id.add_button_main);
        addTransactionButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, addTransaction.class);
            startActivity(intent);
        });

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
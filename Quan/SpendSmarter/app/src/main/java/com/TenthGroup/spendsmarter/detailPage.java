package com.TenthGroup.spendsmarter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class detailPage extends AppCompatActivity {
    public void setMenu(){
        DrawerLayout drawerLayout = findViewById(R.id.detailPage_layout);
        TextView menuImg = findViewById(R.id.menu_detail);
        menuImg.setOnClickListener(v -> {
            NavigationView nav_menu = findViewById(R.id.nav_menu_detail);
            drawerLayout.openDrawer(GravityCompat.START);
            nav_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.mainPage) {
                        Intent intent = new Intent(detailPage.this, MainActivity.class);
                        startActivity(intent);
                    }
                    if (id == R.id.categoryPage) {
                        Intent intent = new Intent(detailPage.this, categoryPage.class);
                        startActivity(intent);
                    }
                    if (id == R.id.settingPage) {
                        Intent intent = new Intent(detailPage.this, settingPage.class);
                        startActivity(intent);
                    }

                    DrawerLayout drawer = findViewById(R.id.detailPage_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
            });
        });
    }

    public int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public void setTypePicker(){
        TextView total = findViewById(R.id.total_detail);
        TextView expense = findViewById(R.id.expense_detail);
        TextView income = findViewById(R.id.income_detail);

        View menu_bar_detail = findViewById(R.id.menu_bar_detail);

        total.setOnClickListener(v -> {
            menu_bar_detail.animate()
                    .x( total.getX() + (total.getWidth()/2 - dpToPx(70)/2))
                    .setDuration(500)
                    .start();

            setTypeStyle(total, 2);
            setTypeStyle(expense, 1);
            setTypeStyle(income, 1);});

        expense.setOnClickListener(v -> {
            menu_bar_detail.animate()
                    .x( expense.getX() + (total.getWidth()/2 - dpToPx(70)/2))
                    .setDuration(500)
                    .start();

            setTypeStyle(total, 1);
            setTypeStyle(expense, 2);
            setTypeStyle(income, 1);});

        income.setOnClickListener(v -> {
            menu_bar_detail.animate()
                    .x( income.getX() + (total.getWidth()/2 - dpToPx(70)/2))
                    .setDuration(500)
                    .start();

            setTypeStyle(total, 1);
            setTypeStyle(expense, 1);
            setTypeStyle(income, 2);});
    }

    public void setTypeStyle(TextView textView, int mode){
        int text_color = 0;
        switch (mode) {
            case 1 :
                text_color = ContextCompat.getColor(this, R.color.type_uncheck);
                break;
            case 2 :
                text_color = ContextCompat.getColor(this, R.color.type_check);
                break;
        }
        textView.setTextColor(text_color);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);

        setMenu();
        setTypePicker();

        BarChart barChart = findViewById(R.id.barChart);
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 10));
        entries.add(new BarEntry(2, 20));
        entries.add(new BarEntry(3, 30));
        entries.add(new BarEntry(4, 40));
        entries.add(new BarEntry(5, 50));
        BarDataSet dataSet = new BarDataSet(entries, "Label");
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.invalidate();
    }
}
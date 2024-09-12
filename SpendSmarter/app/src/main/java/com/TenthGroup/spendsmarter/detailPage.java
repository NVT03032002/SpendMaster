package com.TenthGroup.spendsmarter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class detailPage extends AppCompatActivity {
    private List<Transaction> trans_list;
    private BarChart barChart;

    private boolean isExpanse = false;
    private long mili_FirstDay, mili_LastDay;

    private TextView expense, income;
    private View menu_bottom_bar;
    private TextView day, week, month, year, other, detail_page;
    private View date_bottom_bar;
    private int dd, mm ,yyyy;
    Context context;
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppTheme();
        setContentView(R.layout.activity_detail_page);

        detail_page = findViewById(R.id.detail_page);
        expense = findViewById(R.id.expense_detail);
        income = findViewById(R.id.income_detail);
        day = findViewById(R.id.day_detail);
        week = findViewById(R.id.week_detail);
        month = findViewById(R.id.month_detail);
        year = findViewById(R.id.year_detail);
        other = findViewById(R.id.other_detail);

        context = LocaleHelper.setLocale(detailPage.this, settingPage.CURRENT_LANGUAGE);
        resources = context.getResources();

        detail_page.setText(resources.getString(R.string.detail_page));
        expense.setText(resources.getString(R.string.expenses));
        income.setText(resources.getString(R.string.incomes));
        day.setText(resources.getString(R.string.day));
        week.setText(resources.getString(R.string.week));
        month.setText(resources.getString(R.string.month));
        year.setText(resources.getString(R.string.year));
        other.setText(resources.getString(R.string.more));

        AppSystem.getAppSystem().readData();
        Thread thread = new Thread(){
            @Override
            public void run() {
                while (!AppSystem.getAppSystem().isReady()){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                runOnUiThread(() -> Day_onclick());
            }
        };
        thread.start();

        activity_init();
        setMenu();
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

    public void activity_init(){
        expense = findViewById(R.id.expense_detail);
        income = findViewById(R.id.income_detail);
        menu_bottom_bar = findViewById(R.id.menu_bar_detail);

        expense.setOnClickListener(v -> {
            if (!isExpanse) Expanse_Onclick();
        });

        income.setOnClickListener(v -> {
            if (isExpanse) Income_Onclick();
        });

        day = findViewById(R.id.day_detail);
        week = findViewById(R.id.week_detail);
        month = findViewById(R.id.month_detail);
        year = findViewById(R.id.year_detail);
        other = findViewById(R.id.other_detail);
        date_bottom_bar = findViewById(R.id.date_picker_bar_detail);

        day.setOnClickListener(v -> Day_onclick());
        week.setOnClickListener(v -> Week_onclick());
        month.setOnClickListener(v -> Month_onclick());
        year.setOnClickListener(v -> Year_onclick());
        other.setOnClickListener(v -> Other_onclick());

        //trans_list = database.getallTransaction();

        Calendar calendar = Calendar.getInstance();
        dd = calendar.get(Calendar.DAY_OF_MONTH);
        mm = calendar.get(Calendar.MONTH);
        yyyy = calendar.get(Calendar.YEAR);

        barChart = findViewById(R.id.barChart);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisLeft().setDrawZeroLine(true);
        barChart.getAxisLeft().setAxisMinimum(0f);
        barChart.getDescription().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(5);

        barChart.animateY(1000, Easing.EaseInOutQuad);
        barChart.invalidate();

        menu_bottom_bar.post(this::Expanse_Onclick);
        date_bottom_bar.post(this::Day_onclick);
    }

    public void Expanse_Onclick(){
        Type_Onclick_Animation(expense, income);
        Day_onclick();
    }

    public void Income_Onclick(){
        Type_Onclick_Animation(income, expense);
        Day_onclick();
    }

    public void Type_Onclick_Animation(TextView item1, TextView item2){
        isExpanse = (!isExpanse);
        menu_bottom_bar.animate()
                .x( item1.getX() + (item1.getMeasuredWidth()/2 - dpToPx(70)/2))
                .setDuration(500)
                .start();

        item1.setTextColor(ContextCompat.getColor(this, R.color.type_check));
        item2.setTextColor(ContextCompat.getColor(this, R.color.type_uncheck));
    }

    public void Day_onclick(){
        if (!AppSystem.getAppSystem().isReady()) return;
        Date_Onclick_Animation(day, week, month, year, other);

        Calendar LastDay = Calendar.getInstance();
        LastDay.set(yyyy,mm,dd,0,0,0);
        LastDay.add(Calendar.DATE, 1);
        Calendar FirstDay = Calendar.getInstance();
        FirstDay.set(yyyy,mm,dd,0,0,0);
        mili_FirstDay = FirstDay.getTimeInMillis();
        mili_LastDay = LastDay.getTimeInMillis();

        ArrayList<BarEntry> entries = new ArrayList<>();
        String[] node = new String[] {"", "A", "B", "C", "D", "E"};

        for (int i = 0; i < 5; i++) {
            node[5 - i] = FirstDay.get(Calendar.DATE) + "/" + FirstDay.get(Calendar.MONTH);
            int data = 0;
            for (Transaction item : AppSystem.getAppSystem().getTransactionList()) {
                if (item == null) continue;
                if (item.getisExpense() == isExpanse && (item.getDate() >= mili_FirstDay && item.getDate() < mili_LastDay))
                    data += item.getAmount();
            }
            entries.add(new BarEntry(5 - i, data));
            FirstDay.add(Calendar.DATE, -1);
            LastDay.add(Calendar.DATE, -1);
            mili_FirstDay = FirstDay.getTimeInMillis();
            mili_LastDay = LastDay.getTimeInMillis();
        }

        setChart(entries, node);
    }

    public void Week_onclick(){
        Date_Onclick_Animation(week, day, month, year, other);

        Calendar LastDay = Calendar.getInstance();
        Calendar FirstDay = Calendar.getInstance();

        FirstDay.set(yyyy, mm , dd);
        LastDay.set(yyyy, mm , dd);
        int dayOfWeek = FirstDay.get(Calendar.DAY_OF_WEEK);
        FirstDay.add(Calendar.DATE, - dayOfWeek + 2);
        LastDay.add(Calendar.DATE, - dayOfWeek + 9);
        mili_FirstDay = FirstDay.getTimeInMillis();
        mili_LastDay = LastDay.getTimeInMillis();

        ArrayList<BarEntry> entries = new ArrayList<>();
        String[] node = new String[] {"", "A", "B", "C", "D", "E"};

        for (int i = 0; i < 5; i++) {
            Log.e("TAG", "-----------------------------------------");
            Log.e("TAG", FirstDay.toString());
            Log.e("TAG", LastDay.toString());
            node[i + 1] = "Tuần " + (i + 1);
            int data = 0;
            for (Transaction item : AppSystem.getAppSystem().getTransactionList()) {
                if (item == null) continue;
                if (item.getisExpense() == isExpanse && (item.getDate() >= mili_FirstDay && item.getDate() < mili_LastDay))
                    data += item.getAmount();
            }
            entries.add(new BarEntry(5 - i, data));
            FirstDay.add(Calendar.DATE, -7);
            LastDay.add(Calendar.DATE, -7);
            mili_FirstDay = FirstDay.getTimeInMillis();
            mili_LastDay = LastDay.getTimeInMillis();
        }
        setChart(entries, node);
    }

    public void Month_onclick(){
        Date_Onclick_Animation(month, day, week, year, other);
        Calendar FirstDay = Calendar.getInstance();
        Calendar LastDay = Calendar.getInstance();

        FirstDay.set(yyyy, mm , 1);
        LastDay.set(yyyy, mm , 1);
        LastDay.add(Calendar.MONTH, 1);

        mili_FirstDay = FirstDay.getTimeInMillis();
        mili_LastDay = LastDay.getTimeInMillis();

        ArrayList<BarEntry> entries = new ArrayList<>();
        String[] node = new String[] {"", "A", "B", "C", "D", "E"};

        for (int i = 0; i < 5; i++) {
            node[5 - i] = FirstDay.get(Calendar.MONTH) + "/" + FirstDay.get(Calendar.YEAR);
            int data = 0;
            for (Transaction item : AppSystem.getAppSystem().getTransactionList()) {
                if (item == null) continue;
                if (item.getisExpense() == isExpanse && (item.getDate() >= mili_FirstDay && item.getDate() < mili_LastDay))
                    data += item.getAmount();
            }
            entries.add(new BarEntry(5 - i, data));
            FirstDay.add(Calendar.MONTH, -1);
            LastDay.add(Calendar.MONTH, -1);
            mili_FirstDay = FirstDay.getTimeInMillis();
            mili_LastDay = LastDay.getTimeInMillis();
        }
        setChart(entries, node);
    }

    public void Year_onclick(){
        Date_Onclick_Animation(year, day, week, month, other);

        Calendar FirstDay = Calendar.getInstance();
        Calendar LastDay = Calendar.getInstance();

        FirstDay.set(yyyy, 0 , 1);
        LastDay.set(yyyy, 0 , 1);
        LastDay.add(Calendar.YEAR, 1);

        mili_FirstDay = FirstDay.getTimeInMillis();
        mili_LastDay = LastDay.getTimeInMillis();

        ArrayList<BarEntry> entries = new ArrayList<>();
        String[] node = new String[] {"", "A", "B", "C", "D", "E"};

        for (int i = 0; i < 5; i++) {
            Log.e("TAG", "-----------------------------------------");
            Log.e("TAG", FirstDay.toString());
            Log.e("TAG", LastDay.toString());
            node[5 - i] = "" + FirstDay.get(Calendar.YEAR);
            int data = 0;
            for (Transaction item : AppSystem.getAppSystem().getTransactionList()) {
                if (item == null) continue;
                if (item.getisExpense() == isExpanse && (item.getDate() >= mili_FirstDay && item.getDate() < mili_LastDay))
                    data += item.getAmount();
            }
            entries.add(new BarEntry(5 - i, data));
            FirstDay.add(Calendar.YEAR, -1);
            LastDay.add(Calendar.YEAR, -1);
            mili_FirstDay = FirstDay.getTimeInMillis();
            mili_LastDay = LastDay.getTimeInMillis();
        }
        setChart(entries, node);
    }

    public void Other_onclick(){
        Toast.makeText(detailPage.this, "Chức năng đang được phát triển", Toast.LENGTH_SHORT);
        Date_Onclick_Animation(other, day, week, month, year);
    }

    public void Date_Onclick_Animation(TextView item1, TextView item2, TextView item3, TextView item4, TextView item5){
        date_bottom_bar.animate()
                .x( item1.getX() + (item1.getMeasuredWidth()/2 - dpToPx(50)/2))
                .setDuration(500)
                .start();

        item1.setTextColor(ContextCompat.getColor(this, R.color.date_check));
        item2.setTextColor(ContextCompat.getColor(this, R.color.date_uncheck));
        item3.setTextColor(ContextCompat.getColor(this, R.color.date_uncheck));
        item4.setTextColor(ContextCompat.getColor(this, R.color.date_uncheck));
        item5.setTextColor(ContextCompat.getColor(this, R.color.date_uncheck));
    }

    public void setMenu(){
        DrawerLayout drawerLayout = findViewById(R.id.detailPage_layout);
        TextView menuImg = findViewById(R.id.menu_detail);
        menuImg.setOnClickListener(v -> {
            NavigationView nav_menu = findViewById(R.id.nav_menu_detail);
            drawerLayout.openDrawer(GravityCompat.START);
            nav_menu.setNavigationItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.mainPage) {
                    Intent intent = new Intent(detailPage.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                if (id == R.id.categoryPage) {
                    Intent intent = new Intent(detailPage.this, categoryPage.class);
                    startActivity(intent);
                    finish();
                }
                if (id == R.id.settingPage) {
                    Intent intent = new Intent(detailPage.this, settingPage.class);
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

                DrawerLayout drawer = findViewById(R.id.detailPage_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            });
        });
    }

    public int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public void setChart(ArrayList<BarEntry> entries, String[] node){
        BarDataSet dataSet = new BarDataSet(entries, null);
        BarData barData = new BarData(dataSet);

        barData.setBarWidth(0.7f);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.0f", value);
            }
        });

        barChart.setFitBars(true);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(node));

        barChart.setData(barData);
        barChart.invalidate();
    }
}
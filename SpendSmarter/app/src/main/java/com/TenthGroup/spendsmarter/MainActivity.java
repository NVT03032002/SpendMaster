package com.TenthGroup.spendsmarter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    /*-- Recycler View của Main Activity --*/
    private RecyclerView main_RecyclerView;
    private mainHistory_RecyclerView main_adapter;
    Context context;
    Resources resources;

    /*-- Các cơ sở dữ liệu sẽ được gọi đến --*/
    private List<Transaction> current_trans_list = new ArrayList<>();

    /*-- Các biến để biết bây giờ đang thực thi với phần tử nào --*/
    private boolean isExpanse = false;
    private String current_time = "day";
    private long mili_FirstDay, mili_LastDay;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    /*-- Các biến để thap tác với Layout --*/
    private TextView expense, income;
    private View menu_bottom_bar;
    private TextView day, week, month, year, other, total, left_arrow, right_arrow;
    private View date_bottom_bar;
    private TextView date;
    private int dd, mm ,yyyy;

    private PieChart chart;
    private PieDataSet dataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setAppTheme();
        setContentView(R.layout.activity_main);

        total = findViewById(R.id.total);
        expense = findViewById(R.id.expense_main);
        income = findViewById(R.id.income_main);
        day = findViewById(R.id.day);
        week = findViewById(R.id.week);
        month = findViewById(R.id.month);
        year = findViewById(R.id.year);
        other = findViewById(R.id.other);

        context = LocaleHelper.setLocale(MainActivity.this, settingPage.CURRENT_LANGUAGE);
        resources = context.getResources();

        total.setText(resources.getString(R.string.total));
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
                runOnUiThread(() -> set_current_transdata());
            }
        };
        thread.start();

        menu_init();
        activity_init();

        ImageView add_button = findViewById(R.id.add_button_main);
        add_button.setOnClickListener(l -> {
            Intent intent = new Intent(this, addTransaction.class);
            intent.putExtra("isExpanse", isExpanse);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        set_current_transdata();
        setDataPieChart();
    }

    private void activity_init(){
        main_RecyclerView = findViewById(R.id.transaction_history);
        main_adapter = new mainHistory_RecyclerView(current_trans_list);
        main_RecyclerView.setAdapter(main_adapter);

        expense = findViewById(R.id.expense_main);
        income = findViewById(R.id.income_main);
        menu_bottom_bar = findViewById(R.id.menu_bar_main);

        expense.setOnClickListener(v -> {
            if (!isExpanse) Expanse_Onclick();
        });
        income.setOnClickListener(v -> {
            if (isExpanse) Income_Onclick();
        });

        day = findViewById(R.id.day);
        week = findViewById(R.id.week);
        month = findViewById(R.id.month);
        year = findViewById(R.id.year);
        other = findViewById(R.id.other);
        date_bottom_bar = findViewById(R.id.date_picker_bar_main);

        day.setOnClickListener(v -> Day_onclick());
        week.setOnClickListener(v -> Week_onclick());
        month.setOnClickListener(v -> Month_onclick());
        year.setOnClickListener(v -> Year_onclick());
        other.setOnClickListener(v -> Other_onclick());

        date = findViewById(R.id.dateRange);
        left_arrow = findViewById(R.id.left_arrow);
        right_arrow = findViewById(R.id.right_arrow);

        left_arrow.setOnClickListener(v -> left_arrow_onclick());
        right_arrow.setOnClickListener(v -> right_arrow_onclick());

        Calendar calendar = Calendar.getInstance();
        dd = calendar.get(Calendar.DAY_OF_MONTH);
        mm = calendar.get(Calendar.MONTH);
        yyyy = calendar.get(Calendar.YEAR);

        setPieChart();

        menu_bottom_bar.post(this::Expanse_Onclick);
        date_bottom_bar.post(this::Day_onclick);
    }

    public void menu_init(){
        DrawerLayout drawer = findViewById(R.id.mainPage_layout);
        TextView menuImg = findViewById(R.id.menu_main);
        menuImg.setOnClickListener(v -> {
            NavigationView nav_menu = findViewById(R.id.nav_menu_main);
            drawer.openDrawer(GravityCompat.START);
            nav_menu.setNavigationItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.detailPage) {
                    Intent intent = new Intent(MainActivity.this, detailPage.class);
                    startActivity(intent);
                    finish();
                }
                if (id == R.id.categoryPage) {
                    Intent intent = new Intent(MainActivity.this, categoryPage.class);
                    startActivity(intent);
                    finish();
                }
                if (id == R.id.settingPage) {
                    Intent intent = new Intent(MainActivity.this, settingPage.class);
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
                drawer.closeDrawer(GravityCompat.START);
                return true;
            });
        });
    }

    public void setPieChart() {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(1));
        dataSet = new PieDataSet(entries, "");
        dataSet.setColors(Color.parseColor("#8e9788"));
        dataSet.setDrawValues(false);
        dataSet.setSliceSpace(5f);
        PieData data = new PieData(dataSet);

        chart = findViewById(R.id.DataChart);
        chart.setTouchEnabled(false);
        chart.setHoleRadius(60f);
        chart.setHoleColor(ContextCompat.getColor(this, android.R.color.transparent));
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setData(data);
        chart.invalidate();
    }

    public void setDataPieChart(){
        Log.e("TAG", current_trans_list.toString() );
        if (current_trans_list.size() == 0)
        {
            List<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry(1));
            dataSet.setColors(Color.parseColor("#8e9788"));
            dataSet.setValues(entries);
            PieData data = new PieData(dataSet);
            chart.setData(data);
            chart.invalidate();
            return;
        }
        /*-- Lấy ra số lượng Category có trong Danh sách Giao dịch --*/
        List<Integer> Category_id_list = new ArrayList<>();
        for (Transaction item : current_trans_list){
            if (!Category_id_list.contains(item.getCategory_id()))
                Category_id_list.add(item.Category_id);
        }

        /*-- Lấy tổng số tiền cho từng Category có trong Danh sách Giao dịch --*/
        List<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < Category_id_list.size(); i++) {
            int temp_money = 0;
            for (Transaction item : current_trans_list){
                if (item.getCategory_id() == Category_id_list.get(i))
                    temp_money += item.getAmount();
            }
            entries.add(new PieEntry(temp_money));
        }

        List<Integer> Color_List = new ArrayList<>();
        for (int i = 0; i < Category_id_list.size(); i++) {
            Color_List.add(Color.parseColor(
                    AppSystem.getAppSystem().getCategoryById(Category_id_list.get(i)).getColor()));
        }

        dataSet.setValues(entries);
        dataSet.setColors(Color_List);
        PieData data = new PieData(dataSet);
        chart.setData(data);
        chart.invalidate();
    }

    public int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
    public void Expanse_Onclick(){
        Type_Onclick_Animation(expense, income);
        set_current_transdata();
    }

    public void Income_Onclick(){
        Type_Onclick_Animation(income, expense);
        set_current_transdata();
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
        current_time = "day";
        Date_Onclick_Animation(day, week, month, year, other);

        Calendar FirstDay = Calendar.getInstance();
        FirstDay.set(yyyy,mm,dd,0,0,0);
        Calendar LastDay = Calendar.getInstance();
        LastDay.set(yyyy,mm,dd,23,59,59);
        mili_FirstDay = FirstDay.getTimeInMillis();
        mili_LastDay = LastDay.getTimeInMillis();

        date.setText(dateFormat.format(new Date(mili_FirstDay)));

        set_current_transdata();
    }

    public void Week_onclick(){
        current_time = "week";
        Date_Onclick_Animation(week, day, month, year, other);

        TextView date = findViewById(R.id.dateRange);
        Calendar FirstDay = Calendar.getInstance();
        Calendar LastDay = Calendar.getInstance();

        FirstDay.set(yyyy, mm , dd);
        LastDay.set(yyyy, mm , dd);
        int dayOfWeek = FirstDay.get(Calendar.DAY_OF_WEEK);
        FirstDay.add(Calendar.DATE, - dayOfWeek + 2);
        LastDay.add(Calendar.DATE, - dayOfWeek + 8);

        mili_FirstDay = FirstDay.getTimeInMillis();
        mili_LastDay = LastDay.getTimeInMillis();

        date.setText(String.format("%s - %s",
                dateFormat.format(new Date(mili_FirstDay)),
                dateFormat.format(new Date(mili_LastDay))));

        set_current_transdata();
    }

    public void Month_onclick(){
        current_time = "month";
        Date_Onclick_Animation(month, day, week, year, other);
        Calendar current_day = Calendar.getInstance();

        TextView date = findViewById(R.id.dateRange);
        Calendar FirstDay = Calendar.getInstance();
        Calendar LastDay = Calendar.getInstance();

        current_day.set(yyyy,mm, 1);
        int lastDayOfMonth = current_day.getActualMaximum(Calendar.DAY_OF_MONTH);
        FirstDay.set(yyyy, mm , 1, 0,0,0);
        LastDay.set(yyyy, mm , lastDayOfMonth, 23, 59, 59);

        mili_FirstDay = FirstDay.getTimeInMillis();
        mili_LastDay = LastDay.getTimeInMillis();

        date.setText(String.format("%s - %s",
                dateFormat.format(new Date(mili_FirstDay)),
                dateFormat.format(new Date(mili_LastDay))));

        set_current_transdata();
    }

    public void Year_onclick(){
        current_time = "year";
        Date_Onclick_Animation(year, day, week, month, other);

        Calendar FirstDay = Calendar.getInstance();
        Calendar LastDay = Calendar.getInstance();
        Calendar current_day = Calendar.getInstance();

        current_day.set(yyyy, 11 , 1);
        int lastDayOfMonth = current_day.getActualMaximum(Calendar.DAY_OF_MONTH);

        FirstDay.set(yyyy, 0 , 1, 0,0,0);
        LastDay.set(yyyy, 11 , lastDayOfMonth, 23, 59, 59);

        mili_FirstDay = FirstDay.getTimeInMillis();
        mili_LastDay = LastDay.getTimeInMillis();

        TextView date = findViewById(R.id.dateRange);
        date.setText(String.format("%s - %s",
                dateFormat.format(new Date(mili_FirstDay)),
                dateFormat.format(new Date(mili_LastDay))));

        set_current_transdata();
    }

    public void Other_onclick(){
        TextView date = findViewById(R.id.dateRange);
        date.setText("Chức năng đang được phát triển");
        Date_Onclick_Animation(other, day, week, month, year);
    }

    public void left_arrow_onclick(){
    }

    public void right_arrow_onclick(){
    }

    public void set_current_transdata(){
        if (!AppSystem.getAppSystem().isReady()) return;

        int sum = 0;
        current_trans_list.clear();
        Log.e("TAG", AppSystem.getAppSystem().getTransactionList().toString() );
        for (Transaction item : AppSystem.getAppSystem().getTransactionList()){
            if (item == null) continue;
            if (item.getisExpense()) sum -= item.getAmount();
            else sum += item.getAmount();
            if (item.getisExpense() == isExpanse && (item.getDate() >= mili_FirstDay && item.getDate() <= mili_LastDay))
                current_trans_list.add(item);
        }
        TextView tv = findViewById(R.id.current_money);
        tv.setText(sum + " đ");
        main_adapter.updateData(current_trans_list);
        setDataPieChart();
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
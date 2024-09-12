package com.TenthGroup.spendsmarter;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class categoryPage extends AppCompatActivity implements category_RecyclerView.OnItemClickListener{

    // Declare variable
    private RecyclerView categoryPage_RecycleView;
    private category_RecyclerView adapter ;
    private List<Category> currentList;

    private boolean isExpanse = true;
    private boolean isResume = false;
    private TextView expense, income, category_page;
    private View bottom_bar;
    Context context;
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppTheme();
        setContentView(R.layout.activity_category_page);

        expense = findViewById(R.id.expense_category);
        income = findViewById(R.id.income_category);
        category_page = findViewById(R.id.category_page);
        context = LocaleHelper.setLocale(categoryPage.this, settingPage.CURRENT_LANGUAGE);
        resources = context.getResources();

        expense.setText(resources.getString(R.string.expenses));
        income.setText(resources.getString(R.string.incomes));
        category_page.setText(resources.getString(R.string.category_page));


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
                runOnUiThread(() -> getCurrentList());
            }
        };
        thread.start();


        activity_init();
        menu_init();
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

    /**
     * Initialize Activity
     */
    public void activity_init(){
        income = findViewById(R.id.income_category);
        expense = findViewById(R.id.expense_category);
        bottom_bar = findViewById(R.id.menu_bar_category);

        expense.setOnClickListener(v -> {
            if(!isExpanse) Expanse_Onclick();
        });
        income.setOnClickListener(v -> {
            if(isExpanse)Income_Onclick();
        });

        categoryPage_RecycleView = findViewById(R.id.Recycler_View);
        currentList = new ArrayList<>();
        adapter = new category_RecyclerView(currentList);
        getCurrentList();
        adapter.setOnItemClickListener(this);
        categoryPage_RecycleView.setAdapter(adapter);

        bottom_bar.post(this::Expanse_Onclick);
    };

    public void getCurrentList(){
        if (!AppSystem.getAppSystem().isReady())
            return;

        currentList.clear();
        for (Category item : AppSystem.getAppSystem().getCategoryList()){
            if (item == null) continue;
            if (item.getIsExpanse() == isExpanse)
                currentList.add(item);
        }
        currentList.add(new Category(-1, "Thêm", true, "add", "#11998e"));
        adapter.updateData(currentList);
    }

    public void menu_init(){
        DrawerLayout drawerLayout = findViewById(R.id.categoryPage_layout);
        TextView menuImg = findViewById(R.id.menu_category);
        menuImg.setOnClickListener(v -> {
            NavigationView nav_menu = findViewById(R.id.nav_menu_category);
            drawerLayout.openDrawer(GravityCompat.START);
            nav_menu.setNavigationItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.mainPage) {
                    Intent intent = new Intent(categoryPage.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                if (id == R.id.detailPage) {
                    Intent intent = new Intent(categoryPage.this, detailPage.class);
                    startActivity(intent);
                    finish();
                }
                if (id == R.id.settingPage) {
                    Intent intent = new Intent(categoryPage.this, settingPage.class);
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

                DrawerLayout drawer = findViewById(R.id.categoryPage_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            });
        });
    }

    public void Expanse_Onclick(){
        isExpanse = true;
        Type_Onclick_Animation(expense, income);
        getCurrentList();
    }

    public void Type_Onclick_Animation(TextView item1, TextView item2){
        bottom_bar.animate()
                .x( item1.getX() + (item1.getMeasuredWidth()/2 - dpToPx(70)/2))
                .setDuration(500)
                .start();

        item1.setTextColor(ContextCompat.getColor(this, R.color.type_check));
        item2.setTextColor(ContextCompat.getColor(this, R.color.type_uncheck));

        adapter.updateData(currentList);
    }

    public void Income_Onclick(){
        isExpanse = false;
        Type_Onclick_Animation(income, expense);
        getCurrentList();
    }

    public int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    public void onItemClick(Category value, int pos) {
        if (pos <  currentList.size() -1)
        {
            Intent intent = new Intent(categoryPage.this, categoryModifyPage.class);
            intent.putExtra("mode", "modify");
            intent.putExtra("id", value.getId());
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(categoryPage.this, categoryModifyPage.class);
            intent.putExtra("mode", "create");
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResume = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isResume){
            getCurrentList();
        }
    }
}
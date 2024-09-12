package com.TenthGroup.spendsmarter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class addTransaction extends AppCompatActivity implements category_RecyclerView.OnItemClickListener {
    private RecyclerView addTransPage_RecyclerView;
    private category_RecyclerView addTransAdapter ;

    private List<Category> currentList = new ArrayList<>();

    private Transaction transaction;
    private boolean isExpanse = true;
    private int current_category = -1;

    private TextView expense, income, add_tracsaction, category, note;
    private EditText money_input, add_trans_detail;
    private View bottom_bar;
    private DatePicker datePicker;
    private Button add_button_trans;
    Context context;
    Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        add_tracsaction = findViewById(R.id.add_tracsaction);
        expense = findViewById(R.id.expense_add_trans);
        income = findViewById(R.id.income_add_trans);
        money_input = findViewById(R.id.money_input);
        note = findViewById(R.id.note);
        add_button_trans = findViewById(R.id.add_button_trans);

        context = LocaleHelper.setLocale(addTransaction.this, settingPage.CURRENT_LANGUAGE);
        resources = context.getResources();

        add_tracsaction.setText(resources.getString(R.string.add_tracsaction));
        expense.setText(resources.getString(R.string.expenses));
        income.setText(resources.getString(R.string.incomes));
        money_input.setText(resources.getString(R.string.input_money));
        note.setText(resources.getString(R.string.note));
        add_button_trans.setText(resources.getString(R.string.add_button));

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
                runOnUiThread(() -> updateView());
            }
        };
        thread.start();
        transaction = new Transaction(0, 0, true, 0, 0, "");
        activity_init();
        default_setting();
    }

    public void activity_init(){
        expense = findViewById(R.id.expense_add_trans);
        income = findViewById(R.id.income_add_trans);
        bottom_bar = findViewById(R.id.menu_bar_add_trans);
        datePicker = findViewById(R.id.datePicker);

        expense.setOnClickListener(v -> {
            if(!isExpanse) Expanse_Onclick();
        });
        income.setOnClickListener(v -> {
            if(isExpanse)Income_Onclick();
        });


        addTransPage_RecyclerView = findViewById(R.id.add_trans_category_Recycler_View);
        currentList = new ArrayList<>();
        addTransAdapter = new category_RecyclerView(currentList);
        addTransPage_RecyclerView.post(() -> addTransAdapter.setOnItemClickListener(addTransaction.this));
        addTransPage_RecyclerView.setAdapter(addTransAdapter);

        bottom_bar.post(() -> {
            Intent intent = getIntent();
            boolean result = intent.getBooleanExtra("isExpanse", true);
            if (result){
                Expanse_Onclick();
                isExpanse = true;
            }
            else{
                Income_Onclick();
                isExpanse = false;
            }

        });

        datePicker.post(() -> {
            Calendar calendar = Calendar.getInstance();
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            int mm = calendar.get(Calendar.MONTH);
            int yyyy = calendar.get(Calendar.YEAR);
            datePicker.init(yyyy, mm, dd, null);
        });
    }

    @Override
    public void onItemClick(Category value, int pos) {
        transaction.setCategory_id(value.getId());
        View v;
        if(current_category != -1){
            v = addTransPage_RecyclerView
                    .getLayoutManager()
                    .getChildAt(current_category)
                    .findViewById(R.id.category_background);
            v.setBackgroundResource(0);
        }

        current_category = pos;
        v = addTransPage_RecyclerView
                .getLayoutManager()
                .getChildAt(current_category)
                .findViewById(R.id.category_background);
        v.setBackgroundColor(Color.parseColor("#BDDAF1"));
    };

    public void default_setting(){
        TextView return_icon = findViewById(R.id.return_icon_add_trans);
        return_icon.setOnClickListener(v -> finish());

        EditText amount_input = findViewById(R.id.money_input);
        EditText trans_detail = findViewById(R.id.add_trans_detail);
        Button submit_button = findViewById(R.id.add_button_trans);
        submit_button.setOnClickListener(l -> {
            String str = amount_input.getText().toString();
            if (str.equals("")){
                Toast.makeText(addTransaction.this, "Vui lòng nhập số tiền", Toast.LENGTH_SHORT).show();
                return;
            }
            int amount = Integer.parseInt(str);
            if (amount < 1000) {
                Toast.makeText(addTransaction.this, "Nhập số tiền khác lớn hơn giùm", Toast.LENGTH_SHORT).show();
                return;
            }
            transaction.setAmount(amount);
            transaction.setisExpense(isExpanse);
            if (transaction.getCategory_id() == 0){
                Toast.makeText(addTransaction.this, "Chọn loại chi tiêu", Toast.LENGTH_SHORT).show();
                return;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.set(datePicker.getYear(), datePicker.getMonth() , datePicker.getDayOfMonth());
            transaction.setDate(calendar.getTimeInMillis());

            transaction.setDetail(trans_detail.getText().toString());

            AppSystem.getAppSystem().updateTransaction(transaction);
            finish();
        });
    }

    public void Expanse_Onclick(){
        isExpanse = true;
        transaction.setCategory_id(0);
        if(current_category != -1){
            View v = addTransPage_RecyclerView
                    .getLayoutManager()
                    .getChildAt(current_category)
                    .findViewById(R.id.category_background);
            v.setBackgroundResource(0);
        }
        current_category = -1;
        Type_Onclick_Animation(expense, income);
    }

    public void Income_Onclick(){
        isExpanse = false;
        transaction.setCategory_id(0);
        if(current_category != -1){
            View v = addTransPage_RecyclerView
                    .getLayoutManager()
                    .getChildAt(current_category)
                    .findViewById(R.id.category_background);
            v.setBackgroundResource(0);
        }
        current_category = -1;
        Type_Onclick_Animation(income, expense);
    }

    public void Type_Onclick_Animation(TextView item1, TextView item2){
        bottom_bar.animate()
                .x( item1.getX() + (item1.getMeasuredWidth()/2 - dpToPx(70)/2))
                .setDuration(500)
                .start();

        item1.setTextColor(ContextCompat.getColor(this, R.color.type_check));
        item2.setTextColor(ContextCompat.getColor(this, R.color.type_uncheck));

        updateView();
    }

    public int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public void updateView(){
        if (!AppSystem.getAppSystem().isReady()) return;
        currentList.clear();
        for (Category item : AppSystem.getAppSystem().getCategoryList()){
            if (item == null) continue;
            if (item.getIsExpanse() == isExpanse)
                currentList.add(item);
        }
        Log.e("TAG", currentList.toString());
        addTransAdapter.updateData(currentList);
    }
}
package com.TenthGroup.spendsmarter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class categoryModifyPage extends AppCompatActivity implements categorySelection_RecycleView.OnItemClickListener{

    private String TAG = "mysql";

    private Category category;
    private List<Category> categoryList;
    private int icon_index = -1;
    private int color_index = -1;

    private RecyclerView icon_RecycleView;
    private categorySelection_RecycleView icon_adapter ;
    private List<String> IconList;

    private RecyclerView color_RecycleView;
    private categorySelection_RecycleView color_adapter ;
    private List<String> ColorList;

    private TextView create_category,icon,color_add;
    private RadioButton radioButton1, radioButton2;

    private Button submit_button;
    Context context;
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppTheme();
        setContentView(R.layout.activity_category_modify_page);

        create_category = findViewById(R.id.create_category);
        icon = findViewById(R.id.icon);
        color_add = findViewById(R.id.color_add);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        submit_button = findViewById(R.id.submit_button);

        context = LocaleHelper.setLocale(categoryModifyPage.this, settingPage.CURRENT_LANGUAGE);
        resources = context.getResources();

        create_category.setText(resources.getString(R.string.add_tracsaction));
        icon.setText(resources.getString(R.string.icon));
        color_add.setText(resources.getString(R.string.color_add));
        radioButton1.setText(resources.getString(R.string.expenses));
        radioButton2.setText(resources.getString(R.string.incomes));
        submit_button.setText(resources.getString(R.string.add_button));



        Intent intent = getIntent();
        String mode = intent.getStringExtra("mode");
        int id = 0;
        Log.e(TAG, mode);
        if (mode.equals("modify")){
            id = intent.getIntExtra("id", 0);
            Log.e(TAG, AppSystem.getAppSystem().getCategoryList().toString() );
            category = AppSystem.getAppSystem().getCategoryById(id);
            Log.e(TAG, category.toString() );
        }
        else {
            category = new Category(0, "", true, "person", "#11998e");
        }

        activity_init();
        default_setting(mode);
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


    private void activity_init() {
        ImageView current_icon = findViewById(R.id.current_category_icon);
        EditText category_name = findViewById(R.id.category_name_edittext);
        RadioButton selection_1 = findViewById(R.id.radioButton1);
        RadioButton selection_2 = findViewById(R.id.radioButton2);

        String icon_path = "android.resource://com.TenthGroup.spendsmarter/drawable/"
                + category.getIcon_url();
        current_icon.setImageURI(Uri.parse(icon_path));
        category_name.setText(category.getName());

        if (category.getIsExpanse()) {
            selection_1.setChecked(true);
            selection_2.setChecked(false);
        }
        else {
            selection_1.setChecked(false);
            selection_2.setChecked(true);
        }

        IconList = new ArrayList<>();
        IconList.add("food");
        IconList.add("water");
        IconList.add("electric");
        IconList.add("shopping");
        IconList.add("fitness");

        icon_RecycleView = findViewById(R.id.Icon_Recycler_View);
        icon_adapter = new categorySelection_RecycleView(IconList, 1);
        icon_RecycleView.post(() -> icon_adapter.setOnItemClickListener(categoryModifyPage.this));
        icon_RecycleView.setAdapter(icon_adapter);

        icon_RecycleView.post(() -> {
            for (String item : IconList) {
                if (category.getIcon_url().equals(item)){
                    icon_index = IconList.indexOf(item);
                    View v = icon_RecycleView
                            .getLayoutManager()
                            .getChildAt(icon_index)
                            .findViewById(R.id.icon_background);
                    v.setBackgroundColor(Color.parseColor("#BDDAF1"));
                }
            }
        });

        ColorList = new ArrayList<>();
        ColorList.add("#11998e");
        ColorList.add("#EF4949");
        ColorList.add("#4CAF50");
        ColorList.add("#2196F3");
        ColorList.add("#E91E63");
        ColorList.add("#8BC34A");
        ColorList.add("#BDDAF1");

        color_RecycleView = findViewById(R.id.Color_Recycler_View);
        color_adapter = new categorySelection_RecycleView(ColorList, 2);
        color_RecycleView.post(() -> color_adapter.setOnItemClickListener(categoryModifyPage.this));
        color_RecycleView.setAdapter(color_adapter);

        color_RecycleView.post(() -> {
            for (String item : ColorList) {
                if (Color.parseColor(category.getColor()) == Color.parseColor(item)){
                    color_index = ColorList.indexOf(item);
                    View v = color_RecycleView
                            .getLayoutManager()
                            .getChildAt(color_index)
                            .findViewById(R.id.icon);
                    v.setVisibility(View.VISIBLE);
                }
            }
            setColor_Icon_list();
        });
    }

    private void setColor_Icon_list(){
        for (int i = 0; i < icon_RecycleView.getChildCount(); i++) {
            View v = icon_RecycleView
                    .getLayoutManager()
                    .getChildAt(i)
                    .findViewById(R.id.icon);
            ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor(category.getColor()));
            v.setBackgroundTintList(colorStateList);
        }

        ImageView current_icon = findViewById(R.id.current_category_icon);
        ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor(category.getColor()));
        current_icon.setBackgroundTintList(colorStateList);
    }

    public void default_setting(String mode) {
        TextView return_icon = findViewById(R.id.return_icon);
        return_icon.setOnClickListener(v -> finish());

        TextView category_delete_icon = findViewById(R.id.category_delete);
        category_delete_icon.setOnClickListener(v-> {

            finish();
        });


        RadioGroup type_selection = findViewById(R.id.type_selection);
        type_selection.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButton1) category.setIsExpanse(true);
            if (checkedId == R.id.radioButton2) category.setIsExpanse(false);
        });

        EditText category_name = findViewById(R.id.category_name_edittext);
        Button submit_button = findViewById(R.id.submit_button);
        submit_button.setOnClickListener(v -> {
            if (category_name.getText().toString().equals("")) {
                Toast.makeText(categoryModifyPage.this, "Không được để trống tên", Toast.LENGTH_SHORT).show();
            } else {
                category.setName(category_name.getText().toString());
                if (mode.equals("modify")) {
                    AppSystem.getAppSystem().updateCategory(category);
                    finish();
                } else {
                    categoryList = AppSystem.getAppSystem().getCategoryList();
                    int new_id = 1;
                    boolean isnewID = false;
                    while (!isnewID) {
                        isnewID = true;
                        for (Category item : categoryList) {
                            if (new_id == item.getId()) {
                                new_id = new_id + 1;
                                isnewID = false;
                                break;
                            }
                        }
                    }
                    category.setId(new_id);
                    AppSystem.getAppSystem().updateCategory(category);
                }
                finish();
            }
        });
    }

    public void set_onclick(String value) {
        ImageView current_icon = findViewById(R.id.current_category_icon);
        View v;
        /* Set current icon selection background */
        if(icon_index != -1){
            v = icon_RecycleView
                    .getLayoutManager()
                    .getChildAt(icon_index)
                    .findViewById(R.id.icon_background);
            v.setBackgroundResource(0);
        }

        /* Set new icon selection background and current icon background*/
        category.setIcon_url(value);
        for (String item : IconList) {
            if (value.equals(item))
            {
                icon_index = IconList.indexOf(item);
                v = icon_RecycleView
                        .getLayoutManager()
                        .getChildAt(icon_index)
                        .findViewById(R.id.icon_background);
                v.setBackgroundColor(Color.parseColor("#BDDAF1"));
                String icon_path = "android.resource://com.TenthGroup.spendsmarter/drawable/" + item;
                current_icon.setImageURI(Uri.parse(icon_path));
                break;
            }
        }
    }

    public void set_Colorclick(String color){
        View v;
        if(color_index != -1){
            v = color_RecycleView
                    .getLayoutManager()
                    .getChildAt(color_index)
                    .findViewById(R.id.icon);
            v.setVisibility(View.INVISIBLE);
        }

        category.setColor(color);
        for (String item : ColorList) {
            if (Color.parseColor(category.getColor()) == Color.parseColor(item)){
                color_index = ColorList.indexOf(item);
                v = color_RecycleView
                        .getLayoutManager()
                        .getChildAt(color_index)
                        .findViewById(R.id.icon);
                v.setVisibility(View.VISIBLE);
            }
        }

        setColor_Icon_list();
    }
}
package com.TenthGroup.spendsmarter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AppSystem {
    private static AppSystem appSystem;
    private static List<Category> CategoryList = new ArrayList<>();
    private static List<Transaction> TransactionList = new ArrayList<>();

    private static boolean isReady = false;
    private static String UID = FirebaseAuth.getInstance().getUid();
    private String api = "https://spendsmarterver2-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public AppSystem(){
        database = FirebaseDatabase.getInstance(api);
        myRef = database.getReference(UID);
    }

    public static synchronized AppSystem getAppSystem(){
        if (appSystem == null) {
            appSystem = new AppSystem();
        }
        return appSystem;
    }

    public boolean isReady(){
        return isReady;
    }

    public void setCategoryList(List<Category> CategoryList){
        this.CategoryList = CategoryList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        TransactionList = transactionList;
    }

    public void setUID(String UID) {
        this.UID = UID;
        myRef = database.getReference(UID);
        readData();
        TransactionList = new ArrayList<>();
    }

    public String getUID() {
        return UID;
    }

    public List<Transaction> getTransactionList() {
        return TransactionList;
    }

    public List<Category> getCategoryList(){
        return CategoryList;
    }

    public Category getCategoryById(int id){
        Category temp = new Category(100, "temp", true, "add", "#000000");
        for (Category item : getCategoryList())
        {
            if (item == null) continue;
            if (item.getId() == id){
                temp = item;
                break;
            }
        }
        return temp;
    }

    public void resetCategory(){
        List<Category> default_data = new ArrayList<>();
        myRef.child("CategoryList").setValue(default_data);
        default_data.add(new Category(1, "Di chuyển", true, "moving", "#efcc4e"));
        default_data.add(new Category(2, "Tập thể dục", true, "fitness", "#82d35d"));
        default_data.add(new Category(3, "Tạp phẩm", true, "food", "#6cd4cd"));
        default_data.add(new Category(4, "Quà tặng", true, "gift", "#9cd490"));
        default_data.add(new Category(5, "Gia đình", true, "family", "#ff4345"));
        default_data.add(new Category(6, "Phiếu quà", false, "ticket", "#82d35d"));
        default_data.add(new Category(7, "Lương", false, "salary", "#ff4345"));
        default_data.add(new Category(8, "Khác", false, "question", "#11998e"));
        default_data.add(new Category(9, "Khác", true, "question", "#11998e"));


        for (Category item : default_data){
            myRef.child("CategoryList").child(item.getId() + "").setValue(item);
        }
    }

    public void readData() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Log.e("TAG", UID );
                    appSystem = snapshot.getValue(AppSystem.class);
                    isReady = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateCategory(Category category){
        myRef.child("CategoryList").child(category.getId() + "").setValue(category);
    }

    public void updateTransaction(Transaction transaction){
        if (TransactionList.size() == 0) transaction.setId(1);
        else {
            int new_id = 1;
            boolean isnewID = false;
            while (!isnewID) {
                isnewID = true;
                for (Transaction item : TransactionList) {
                    if (item == null) continue;
                    if (new_id == item.getId()) {
                        new_id = new_id + 1;
                        isnewID = false;
                        break;
                    }
                }
            }
            transaction.setId(new_id);
        }
        myRef.child("TransactionList").child(transaction.getId() + "").setValue(transaction);
    }

    public String toString(){
        String temp = "";
        if (CategoryList != null) {
            temp += CategoryList.toString();
        }
        else temp += "null";

        return temp;
    }
}

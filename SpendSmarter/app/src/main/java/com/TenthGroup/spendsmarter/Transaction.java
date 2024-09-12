package com.TenthGroup.spendsmarter;

public class Transaction {
    int id;
    int amount;
    boolean Expense;
    int Category_id ;
    long date;
    String detail;

    public Transaction(){}

    public Transaction(int id, int amount, boolean Expense, int Category_id, long date, String detail){
        this.id = id;
        this.amount = amount;
        this.Expense = Expense;
        this.Category_id = Category_id;
        this.date = date;
        this.detail = detail;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public boolean getisExpense(){
        return Expense;
    }

    public int getCategory_id() {
        return Category_id;
    }

    public long getDate() {
        return date;
    }

    public String getDetail() {
        return detail;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setisExpense(boolean expense) {
        Expense = expense;
    }

    public void setCategory_id(int category_id) {
        Category_id = category_id;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String toString(){
        return this.id + "-" + this.amount;
    }
}

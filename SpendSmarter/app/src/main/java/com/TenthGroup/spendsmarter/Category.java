package com.TenthGroup.spendsmarter;
import androidx.annotation.Nullable;

public class Category {
    int id;
    String name;
    boolean isExpanse;
    String icon_url;
    String color;

    public Category(){}

    public Category(int id, String name, boolean isExpanse, String icon_url, String color){
        this.id = id;
        this.name = name;
        this.isExpanse = isExpanse;
        this.icon_url = icon_url;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean getIsExpanse() {
        return isExpanse;
    }

    public void setIsExpanse(boolean isExpanse) {
        this.isExpanse = isExpanse;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public String getColor() {
        return color;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String toString(){
        return this.id + "-" + this.name + "-" + this.isExpanse + "-" + this.icon_url + "-" + this.color;
    }
}

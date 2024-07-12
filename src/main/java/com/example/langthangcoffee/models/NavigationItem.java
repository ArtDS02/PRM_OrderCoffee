package com.example.langthangcoffee.models;

import android.graphics.drawable.Drawable;

public class NavigationItem {
    private String key;
    private String title;
    private Drawable drawable;
    private int id;

    public NavigationItem() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public int getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setKey(String key) {
        this.key = key;
    }

}

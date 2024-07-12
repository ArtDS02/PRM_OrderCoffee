package com.example.langthangcoffee.models;

public class ToppingSanPham {
    private int maSanPham;
    private String tenTopping;
    private int giaTien;
    public ToppingSanPham() {

    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public void setGiaTien(int giaTien) {
        this.giaTien = giaTien;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public int getGiaTien() {
        return giaTien;
    }

    public String getTenTopping() {
        return tenTopping;
    }

    public void setTenTopping(String tenTopping) {
        this.tenTopping = tenTopping;
    }
}

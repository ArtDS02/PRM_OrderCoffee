package com.example.langthangcoffee.models;

public class FoodOrderTopping {

    private String tenTopping;
    private int maSanPham;
    private int giaTien;

    public FoodOrderTopping(int maSanPham, String tenTopping, int giaTien) {
        this.maSanPham = maSanPham;
        this.giaTien = giaTien;
        this.tenTopping = tenTopping;
    }

    public FoodOrderTopping() {

    }
    public String getTenTopping() {
        return tenTopping;
    }
    public int getMaSanPham() {
        return maSanPham;
    }
    public int getGiaTien() {
        return giaTien;
    }
    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }
    public void setTenTopping(String tenTopping) {
        this.tenTopping = tenTopping;

    }
    public void setGiaTien(int giaTien) {
        this.giaTien = giaTien;
    }



}

package com.example.langthangcoffee.models;

public class FoodOrderSize {

    private String tenKichThuoc;
    private int maSanPham;
    private int giaTien;

    public FoodOrderSize(int maSanPham, String tenKichThuoc, int giaTien) {
        this.maSanPham = maSanPham;
        this.giaTien = giaTien;
        this.tenKichThuoc = tenKichThuoc;
    }

    public FoodOrderSize() {

    }
    public String getTenKichThuoc() {
        return tenKichThuoc;
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
    public void setTenKichThuoc(String tenKichThuoc) {
        this.tenKichThuoc = tenKichThuoc;

    }
    public void setGiaTien(int giaTien) {
        this.giaTien = giaTien;
    }



}

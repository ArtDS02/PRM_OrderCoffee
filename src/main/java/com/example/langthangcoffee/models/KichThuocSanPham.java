package com.example.langthangcoffee.models;

public class KichThuocSanPham {
    private int maSanPham;
    private String tenKichThuoc;
    private int giaTien;
    public KichThuocSanPham() {
    }

    public String getTenKichThuoc() {
        return tenKichThuoc;
    }

    public void setTenKichThuoc(String tenKichThuoc) {
        this.tenKichThuoc = tenKichThuoc;
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

    public void setGiaTien(int giaTien) {
        this.giaTien = giaTien;
    }
}

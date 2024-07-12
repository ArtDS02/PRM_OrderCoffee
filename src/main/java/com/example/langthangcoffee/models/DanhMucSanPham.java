package com.example.langthangcoffee.models;

public class DanhMucSanPham {
    private int maDanhMuc;
    private String tenDanhMuc;

    public DanhMucSanPham() {
    }
    public DanhMucSanPham(int maDanhMuc, String tenDanhMuc) {
        this.maDanhMuc = maDanhMuc;
        this.tenDanhMuc = tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    public void setMaDanhMuc(int maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public int getMaDanhMuc() {
        return maDanhMuc;
    }

    @Override
    public String toString() {
        return String.valueOf(maDanhMuc) + " - " + tenDanhMuc;
    }
}

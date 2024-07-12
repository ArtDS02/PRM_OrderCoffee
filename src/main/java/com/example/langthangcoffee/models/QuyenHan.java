package com.example.langthangcoffee.models;

public class QuyenHan {
    private int maQuyenHan;
    private  String tenQuyenHan;
    public QuyenHan() {

    }
    public QuyenHan(int maQuyenHan, String tenQuyenHan) {
        this.maQuyenHan = maQuyenHan;
        this.tenQuyenHan = tenQuyenHan;

    }

    public void setTenQuyenHan(String tenQuyenHan) {
        this.tenQuyenHan = tenQuyenHan;
    }

    public void setMaQuyenHan(int maQuyenHan) {
        this.maQuyenHan = maQuyenHan;
    }

    public int getMaQuyenHan() {
        return maQuyenHan;
    }

    public String getTenQuyenHan() {
        return tenQuyenHan;
    }

    @Override
    public String toString() {
        return String.valueOf(maQuyenHan) + " - " + tenQuyenHan;
    }
}

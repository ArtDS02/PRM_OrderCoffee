package com.example.langthangcoffee.models;

public class LoaiVoucher {
    private int maLoaiVoucher;
    private String tenLoaiVoucher;
    private String moTaVoucher;
    private String hinhAnh;
    private int tongTien;
    private int phiShip;
    private int soLuongToiThieu;
    public LoaiVoucher() {

    }
    public void setMaLoaiVoucher(int maLoaiVoucher) {
        this.maLoaiVoucher = maLoaiVoucher;
    }
    public void setTenLoaiVoucher(String tenLoaiVoucher) {
        this.tenLoaiVoucher = tenLoaiVoucher;
    }
    public void setMoTaVoucher(String moTaVoucher) {
        this.moTaVoucher = moTaVoucher;
    }
    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
    public String getTenLoaiVoucher() {
        return tenLoaiVoucher;
    }
    public String getMoTaVoucher() {
        return moTaVoucher;
    }
    public String getHinhAnh() {
        return hinhAnh;
    }
    public int getMaLoaiVoucher() {
        return maLoaiVoucher;
    }

    public int getPhiShip() {
        return phiShip;
    }

    public int getSoLuongToiThieu() {
        return soLuongToiThieu;
    }

    public int getTongTien() {
        return tongTien;
    }

    public void setPhiShip(int phiShip) {
        this.phiShip = phiShip;
    }

    public void setSoLuongToiThieu(int soLuongToiThieu) {
        this.soLuongToiThieu = soLuongToiThieu;
    }

    public void setTongTien(int tongTien) {
        this.tongTien = tongTien;
    }

    @Override
    public String toString() {
        return tenLoaiVoucher;
    }
}

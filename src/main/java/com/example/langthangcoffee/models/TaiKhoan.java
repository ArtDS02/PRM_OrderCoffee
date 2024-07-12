package com.example.langthangcoffee.models;

public class TaiKhoan {
    private String sdtTaiKhoan;
    private String matKhau;
    private String ho;
    private  String ten;
    private String diaChiGiaoHang;
    private String tenQuyenHan;
    private String notificationToken;
    private String thoiGianThamGia;
    private int maQuyenHan;

    public TaiKhoan(){

    }


    public void setNotificationToken(String notificationToken) {
        this.notificationToken = notificationToken;
    }

    public String getNotificationToken() {
        return notificationToken;
    }

    public String getThoiGianThamGia() {
        return thoiGianThamGia;
    }

    public void setThoiGianThamGia(String thoiGianThamGia) {
        this.thoiGianThamGia = thoiGianThamGia;
    }

    public void setTenQuyenHan(String tenQuyenHan) {
        this.tenQuyenHan = tenQuyenHan;
    }
    public String getTenQuyenHan() {
        return tenQuyenHan;
    }

    public void setSdtTaiKhoan(String sdtTaiKhoan) {
        this.sdtTaiKhoan = sdtTaiKhoan;
    }
    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }
    public void setHo(String ho) {
        this.ho = ho;
    }
    public void setTen(String ten) {
        this.ten = ten;
    }
    public void setDiaChiGiaoHang(String diaChiGiaoHang) {
        this.diaChiGiaoHang = diaChiGiaoHang;
    }
    public void setMaQuyenHan(int maQuyenHan) {
        this.maQuyenHan = maQuyenHan;
    }
    public String getSdtTaiKhoan() {
        return sdtTaiKhoan;
    }
    public String getMatKhau() {
        return matKhau;
    }
    public String getHo() {
        return ho;
    }
    public String getTen() {
        return ten;
    }
    public String getDiaChiGiaoHang() {
        return diaChiGiaoHang;
    }
    public int getMaQuyenHan() {
        return maQuyenHan;
    }
    @Override
    public String toString() {
        return sdtTaiKhoan;
    }
}

package com.example.langthangcoffee.models;

public class Voucher {
    private String maVoucher;
    private TaiKhoan taiKhoan;
    private LoaiVoucher loaiVoucher;
    private String thoiGianTao;
    private String thoiGianHetHan;
    private String sdtTaiKhoan;
    private int maLoaiVoucher;
    private int suDung;

    public Voucher() {

    }

    public void setMaLoaiVoucher(int maLoaiVoucher) {
        this.maLoaiVoucher = maLoaiVoucher;
    }

    public void setSdtTaiKhoan(String sdtTaiKhoan) {
        this.sdtTaiKhoan = sdtTaiKhoan;
    }

    public void setSuDung(int suDung) {
        this.suDung = suDung;
    }

    public String getSdtTaiKhoan() {
        return sdtTaiKhoan;
    }

    public int getMaLoaiVoucher() {
        return maLoaiVoucher;
    }

    public int getSuDung() {
        return suDung;
    }

    public void setLoaiVoucher(LoaiVoucher loaiVoucher) {
        this.loaiVoucher = loaiVoucher;
    }

    public void setMaVoucher(String maVoucher) {
        this.maVoucher = maVoucher;
    }

    public void setTaiKhoan(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public void setThoiGianHetHan(String thoiGianHetHan) {
        this.thoiGianHetHan = thoiGianHetHan;
    }

    public void setThoiGianTao(String thoiGianTao) {
        this.thoiGianTao = thoiGianTao;
    }

    public String getMaVoucher() {
        return maVoucher;
    }

    public LoaiVoucher getLoaiVoucher() {
        return loaiVoucher;
    }

    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }

    public String getThoiGianHetHan() {
        return thoiGianHetHan;
    }

    public String getThoiGianTao() {
        return thoiGianTao;
    }
}

package com.example.langthangcoffee.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LichSuOrder implements Comparable<LichSuOrder>  {
    private int maLichSuOrder;
    private int soLuong;
    private int thanhTien;
    private String ghiChu;
    private String kichThuoc;
    private String tenSanPham;
    private String topping;
    private LocalDateTime thoiGianTao;
    private int maSanPham;
    private int maDonHang;
    private int giaTienKichThuoc;
    private List<FoodOrderTopping> foodOrderToppingList = new ArrayList<FoodOrderTopping>();
    private List<FoodOrderSize> foodOrderSizeList = new ArrayList<FoodOrderSize>();

    public LichSuOrder(){

    }
    public LichSuOrder(LichSuOrder lichSuOrder) {
        this.maLichSuOrder = lichSuOrder.maLichSuOrder;
        this.soLuong = lichSuOrder.soLuong;
        this.thanhTien = lichSuOrder.thanhTien;
        this.ghiChu = lichSuOrder.ghiChu;
        this.kichThuoc = lichSuOrder.kichThuoc;
        this.tenSanPham = lichSuOrder.tenSanPham;
        this.topping = lichSuOrder.topping;
        this.thoiGianTao = lichSuOrder.thoiGianTao;
        this.maSanPham = lichSuOrder.maSanPham;
        this.maDonHang = lichSuOrder.maDonHang;
        this.giaTienKichThuoc = lichSuOrder.giaTienKichThuoc;
        this.foodOrderToppingList =  new ArrayList<FoodOrderTopping>(lichSuOrder.foodOrderToppingList);
        this.foodOrderSizeList =  new ArrayList<FoodOrderSize>(lichSuOrder.foodOrderSizeList);

    }

    public List<FoodOrderSize> getFoodOrderSizeList() {
        return foodOrderSizeList;
    }

    public void setFoodOrderSizeList(List<FoodOrderSize> foodOrderSizeList) {
        this.foodOrderSizeList = foodOrderSizeList;
    }

    public void setFoodOrderToppingList(List<FoodOrderTopping> foodOrderToppingList) {
        this.foodOrderToppingList = foodOrderToppingList;
    }
    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }
    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setMaLichSuOrder(int maLichSuOrder) {
        this.maLichSuOrder = maLichSuOrder;
    }
    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
    public void setGiaTienKichThuoc(int giaTienKichThuoc) {
        this.giaTienKichThuoc = giaTienKichThuoc;
    }
    public void setThanhTien(int thanhTien) {
        this.thanhTien = thanhTien;
    }
    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
    public void setKichThuoc(String kichThuoc) {
        this.kichThuoc = kichThuoc;
    }
    public void setTopping(String topping) {
        this.topping = topping;
    }
    public void setThoiGianTao(LocalDateTime thoigianTao) {
        this.thoiGianTao = thoiGianTao;
    }
    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }
    public void setMaDonHang(int maDonHang) {
        this.maDonHang = maDonHang;
    }
    public int getMaLichSuOrder() {
        return maLichSuOrder;
    }
    public int getSoLuong() {
        return soLuong;
    }
    public int getMaSanPham() {
        return maSanPham;
    }
    public int getGiaTienKichThuoc() {
        return giaTienKichThuoc;
    }
    public int getThanhTien() {
        return thanhTien;
    }
    public int getMaDonHang() {
        return maDonHang;
    }
    public String getGhiChu() {
        return ghiChu;
    }
    public String getKichThuoc() {
        return kichThuoc;
    }
    public String getTopping() {
        return topping;
    }
    public LocalDateTime getThoiGianTao() {
        return thoiGianTao;
    }

    public int getGiaTienTopping() {
        int tongTienTopping = 0;
        for (FoodOrderTopping i: foodOrderToppingList) {
            tongTienTopping += i.getGiaTien();
        }
        return tongTienTopping;
    }
    public List<FoodOrderTopping> getFoodOrderToppingList() {
        return foodOrderToppingList;
    }

    // overriding the compareTo method of Comparable class
    @Override public int compareTo(LichSuOrder comparestu) {
        int compareMaLichSuOrder
                = ((LichSuOrder)comparestu).getMaLichSuOrder();

        //  For Ascending order
        return this.maLichSuOrder - compareMaLichSuOrder;

        // For Descending order do like this
        // return compareage-this.studentage;
    }
}

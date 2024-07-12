package com.example.langthangcoffee.models;

import java.util.ArrayList;
import java.util.List;

public class FoodOrder {
    public static final int TYPE_CAFE = 1;
    public static final int TYPE_SODA = 2;
    public static final int TYPE_TRASUA = 3;
    public static final int TYPE_SWEETCAKE = 4;
    private String hinhAnh;
    private String tenDanhMuc;
    private int maDanhMuc;

    private int image;
    private String name;
    private int id;
    private int price;
    private int type;
    private boolean favorite;
    private String desc;
    private List<KichThuocSanPham> kichThuocSanPhamList = new ArrayList<>();
    private List<ToppingSanPham> toppingSanPhams = new ArrayList<>();

    public FoodOrder() {
    }
    public FoodOrder(FoodOrder foodOrder) {
        this.hinhAnh = foodOrder.hinhAnh;
        this.tenDanhMuc = foodOrder.tenDanhMuc;
        this.name = foodOrder.name;
        this.id = foodOrder.id;
        this.price = foodOrder.price;
        this.type = foodOrder.type;
        this.desc = foodOrder.desc;
        this.maDanhMuc = foodOrder.maDanhMuc;
        this.kichThuocSanPhamList = new ArrayList<>(foodOrder.kichThuocSanPhamList);
        this.toppingSanPhams = new ArrayList<>(foodOrder.toppingSanPhams);
    }

    public int getMaDanhMuc() {
        return maDanhMuc;
    }

    public void setMaDanhMuc(int maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }

    public List<KichThuocSanPham> getKichThuocSanPhamList() {
        return kichThuocSanPhamList;
    }

    public List<ToppingSanPham> getToppingSanPhams() {
        return toppingSanPhams;
    }

    public void setKichThuocSanPhamList(List<KichThuocSanPham> kichThuocSanPhamList) {
        this.kichThuocSanPhamList = kichThuocSanPhamList;
    }

    public void setToppingSanPhams(List<ToppingSanPham> toppingSanPhams) {
        this.toppingSanPhams = toppingSanPhams;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public FoodOrder(int image, String name, int price, int type, boolean favorite, String desc) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.type = type;
        this.favorite = favorite;
        this.desc = desc;
    }

    public int getID() {
        return id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int cost) {
        this.price = cost;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

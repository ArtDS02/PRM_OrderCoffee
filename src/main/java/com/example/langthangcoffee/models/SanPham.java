package com.example.langthangcoffee.models;

public class SanPham {

    public static final int TYPE_CAFE = 1;
    public static final int TYPE_TRASUA = 2;
    public static final int TYPE_SODA = 3;
private int maSanPham;
    private String tenSanPham;

    private int image;
    private String name;
    private String price;
    private int type;

    public SanPham(int image, String name, String price, int type)
    {
        this.image = image;
        this.name = name;
        this.price = price;
        this.type = type;

    }
    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setCost(String cost) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

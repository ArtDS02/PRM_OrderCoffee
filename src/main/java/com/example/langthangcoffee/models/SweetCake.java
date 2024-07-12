package com.example.langthangcoffee.models;

public class SweetCake {
    private int image;
    private String name;
    private String cost;
    public SweetCake(int image, String name, String cost)
    {
        this.image = image;
        this.name = name;
        this.cost = cost;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getCost() {
        return cost;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}

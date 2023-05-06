package com.example.mapy.models;

public class DashBoardModel {

    int image;
    String name;

    public DashBoardModel(int image, String name) {
        this.image = image;
        this.name = name;
    }

    public DashBoardModel() {
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
}

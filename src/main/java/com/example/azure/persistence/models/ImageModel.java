package com.example.azure.persistence.models;

public class ImageModel {

    private String img;

    public ImageModel(String img) {
        this.img = img;
    }

    public ImageModel() {}

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}

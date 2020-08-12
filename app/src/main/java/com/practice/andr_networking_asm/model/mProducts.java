package com.practice.andr_networking_asm.model;

public class mProducts {
    int id;
    String productName;
    double productPrice;
    String productCat;

    public mProducts() {
    }

    public mProducts(int id, String productName, double productPrice, String productCat) {
        this.id = id;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCat = productCat;
    }

    public mProducts(String productName, double productPrice, String productCat) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCat = productCat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductCat() {
        return productCat;
    }

    public void setProductCat(String productCat) {
        this.productCat = productCat;
    }
}

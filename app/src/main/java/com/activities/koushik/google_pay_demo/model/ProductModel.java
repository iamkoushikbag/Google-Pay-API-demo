package com.activities.koushik.google_pay_demo.model;

/**
 * Created by Mindtree on 9/4/2018.
 */
public class ProductModel {
    private String productName;
    private String productCode;
    private String quantity;
    private String price;
    private int imageUrl;

    public ProductModel(String productName, String productCode, String quantity, String price, int imageUrl) {
        this.productName = productName;
        this.productCode = productCode;
        this.quantity = quantity;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "ProductModel{" +
                "productName='" + productName + '\'' +
                ", productCode='" + productCode + '\'' +
                ", quantity='" + quantity + '\'' +
                ", price='" + price + '\'' +
                ", imageUrl=" + imageUrl +
                '}';
    }
}

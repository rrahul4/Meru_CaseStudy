package com.wipro.meru.main.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@JsonPropertyOrder({"productId","productName","productOffer","productDiscount","productPrice"})
public class Product {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int productId;
    private String productName;
    private boolean productOffer=true;
    private int productDiscount;
    private double productPrice;
    //TODO :to calculate offer price based on productOffer and productDiscount
    //private double productOfferPrice;

    public Product() {
    }


    public Product(int productId, String productName, boolean productOffer, int productDiscount, double productPrice) {
        this.productId = productId;
        this.productName = productName;
        this.productOffer = productOffer;
        this.productDiscount = productDiscount;
        this.productPrice = productPrice;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public boolean isProductOffer() {
        return productOffer;
    }

    public void setProductOffer(boolean productOffer) {
        this.productOffer = productOffer;
    }

    public int getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(int productDiscount) {
        this.productDiscount = productDiscount;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productOffer=" + productOffer +
                ", productDiscount=" + productDiscount +
                ", productPrice=" + productPrice +
                '}';
    }
}

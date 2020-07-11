package com.wipro.meru.main.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"productId","productName","productOffer","discount","productPrice","quantity", "supplierName"})
public class ProductView {

    private int productId;
    private String productName;
    private boolean productOffer;
    private int productDiscount;
    private double productPrice;
    
	private int productQuantity;
	private String productSupplierName;
	
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

	public int getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(int productQuantity) {
		this.productQuantity = productQuantity;
	}

	public String getProductSupplierName() {
		return productSupplierName;
	}

	public void setProductSupplierName(String productSupplierName) {
		this.productSupplierName = productSupplierName;
	}

	@Override
	public String toString() {
		return "ProductView [productId=" + productId + ", productName=" + productName + ", productOffer=" + productOffer
				+ ", productDiscount=" + productDiscount + ", productPrice=" + productPrice + ", productQuantity="
				+ productQuantity + ", productSupplierName=" + productSupplierName + "]";
	}
	
	
}

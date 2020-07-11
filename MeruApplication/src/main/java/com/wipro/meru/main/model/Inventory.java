package com.wipro.meru.main.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Inventory {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int productId;
	private int productQuantity;
	private String productSupplierName;
	

	public Inventory() {
		super();
	}


	public Inventory(int productId, int productQuantity, String productSupplierName) {
		super();
		this.productId = productId;
		this.productQuantity = productQuantity;
		this.productSupplierName = productSupplierName;
	}


	public int getProductId() {
		return productId;
	}


	public void setProductId(int productId) {
		this.productId = productId;
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
		return "Inventory [productId=" + productId + ", productQuantity=" + productQuantity + ", productSupplierName="
				+ productSupplierName + "]";
	}
	
}

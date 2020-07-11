package com.wipro.meru.main.model;

import java.util.List;

public class CustomerOrderView {
	
	private int orderId;	
	private String orderNumber;
    private String orderStatus;
    private List<OrderItemView> orderItemView;
    private double orderPrice;
    
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public List<OrderItemView> getOrderItemView() {
		return orderItemView;
	}
	public void setOrderItemView(List<OrderItemView> orderItemView) {
		this.orderItemView = orderItemView;
	}
	public double getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(double orderPrice) {
		this.orderPrice = orderPrice;
	}
	    
}

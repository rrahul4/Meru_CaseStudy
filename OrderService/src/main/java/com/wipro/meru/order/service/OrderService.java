package com.wipro.meru.order.service;

import java.util.List;
import java.util.Set;

import com.wipro.meru.order.entity.CustomerOrder;
import com.wipro.meru.order.entity.OrderItem;

public interface OrderService {

	CustomerOrder createOrder(Set<OrderItem> items);

	CustomerOrder getOrder(String orNumber);

	List<CustomerOrder> getOrders();

}

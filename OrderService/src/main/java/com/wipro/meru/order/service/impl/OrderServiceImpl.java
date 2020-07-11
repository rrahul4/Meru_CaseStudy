package com.wipro.meru.order.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.meru.order.constants.OrderConstants;
import com.wipro.meru.order.entity.CustomerOrder;
import com.wipro.meru.order.entity.OrderItem;
import com.wipro.meru.order.repo.OrderRepo;
import com.wipro.meru.order.service.OrderService;
import com.wipro.meru.order.util.OrderUtilities;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderRepo repo;
	
	@Override
	public CustomerOrder createOrder(Set<OrderItem> items) {
		CustomerOrder or=new CustomerOrder();
		or.setOrderNumber("ORDER"+OrderUtilities.getCurrentTimeStampInString());
		or.setStatus(OrderConstants.STATUS_ADD_CART);
		or.setItems(items);
		for(OrderItem item:items)item.setOrder(or);
		return repo.save(or);
	}
	
	@Override
	public CustomerOrder getOrder(String orNumber) {
		return repo.findByOrderNumber(orNumber);
	}
	
	@Override
	public List<CustomerOrder> getOrders() {
		return (List<CustomerOrder>)repo.findAll();
	}

}

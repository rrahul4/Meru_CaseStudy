package com.wipro.meru.order.controller;

import java.net.URI;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.wipro.meru.order.entity.CustomerOrder;
import com.wipro.meru.order.entity.OrderItem;
import com.wipro.meru.order.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	public OrderService serv;
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> createOrder(@RequestBody Set<OrderItem> items) {
		CustomerOrder newOr=serv.createOrder(items);
		// http://localhost:8080/employees/{id} -->savedEmployee.getEmployeeId()
		URI location=ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newOr.getOrderNumber()).toUri();
		return ResponseEntity.created(location).build();
		//return newOr;
	}
	
	@GetMapping("/{orderNumber}")
	public CustomerOrder getOrder(@PathVariable String orderNumber) {
		return serv.getOrder(orderNumber);
	}
	
	@GetMapping()
	public List<CustomerOrder> getOrders() {
		return serv.getOrders();
	}

}

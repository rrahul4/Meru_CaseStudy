package com.wipro.meru.main.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.wipro.meru.main.model.*;
import com.google.gson.Gson;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping("/Meru")
class OrderRestController {
	
	@Autowired
	private EurekaClient eurekaClient;
	
	@Autowired
	private RestTemplateBuilder restTemplateBuilder;
	
	HttpHeaders headers = new HttpHeaders();
	Gson gson = new Gson();
		
	@GetMapping(value="/Orders", produces = MediaType.APPLICATION_JSON_VALUE)
	@HystrixCommand(fallbackMethod="defaultOrderView")
	public String getOrders() {
		
		RestTemplate restTemplate = restTemplateBuilder.build();		
		InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("Zuul-Gateway", false);
		
		String baseUrl = instanceInfo.getHomePageUrl() + "/api/orderapp/orders";		
		ResponseEntity<CustomerOrder[]> response = restTemplate.getForEntity(baseUrl, CustomerOrder[].class);
		CustomerOrder[] customerOrderArray = response.getBody();
		List<CustomerOrder> customerOrderList = new ArrayList<CustomerOrder>(Arrays.asList(customerOrderArray));
		
		List<CustomerOrderView> customerOrderViewList = new ArrayList<CustomerOrderView>();

		for (CustomerOrder customerOrder : customerOrderList) {

			CustomerOrderView customerOrderView = new CustomerOrderView();
			customerOrderView.setOrderId(customerOrder.getOrderId());
			customerOrderView.setOrderNumber(customerOrder.getOrderNumber());
			customerOrderView.setOrderStatus(customerOrder.getStatus());
			
			List<OrderItemView> orderItemViewList = new ArrayList<OrderItemView>();
			
			String baseUrl2 = instanceInfo.getHomePageUrl() + "/api/productapp/inventory/products/{productId}";
			
			double orderPrice = 0;
			for (OrderItem orderItem : customerOrder.getItems())  {
				
				OrderItemView orderItemView = new OrderItemView();
				
				ResponseEntity<Product> response2 = restTemplate.getForEntity(baseUrl2, Product.class, orderItem.getProductId());
				Product product = response2.getBody();
				
				orderItemView.setItemId(orderItem.getItemId());
				orderItemView.setProductId(orderItem.getProductId());
				orderItemView.setProductName(product.getProductName());
				orderItemView.setProductPrice(product.getProductPrice());
				orderItemView.setProductDiscount(product.getProductDiscount());
				orderItemView.setProductQuantity(orderItem.getQuantity());
				
				orderItemViewList.add(orderItemView);
				
				double discount = 100 - product.getProductDiscount();					
				orderPrice = orderPrice + product.getProductPrice() * orderItem.getQuantity() * (discount / 100);
			
			}
			customerOrderView.setOrderItemView(orderItemViewList);
			customerOrderView.setOrderPrice(orderPrice);
			
			customerOrderViewList.add(customerOrderView);
		}
			
		return gson.toJson(customerOrderViewList);
		
	}

	@GetMapping(value="/Orders/{orderNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
	@HystrixCommand(fallbackMethod="defaultOrderView2")
	public String getOrder(@PathVariable String orderNumber) {
		
		RestTemplate restTemplate = restTemplateBuilder.build();		
		InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("Zuul-Gateway", false);
		
		String baseUrl = instanceInfo.getHomePageUrl() + "/api/orderapp/orders/{orderNumber}";		
		ResponseEntity<CustomerOrder> response = restTemplate.getForEntity(baseUrl, CustomerOrder.class, orderNumber);
		CustomerOrder customerOrder = response.getBody();
		
		CustomerOrderView customerOrderView = new CustomerOrderView();
		customerOrderView.setOrderId(customerOrder.getOrderId());
		customerOrderView.setOrderNumber(customerOrder.getOrderNumber());
		customerOrderView.setOrderStatus(customerOrder.getStatus());
		
		List<OrderItemView> orderItemViewList = new ArrayList<OrderItemView>();
		
		String baseUrl2 = instanceInfo.getHomePageUrl() + "/api/productapp/inventory/products/{productId}";			
		
		double orderPrice = 0;
		for (OrderItem orderItem : customerOrder.getItems())  {
			
			OrderItemView orderItemView = new OrderItemView();
			
			ResponseEntity<Product> response2 = restTemplate.getForEntity(baseUrl2, Product.class, orderItem.getProductId());
			Product product = response2.getBody();
			
			orderItemView.setItemId(orderItem.getItemId());
			orderItemView.setProductId(orderItem.getProductId());
			orderItemView.setProductName(product.getProductName());
			orderItemView.setProductPrice(product.getProductPrice());
			orderItemView.setProductDiscount(product.getProductDiscount());
			orderItemView.setProductQuantity(orderItem.getQuantity());
						
			orderItemViewList.add(orderItemView);
			
			double discount = 100 - product.getProductDiscount();					
			orderPrice = orderPrice + product.getProductPrice() * orderItem.getQuantity() * (discount / 100);

		}
		customerOrderView.setOrderItemView(orderItemViewList);
		customerOrderView.setOrderPrice(orderPrice);
		
		return gson.toJson(customerOrderView);
		
	}
	
	@PostMapping(value = "/Orders",  consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@HystrixCommand(fallbackMethod="defaultOrderView3")
	public String postProductCreate(@RequestBody List<OrderItem> orderItems) {

		RestTemplate restTemplate = restTemplateBuilder.build();		
		InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("Zuul-Gateway", false);
		
		String baseUrl = instanceInfo.getHomePageUrl() + "/api/inventoryapp/inventorys/{productId}";
		for (OrderItem orderItem : orderItems) {
						
			if (orderItem.getQuantity() > 0) {
				
				ResponseEntity<Inventory> response = restTemplate.getForEntity(baseUrl, Inventory.class, orderItem.getProductId());
				Inventory inventory = response.getBody();
				
				int quantity = inventory.getProductQuantity() - orderItem.getQuantity();
				
				if (quantity < 0) {
					return "Order Cancelled! ProductId " + inventory.getProductId() + " is having only " + inventory.getProductQuantity() + " in stock!";
				}
			}
		}

		String baseUrl2 = instanceInfo.getHomePageUrl() + "/api/orderapp/orders";		
		
		String itemsJson = gson.toJson(orderItems);
		
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request2 = new HttpEntity<>(itemsJson, headers);
		restTemplate.postForObject(baseUrl2, request2, String.class);

		for (OrderItem orderItem : orderItems) {

			if (orderItem.getQuantity() > 0) {
					
				ResponseEntity<Inventory> response = restTemplate.getForEntity(baseUrl, Inventory.class, orderItem.getProductId());
				Inventory inventory = response.getBody();

				int quantity = inventory.getProductQuantity() - orderItem.getQuantity();
					
				if (quantity >= 0) {
					inventory.setProductQuantity(quantity);				
					String inventoryJson = gson.toJson(inventory);
										
					HttpEntity<String> request = new HttpEntity<>(inventoryJson, headers);
					restTemplate.put(baseUrl, request, orderItem.getProductId());						
				}
			}
		}
		return "Success";

	}
	
	@SuppressWarnings("unused")
	private String defaultOrderView() {
		return "Meru Application - OrderService is Down! Sorry for Inconvenience!!";
	}
	
	@SuppressWarnings("unused")
	private String defaultOrderView2(String orderNumber) {
		return "Meru Application - OrderService is Down! Sorry for Inconvenience!!";
	}
	
	@SuppressWarnings("unused")
	private String defaultOrderView3(List<OrderItem> items) {
		return "Meru Application - OrderService is Down! Sorry for Inconvenience!!";
	}

}

package com.wipro.meru.main.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.wipro.meru.main.model.ProductView;
import com.wipro.meru.main.model.*;
import com.google.gson.Gson;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping("/Meru")
class ProductViewRestController {
	
	@Autowired
	private EurekaClient eurekaClient;
	
	@Autowired
	private RestTemplateBuilder restTemplateBuilder;
	
	HttpHeaders headers = new HttpHeaders();
	Gson gson = new Gson();
		
	@GetMapping(value="/ProductView", produces = MediaType.APPLICATION_JSON_VALUE)
	@HystrixCommand(fallbackMethod="defaultProductView")
	public String getProductView() {
		
		RestTemplate restTemplate = restTemplateBuilder.build();		
		InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("Zuul-Gateway", false);
		
		String baseUrl = instanceInfo.getHomePageUrl() + "/api/productapp/inventory/products";		
		ResponseEntity<Product[]> response = restTemplate.getForEntity(baseUrl, Product[].class);
		Product[] productArray = response.getBody();
		List<Product> productList = new ArrayList<Product>(Arrays.asList(productArray));

		baseUrl = instanceInfo.getHomePageUrl() + "/api/inventoryapp/inventorys/{productId}";		
		List<ProductView> productViewList = new ArrayList<ProductView>();
		
		for (Product product : productList) {
			
			ProductView productView = new ProductView();
			
			productView.setProductId(product.getProductId());
			productView.setProductName(product.getProductName());
			productView.setProductPrice(product.getProductPrice());
			productView.setProductDiscount(product.getProductDiscount());
			productView.setProductOffer(product.isProductOffer());
		
			ResponseEntity<Inventory> response2 = restTemplate.getForEntity(baseUrl, Inventory.class, product.getProductId());
			Inventory inventory = response2.getBody();
			
			productView.setProductQuantity(inventory.getProductQuantity());
			productView.setProductSupplierName(inventory.getProductSupplierName());
			
			productViewList.add(productView);
		}
				
		return gson.toJson(productViewList);
		
	}
	
	@GetMapping(value="/ProductView/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@HystrixCommand(fallbackMethod="defaultProductView2")
	public String getProductView2(@PathVariable int productId) {
	
		RestTemplate restTemplate = restTemplateBuilder.build();		
		InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("Zuul-Gateway", false);
		
		String baseUrl = instanceInfo.getHomePageUrl() + "/api/productapp/inventory/products/{productId}";			
		ResponseEntity<Product> response = restTemplate.getForEntity(baseUrl, Product.class, productId);
		Product product = response.getBody();
	
		baseUrl = instanceInfo.getHomePageUrl() + "/api/inventoryapp/inventorys/{productId}";
		ResponseEntity<Inventory> response2 = restTemplate.getForEntity(baseUrl, Inventory.class, productId);
		Inventory inventory = response2.getBody();
		
		ProductView productView = new ProductView();
					
		productView.setProductId(product.getProductId());
		productView.setProductName(product.getProductName());
		productView.setProductPrice(product.getProductPrice());
		productView.setProductDiscount(product.getProductDiscount());
		productView.setProductOffer(product.isProductOffer());
			
		productView.setProductQuantity(inventory.getProductQuantity());
		productView.setProductSupplierName(inventory.getProductSupplierName());
			
		return gson.toJson(productView);
		
	}
	
	@PostMapping(value = "/ProductView",  consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@HystrixCommand(fallbackMethod="defaultProductView3")
	@ResponseStatus(HttpStatus.CREATED)
	public String postProductCreate(@RequestBody List<ProductView> productViewList) {

		RestTemplate restTemplate = restTemplateBuilder.build();		
		InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("Zuul-Gateway", false);
		
		String baseUrl = instanceInfo.getHomePageUrl() + "/api/productapp/inventory/addProduct";		
		String baseUrl2 = instanceInfo.getHomePageUrl() + "/api/inventoryapp/inventorys";
		
		for (ProductView productView : productViewList) {
			Product product = new Product();
			product.setProductName(productView.getProductName());
			product.setProductPrice(productView.getProductPrice());
			product.setProductDiscount(productView.getProductDiscount());
			product.setProductOffer(productView.isProductOffer());
			String productJson = gson.toJson(product);
			
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> request = new HttpEntity<>(productJson, headers);
			restTemplate.postForObject(baseUrl, request, String.class);
	
			Inventory inventory = new Inventory();
			inventory.setProductQuantity(0);
			inventory.setProductSupplierName("NA");		
		
			if (productView.getProductQuantity() > 0) {
				inventory.setProductQuantity(productView.getProductQuantity());				
			}
			if (productView.getProductSupplierName() != null ) {
				inventory.setProductSupplierName(productView.getProductSupplierName());					
			}
			String inventoryJson = gson.toJson(inventory);
		
			HttpEntity<String> request2 = new HttpEntity<>(inventoryJson, headers);
			restTemplate.postForObject(baseUrl2, request2, String.class);
		}
		return "Success";
		
	}

	@PutMapping(value = "/ProductView/{productId}",  consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@HystrixCommand(fallbackMethod="defaultProductView4")
	public String putProductUpdate(@PathVariable int productId, @RequestBody ProductView productView) {
		
		RestTemplate restTemplate = restTemplateBuilder.build();		
		InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("Zuul-Gateway", false);
		
		String baseUrl = instanceInfo.getHomePageUrl() + "/api/productapp/inventory/products/{productId}";
		ResponseEntity<Product> response = restTemplate.getForEntity(baseUrl, Product.class, productId);
		Product product = response.getBody();
			
		if (productView.getProductName() != null) {
			product.setProductName(productView.getProductName());
		}
		if (productView.getProductPrice() > 0) {
			product.setProductPrice(productView.getProductPrice());
		}
		if (productView.getProductDiscount() >= 0) {
			product.setProductDiscount(productView.getProductDiscount());	    		
		}
		if (productView.isProductOffer() != product.isProductOffer() ) {
			product.setProductOffer(productView.isProductOffer());
		}
	
		String productJson = gson.toJson(product);
	    
	    headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<>(productJson, headers);
		restTemplate.put(baseUrl, request, productId);

		baseUrl = instanceInfo.getHomePageUrl() + "/api/inventoryapp/inventorys/{productId}";
		ResponseEntity<Inventory> response2 = restTemplate.getForEntity(baseUrl, Inventory.class, productId);
		Inventory inventory = response2.getBody();
		
		if (productView.getProductQuantity() > 0) {
			inventory.setProductQuantity(productView.getProductQuantity());				
		}
		if (productView.getProductSupplierName() != null ) {
			inventory.setProductSupplierName(productView.getProductSupplierName());					
		}
		String inventoryJson = gson.toJson(inventory);
		
		HttpEntity<String> request2 = new HttpEntity<>(inventoryJson, headers);
		restTemplate.put(baseUrl, request2, productId);	
		
		return "Success";
		
	}
	
	@DeleteMapping(value = "/ProductView/{productId}")
	@HystrixCommand(fallbackMethod="defaultProductView2")
	public String deleteProduct(@PathVariable int productId) {
	    
		RestTemplate restTemplate = restTemplateBuilder.build();		
		InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("Zuul-Gateway", false);
		
		String baseUrl = instanceInfo.getHomePageUrl() + "/api/productapp/inventory/products/{productId}";	    
		restTemplate.delete(baseUrl, productId);

		baseUrl = instanceInfo.getHomePageUrl() + "/api/inventoryapp/inventorys/{productId}";		
		restTemplate.delete(baseUrl, productId);	
		
		return "Success";
		
	}
		
	@SuppressWarnings("unused")
	private String defaultProductView() {
		return "Meru Application - ProductViewService is Down! Sorry for Inconvenience!!";
	}
	
	@SuppressWarnings("unused")
	private String defaultProductView2(int productId) {
		return "Meru Application - ProductViewService is Down! Sorry for Inconvenience!!";
	}
	
	@SuppressWarnings("unused")
	private String defaultProductView3(List<ProductView> productViewList) {
		return "Meru Application - ProductViewService is Down! Sorry for Inconvenience!!";
	}
	
	@SuppressWarnings("unused")
	private String defaultProductView4(int productId, ProductView productView) {
		return "Meru Application - ProductViewService is Down! Sorry for Inconvenience!!";
	}

}

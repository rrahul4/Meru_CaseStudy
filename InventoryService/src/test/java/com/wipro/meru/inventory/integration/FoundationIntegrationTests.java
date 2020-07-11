package com.wipro.meru.inventory.integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wipro.meru.inventory.InventoryServiceApplication;
import com.wipro.meru.inventory.entities.Inventory;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = InventoryServiceApplication.class, webEnvironment=WebEnvironment.RANDOM_PORT)
class FoundationIntegrationTests {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	TestRestTemplate restTemplate;

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
	
    @BeforeEach
    public void init(TestInfo testInfo) {
        System.out.println(" -- BEGIN " + testInfo.getDisplayName() + " -- ");
    
    }
    
    @AfterEach
    public void end() {
        System.out.println(" -- END -- ");
    
    }
        
	@Test
	@Order(1)
	public void test_createCustomer() {

		Inventory inventory1 = new Inventory(1, 100, "ABC");
		Inventory inventory2 = new Inventory(2, 200, "XYZ");
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		headers.set("Content-Type", "application/json");

		HttpEntity<Inventory> request1 = new HttpEntity<>(inventory1, headers);
		ResponseEntity<String> response1 = this.restTemplate.exchange(
									createURLWithPort("/inventorys"),
									HttpMethod.POST, request1, String.class);
		
		HttpEntity<Inventory> request2 = new HttpEntity<>(inventory2, headers);
		ResponseEntity<String> response2 = this.restTemplate.exchange(
									createURLWithPort("/inventorys"),
									HttpMethod.POST, request2, String.class);
		
		assertEquals(HttpStatus.CREATED.value(), response1.getStatusCodeValue());
		assertEquals(HttpStatus.CREATED.value(), response2.getStatusCodeValue());

	}
	
	@Test
	@Order(2)
	public void test_updateInventory() {
		
		Inventory inventory = new Inventory(1, 100, "ABC");
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		headers.set("Content-Type", "application/json");

		HttpEntity<Inventory> request = new HttpEntity<>(inventory, headers);
		ResponseEntity<String> response = this.restTemplate.exchange(
									createURLWithPort("/inventorys/1"),
									HttpMethod.PUT, request, String.class);
		
		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
		
	}

	@Test
	@Order(3)
	public void test_getInventory() {
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
				
		HttpEntity<Inventory> request = new HttpEntity<>(null, headers);
		ResponseEntity<String> response = this.restTemplate.exchange(
									createURLWithPort("/inventorys/1"),
									HttpMethod.GET, request, String.class);
		System.out.println(response.getBody());
		
		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
	}
	
	@Test
	@Order(4)
	public void test_getInventorys() {
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
				
		HttpEntity<Inventory> request = new HttpEntity<>(null, headers);
		ResponseEntity<String> response = this.restTemplate.exchange(
									createURLWithPort("/inventorys"),
									HttpMethod.GET, request, String.class);
		System.out.println(response.getBody());
		
		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
		
	}
			
	@Test
	@Order(10)
	public void test_deleteInventory() {
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
				
		HttpEntity<Inventory> request = new HttpEntity<>(null, headers);
		ResponseEntity<String> response = this.restTemplate.exchange(
									createURLWithPort("/inventorys/1"),
									HttpMethod.DELETE, request, String.class);
		
		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
		
	}
	
}

package com.wipro.meru.inventory.controller;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.google.gson.Gson;
import com.wipro.meru.inventory.controller.InventoryController;
import com.wipro.meru.inventory.entities.Inventory;
import com.wipro.meru.inventory.exceptions.InventoryNotFound;
import com.wipro.meru.inventory.service.InventoryService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(InventoryController.class)
@TestMethodOrder(OrderAnnotation.class)
class InventoryControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private InventoryService inventoryService;
	
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
	public void test_getInventorys() throws Exception {
		
		List<Inventory> inventoryList = new ArrayList<Inventory>();
		inventoryList.add(new Inventory(1, 100, "XYZ"));
		
		System.out.println(inventoryList);
		
		Mockito.when(inventoryService.getInventorys()).thenReturn(inventoryList);
		
		RequestBuilder request=MockMvcRequestBuilders
				.get("/inventorys")
				.accept(MediaType.APPLICATION_JSON);
	
		MvcResult result = mockMvc.perform(request)
				.andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		String expectedResult = "[{\"productId\":1,\"productQuantity\":100,\"productSupplierName\":\"XYZ\"}]";
		
		assertEquals(expectedResult, response.getContentAsString());
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		
	}

	@Test
	@Order(2)
	public void test_getInventory() throws Exception {
				
		Inventory inventory = new Inventory(1, 200, "ABC");		
		
		System.out.println(inventory);
				
		Mockito.when(inventoryService.getInventory(Mockito.anyInt())).thenReturn(inventory);
		
		RequestBuilder request=MockMvcRequestBuilders
				.get("/inventorys/1")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(request)
				.andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		String expectedResult = "{\"productId\":1,\"productQuantity\":200,\"productSupplierName\":\"ABC\"}";
		
		assertEquals(expectedResult, response.getContentAsString());
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		
	}
	
	@Test
	@Order(3)
	public void test_getInventory_negative() throws Exception {
		
		String exceptionMessage = "Inventory Not Found!!";
		
		Mockito.when(inventoryService.getInventory(Mockito.anyInt())).thenThrow(new InventoryNotFound(exceptionMessage));
		
		RequestBuilder request=MockMvcRequestBuilders
				.get("/inventorys/1")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(request)
				.andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
		
	}
	
	@Test
	@Order(4)
	public void test_createInventory() throws Exception {
				
		Inventory inventory = new Inventory(1,200, "ABC");
		System.out.println(inventory);
		
		Gson gson = new Gson();		
		String inventoryJson = gson.toJson(inventory);
		System.out.println(inventoryJson);
		
		Mockito.when(inventoryService.createInventory(Mockito.any(Inventory.class))).thenReturn(inventory);
		
		RequestBuilder request=MockMvcRequestBuilders
				.post("/inventorys")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(inventoryJson);
		
		MvcResult result = mockMvc.perform(request)
				.andReturn();
		
		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.CREATED.value(), response.getStatus());		
		
	}

	@Test
	@Order(5)
	public void test_updateInventory() throws Exception {
						
		Inventory inventory = new Inventory(1, 300, "DEF");
		System.out.println(inventory);
		
		Gson gson = new Gson();		
		String inventoryJson = gson.toJson(inventory);
		System.out.println(inventoryJson);
		
		Mockito.when(inventoryService.updateInventory(Mockito.anyInt(),Mockito.any(Inventory.class))).thenReturn(inventory);
		
		RequestBuilder request=MockMvcRequestBuilders
				.put("/inventorys/1")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(inventoryJson);
		
		MvcResult result = mockMvc.perform(request)
				.andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.OK.value(), response.getStatus());		
	
	}
	
	@Test
	@Order(6)
	public void test_updateInventory_negative() throws Exception {
				
		Inventory inventory = new Inventory(1, 200, "ABC");
		System.out.println(inventory);
		
		Gson gson = new Gson();		
		String inventoryJson = gson.toJson(inventory);
		System.out.println(inventoryJson);
		
		String exceptionMessage = "Customer Not Found!!";
		
		Mockito.when(inventoryService.updateInventory(Mockito.anyInt(),Mockito.any(Inventory.class))).thenThrow(new InventoryNotFound(exceptionMessage));
		
		RequestBuilder request=MockMvcRequestBuilders
				.put("/inventorys/1")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(inventoryJson);
		
		MvcResult result = mockMvc.perform(request)
				.andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
		
	}
	
	@Test
	@Order(7)
	public void test_deleteInventory() throws Exception {
				
		Inventory inventory = new Inventory(1, 200, "DEF");
		System.out.println(inventory);
		
		Mockito.when(inventoryService.deleteInventory(Mockito.anyInt())).thenReturn(inventory);
				
		RequestBuilder request=MockMvcRequestBuilders
				.delete("/inventorys/1")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(request)
				.andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.OK.value(), response.getStatus());		
				
	}
	
	@Test
	@Order(8)
	public void test_deleteInventory_negative() throws Exception {
		
		String exceptionMessage = "Inventory Not Found!!";
		
		Mockito.when(inventoryService.deleteInventory(Mockito.anyInt())).thenThrow(new InventoryNotFound(exceptionMessage));
		
		RequestBuilder request=MockMvcRequestBuilders
				.delete("/inventorys/1")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(request)
				.andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
		
	}
	
	
}

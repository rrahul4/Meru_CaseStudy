package com.wipro.meru.inventory.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wipro.meru.inventory.entities.Inventory;
import com.wipro.meru.inventory.exceptions.InventoryNotFound;
import com.wipro.meru.inventory.repositories.InventoryRepository;
import com.wipro.meru.inventory.service.InventoryServiceImpl;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest
class InventoryServiceTest {
	
	@InjectMocks 
	InventoryServiceImpl inventoryService;
	
	@Mock
	private InventoryRepository inventoryRepository;
	
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
	public void test_getInventory() {

		Optional<Inventory> mockInventory = Optional.of(new Inventory(1, 100, "ABC"));
		
		Mockito.when(inventoryRepository.findById(Mockito.anyInt())).thenReturn(mockInventory);
	
		Inventory inventoryFetched = inventoryService.getInventory(1);		
		System.out.println(inventoryFetched);

		assertEquals(mockInventory.get().getProductQuantity(), inventoryFetched.getProductQuantity());
		verify(inventoryRepository,atLeast(1)).findById(Mockito.anyInt());
		
	}
	
	@Test
	@Order(2)
	public void test_getInventory_negative() {

		String exceptionMessage = "Inventory Not Found!!";
		
		Mockito.when(inventoryRepository.findById(Mockito.anyInt())).thenThrow(new InventoryNotFound(exceptionMessage));
	
		Throwable exception = assertThrows(InventoryNotFound.class, () -> inventoryService.getInventory(1));
		
		assertEquals(exceptionMessage, exception.getMessage());
		verify(inventoryRepository,atLeast(1)).findById(Mockito.anyInt());
		
	}

	@Test
	@Order(3)
	public void test_getInventorys() {

		List<Inventory> inventoryList = new ArrayList<Inventory>();
		
		inventoryList.add(new Inventory(1, 100, "ABC"));
		inventoryList.add(new Inventory(2, 200, "XYZ"));
		
		Mockito.when(inventoryRepository.findAll()).thenReturn(inventoryList);
	
		List<Inventory> inventoryListFetched = inventoryService.getInventorys();
		Inventory inventoryFetched = inventoryListFetched.get(1);
		Inventory inventorySaved = inventoryList.get(1);
		
		System.out.println(inventoryFetched);

		assertEquals(inventorySaved.getProductQuantity(), inventoryFetched.getProductQuantity());
		verify(inventoryRepository,atLeast(1)).findAll();	
		
	}
	
	@Test
	@Order(4)
	public void test_getInventorys_negative() {

		List<Inventory> inventoryList = new ArrayList<Inventory>();
		
		Mockito.when(inventoryRepository.findAll()).thenReturn(inventoryList);
	
		List<Inventory> inventoryListFetched = inventoryService.getInventorys();
		
		System.out.println(inventoryListFetched);

		assertEquals(inventoryList, inventoryListFetched);
		verify(inventoryRepository,atLeast(1)).findAll();	
		
	}
	
	@Test
	@Order(5)
	public void test_createInventory() {
		
		Inventory inventory = new Inventory(1, 100, "XYZ");
		Inventory mockInventory = new Inventory(1, 200, "ABC");
		
		Mockito.when(inventoryRepository.save(Mockito.any(Inventory.class))).thenReturn(mockInventory);
	
		Inventory inventoryCreated = inventoryService.createInventory(inventory);
		
		System.out.println(inventoryCreated);

		assertEquals(mockInventory.getProductQuantity(), inventoryCreated.getProductQuantity());
		verify(inventoryRepository,atLeast(1)).save(Mockito.any(Inventory.class));	
		
	}
	
	@Test
	@Order(6)
	public void test_updateInventory() {

		Inventory inventory = new Inventory(1, 100, "XYZ");
		Optional<Inventory> mockInventory = Optional.of(new Inventory(1, 100, "XYZ"));
		
		Mockito.when(inventoryRepository.findById(Mockito.anyInt())).thenReturn(mockInventory);
		Mockito.when(inventoryRepository.save(Mockito.any(Inventory.class))).thenReturn(mockInventory.get());
	
		Inventory inventoryUpdated = inventoryService.updateInventory(1, inventory);
		
		System.out.println(inventoryUpdated);

		assertEquals(mockInventory.get().getProductQuantity(), inventoryUpdated.getProductQuantity());
		verify(inventoryRepository,atLeast(1)).save(Mockito.any(Inventory.class));
		
	}
	
	@Test
	@Order(7)
	public void test_updateInventory_negative() {

		Inventory inventory = new Inventory(1, 100, "XYZ");;
		
		String exceptionMessage = "Inventory Not Found!!";
		Mockito.when(inventoryRepository.findById(Mockito.anyInt())).thenThrow(new InventoryNotFound(exceptionMessage));
	
		Throwable exception = assertThrows(InventoryNotFound.class, () -> inventoryService.updateInventory(1, inventory));
		
		assertEquals(exceptionMessage, exception.getMessage());
		verify(inventoryRepository,atLeast(1)).findById(Mockito.anyInt());
		
	}
	
	@Test
	@Order(8)
	public void test_deleteInventory() {

		Optional<Inventory> mockInventory = Optional.of(new Inventory(1, 100, "XYZ"));
		
		Mockito.when(inventoryRepository.findById(Mockito.anyInt())).thenReturn(mockInventory);
	
		Inventory inventoryDeleted = inventoryService.deleteInventory(1);
		
		System.out.println(inventoryDeleted);

		assertEquals(mockInventory.get().getProductQuantity(), inventoryDeleted.getProductQuantity());
		verify(inventoryRepository,atLeast(1)).findById(Mockito.anyInt());
		
	}
	
	@Test
	@Order(9)
	public void test_deleteInventory_negative() {
		
		String exceptionMessage = "Inventory Not Found!!";
		Mockito.when(inventoryRepository.findById(Mockito.anyInt())).thenThrow(new InventoryNotFound(exceptionMessage));
	
		Throwable exception = assertThrows(InventoryNotFound.class, () -> inventoryService.deleteInventory(1));
		
		assertEquals(exceptionMessage, exception.getMessage());
		verify(inventoryRepository,atLeast(1)).findById(Mockito.anyInt());
		
	}
	
}

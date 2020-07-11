package com.wipro.meru.inventory.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.wipro.meru.inventory.entities.Inventory;
import com.wipro.meru.inventory.exceptions.InventoryNotFound;
import com.wipro.meru.inventory.service.InventoryService;

@RestController
public class InventoryController {
	
	@Autowired
	InventoryService inventoryService;
	
	@GetMapping("/inventorys")
	public List<Inventory> getInventorys() {
		return inventoryService.getInventorys();
		
	}

	@GetMapping("/inventorys/{productId}")
	public Inventory getCustomer(@PathVariable int productId) {
		Inventory inventory = inventoryService.getInventory(productId);
		if (inventory.getProductId() == 0) {
			throw new InventoryNotFound("ProductId --" + productId);
		}
		
		return inventory;	
	}
	
	@PostMapping("/inventorys")
	public ResponseEntity<Inventory> createInventory(@RequestBody Inventory inventory) {
		Inventory savedInventory = inventoryService.createInventory(inventory);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{productId}")
				.buildAndExpand(savedInventory.getProductId())
				.toUri();
		
		return ResponseEntity.created(location).build();
	}

	@PutMapping("/inventorys/{productId}")
	public void updateInventory(@PathVariable int productId, @RequestBody Inventory inventory) {
		
		Inventory updatedInventory = inventoryService.updateInventory(productId, inventory);
		
		if (updatedInventory.getProductId() == 0) {
			throw new InventoryNotFound("ProductId --" + productId);
		}
	}
	
	@DeleteMapping("/inventorys/{productId}")
	public void deleteInventory(@PathVariable int productId) {
		
		Inventory deletedInventory = inventoryService.deleteInventory(productId);
		if (deletedInventory.getProductId() == 0) {
			throw new InventoryNotFound("ProductId --" + productId);
		}	
	}
	
}

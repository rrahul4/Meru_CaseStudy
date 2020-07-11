package com.wipro.meru.inventory.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.wipro.meru.inventory.entities.Inventory;
import com.wipro.meru.inventory.exceptions.InventoryNotFound;
import com.wipro.meru.inventory.repositories.InventoryRepository;

//This Implementation will use CRUD Repository Layer, Actual Database

@Service
@Primary
public class InventoryServiceImpl implements InventoryService {
	
	@Autowired
	InventoryRepository inventoryRepository;
		
	@Override
	public Inventory getInventory(int productId) {
		
		Optional<Inventory> inventoryOpt = inventoryRepository.findById(productId);
		if (!inventoryOpt.isPresent()) {
			throw new InventoryNotFound("ProductId --" + productId);
		}
		
		Inventory inventory = inventoryOpt.get();
		return inventory;
	
	}

	@Override
	public List<Inventory> getInventorys() {
	
		List<Inventory> inventorys = (List<Inventory>) inventoryRepository.findAll();
		return inventorys;
	
	}

	@Override
	public Inventory createInventory(Inventory inventory) {
		
		Inventory inventorySaved = inventoryRepository.save(inventory);
		return inventorySaved;
		
	}

	@Override
	public Inventory updateInventory(int productId, Inventory inventory) {
		
		Optional<Inventory> inventoryOpt = inventoryRepository.findById(productId);
		if (!inventoryOpt.isPresent()) {
			throw new InventoryNotFound("ProductId --" +productId);
		}
	
		Inventory inventoryUpdated = inventoryOpt.get();
		
		if (inventory.getProductQuantity() >= 0 ) {
			inventoryUpdated.setProductQuantity(inventory.getProductQuantity());
		}
		
		if (inventory.getProductSupplierName() != null ) {
			inventoryUpdated.setProductSupplierName(inventory.getProductSupplierName());
		}
		
		inventoryRepository.save(inventoryUpdated);
		
		return inventoryUpdated;	

	}

	@Override
	public Inventory deleteInventory(int productId) {
		
		Optional<Inventory> inventoryOpt = inventoryRepository.findById(productId);
		if (!inventoryOpt.isPresent()) {
			throw new InventoryNotFound("ProductId --" +productId);
		}
		
		Inventory inventoryDeleted = inventoryOpt.get();
		
		inventoryRepository.delete(inventoryDeleted);
		
		return inventoryDeleted;
		
	}
	
}

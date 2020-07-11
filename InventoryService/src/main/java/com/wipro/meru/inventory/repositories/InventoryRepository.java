package com.wipro.meru.inventory.repositories;

import org.springframework.data.repository.CrudRepository;

import com.wipro.meru.inventory.entities.Inventory;

public interface InventoryRepository extends CrudRepository<Inventory, Integer> {
	
	
}

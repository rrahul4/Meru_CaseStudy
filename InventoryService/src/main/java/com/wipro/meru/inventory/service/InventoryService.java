package com.wipro.meru.inventory.service;

import java.util.List;

import com.wipro.meru.inventory.entities.Inventory;

public interface InventoryService {
	
	public Inventory getInventory(int productId);
	public List<Inventory> getInventorys();
	public Inventory createInventory(Inventory inventory);
	public Inventory updateInventory(int productId, Inventory Inventory);
	public Inventory deleteInventory(int productId);

}

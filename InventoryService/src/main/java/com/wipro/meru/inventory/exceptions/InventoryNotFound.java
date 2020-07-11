package com.wipro.meru.inventory.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class InventoryNotFound extends RuntimeException {

	public InventoryNotFound(String message) {

		super(message);
		// TODO Auto-generated constructor stub

	}
}

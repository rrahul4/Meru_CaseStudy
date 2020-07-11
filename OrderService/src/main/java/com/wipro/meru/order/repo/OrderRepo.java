package com.wipro.meru.order.repo;

import org.springframework.data.repository.CrudRepository;

import com.wipro.meru.order.entity.CustomerOrder;

public interface OrderRepo extends CrudRepository<CustomerOrder, Integer> {

	CustomerOrder findByOrderNumber(String orderNumber);

}

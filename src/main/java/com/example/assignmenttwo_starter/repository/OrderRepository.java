package com.example.assignmenttwo_starter.repository;

import com.example.assignmenttwo_starter.model.Orders;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Orders, Long> {
    @Query("SELECT o FROM Orders o WHERE o.customerId.customerId = ?1")
    public List<Orders> findByCustomerId(Integer customerId);

    public List<Orders> findAll();
}

package com.example.assignmenttwo_starter.service;

import com.example.assignmenttwo_starter.model.Orders;
import com.example.assignmenttwo_starter.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository oRepo;

    @Cacheable("ordersByCustId")
    public List<Orders> findByCustomerID(Integer id) { return oRepo.findByCustomerId(id); }

    @Cacheable("allOrders")
    public List<Orders> findAll() { return oRepo.findAll(); }

    public Optional<Orders> findByOrderID(Long id) { return oRepo.findById(id); }
}

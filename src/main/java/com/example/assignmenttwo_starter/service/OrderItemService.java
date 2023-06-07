package com.example.assignmenttwo_starter.service;

import com.example.assignmenttwo_starter.model.OrderItem;
import com.example.assignmenttwo_starter.model.Product;
import com.example.assignmenttwo_starter.repository.OrderItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemRepo oiRepo;

    @Cacheable("orderItemsByOrderID")
    public List<OrderItem> getOrderItemsByOrderID(List<Integer> orderIds) {
        return oiRepo.getOrderItemsByOrderID(orderIds);
    }

    @Cacheable("topProducts")
    public List<Product> getTopProducts() {
        return oiRepo.getTopProducts();
    }

    @Cacheable("allProducts")
    public List<Product> getAllProducts() { return oiRepo.getAllProducts(); }
}

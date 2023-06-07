package com.example.assignmenttwo_starter.repository;

import com.example.assignmenttwo_starter.model.OrderItem;
import com.example.assignmenttwo_starter.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepo extends CrudRepository<OrderItem, Integer> {
    @Query("SELECT oi FROM OrderItem oi WHERE oi.orderId.orderId in (?1)")
    public List<OrderItem> getOrderItemsByOrderID(List<Integer> orderIds);

    //select product_id,  count(product_id) from order_items group by product_id order by count(product_id) desc
    @Query("SELECT oi.productId, count(oi.productId) FROM OrderItem oi GROUP BY oi.productId order by count(oi.productId) desc")
    public List<Product> getTopProducts();

    @Query("SELECT oi.productId FROM OrderItem oi")
    public List<Product> getAllProducts();

}

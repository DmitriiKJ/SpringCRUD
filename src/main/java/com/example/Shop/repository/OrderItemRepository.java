package com.example.Shop.repository;

import com.example.Shop.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Modifying
    @Query(value = "DELETE FROM order_item WHERE product_id = :id", nativeQuery = true)
    public void deleteByProductId(@Param("id")Long id);

//    @Modifying
//    @Query(value = "DELETE FROM order_demo WHERE id IN (SELECT order_id FROM order_item WHERE product_id = :id)", nativeQuery = true)
//    void deleteOrderItemRelations(@Param("id") Long id);
}

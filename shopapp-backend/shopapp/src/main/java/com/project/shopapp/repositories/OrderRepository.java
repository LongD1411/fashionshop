package com.project.shopapp.repositories;

import com.project.shopapp.entities.Order;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);

    @Query("SELECT o FROM Order o WHERE " + "(:keyword IS NULL OR :keyword = '' OR o.phoneNumber LIKE %:keyword% OR o.fullName LIKE %:keyword% OR o.email LIKE %:keyword%)")
    Page<Order> searchOrders(Pageable pageable,@Param("keyword") String keyword);
}

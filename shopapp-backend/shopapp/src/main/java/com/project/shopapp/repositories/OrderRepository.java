package com.project.shopapp.repositories;

import com.project.shopapp.entities.Order;
import com.project.shopapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}

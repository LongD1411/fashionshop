package com.project.shopapp.repositories;

import com.project.shopapp.entities.Product;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProductRepository  extends JpaRepository<Product, Long> {
    boolean existsByName(String  name);
    @Query("SELECT p FROM Product p WHERE " +
            "(:categoryId IS NULL OR :categoryId = 0 OR p.category.id = :categoryId) AND " +
            "(:keyword IS NULL OR :keyword = '' OR p.name LIKE %:keyword% OR p.description LIKE %:keyword%)")
    Page<Product> searchProduct(Pageable pageable, @Param("categoryId") Long categoryId,
                                @Param("keyword") String keyword);

    Page<Product> findAll(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.id IN :ids")
    List<Product> findAllOrderProduct(@Param("ids") List<Long> ids);

    @Query(value = "SELECT * FROM products ORDER BY created_at DESC LIMIT 8", nativeQuery = true)
    List<Product> findTop8ProductOrderByUpdatedDate();

    @Query(value = "SELECT * FROM products ORDER BY created_at DESC LIMIT 4", nativeQuery = true)
    List<Product> findTop4ProductOrderByUpdatedDate();
}

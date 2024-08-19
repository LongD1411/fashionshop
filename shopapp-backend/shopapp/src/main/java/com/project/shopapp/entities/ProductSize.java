package com.project.shopapp.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_size")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "size_id")
    private Size size;

    private Integer quantity;  // Số lượng sản phẩm trong kho

}

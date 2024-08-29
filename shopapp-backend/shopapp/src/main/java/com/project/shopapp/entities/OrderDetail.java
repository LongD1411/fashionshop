package com.project.shopapp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_details")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private  Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private  Product product;
    
    @ManyToOne
    @JoinColumn(name = "size_id")
    private  Size size;

    @Column(name = "price", nullable = false)
    private Float price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "number_of_products", nullable = false)
    private Integer numberOfProducts;

    @Column(name = "total_money", nullable = false)
    private Float totalMoney;

}

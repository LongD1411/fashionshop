package com.project.shopapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.shopapp.dtos.ProductDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 350)
    private String name;

    private float price;

    @Column(name = "old_price")
    private float oldPrice;

    @Column(name = "thumbnail", length = 300)
    private String thumbnail;

    @Column(name = "description")
    private String description;

    @JoinColumn(name = "category_id")
    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<ProductSize> productSizes;

    private String sku;// mã sản phẩm

    public static Product toProduct(Product product,ProductDTO productDTO) {
       product.setName(productDTO.getName());
       product.setOldPrice(productDTO.getOldPrice());
       product.setPrice(productDTO.getPrice());
       product.setDescription(productDTO.getDescription());
       return  product;
    }

}

package com.project.shopapp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "size")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "size_code")

    private String sizeCode;
    @Column(name = "size_name")
    private String sizeName;

    @ManyToMany(mappedBy = "sizes")
    private List<Product> products = new ArrayList<>();

}

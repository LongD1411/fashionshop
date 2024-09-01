package com.project.shopapp.entities;

import jakarta.persistence.*;
import lombok.*;

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
}

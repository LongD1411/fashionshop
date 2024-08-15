package com.project.shopapp.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "banner")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name= "thumbnail")
    private String thumbnail;
    @Column(name="title")
    private String title;
    @Column(name= "description")
    private String description;
}

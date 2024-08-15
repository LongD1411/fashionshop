package com.project.shopapp.repositories;

import com.project.shopapp.entities.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRespository extends JpaRepository<Banner, Long> {
}

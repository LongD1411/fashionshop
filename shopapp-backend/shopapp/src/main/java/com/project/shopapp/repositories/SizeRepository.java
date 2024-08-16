package com.project.shopapp.repositories;

import com.project.shopapp.entities.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.*;

public interface SizeRepository  extends JpaRepository<Size,Long> {
}

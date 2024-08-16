package com.project.shopapp.services;

import com.project.shopapp.entities.Size;

import java.util.List;

public interface ISizeService {
    List<Size> getAllSize();
    List<Size> getAllSizeOfProduct(Long productId);
}

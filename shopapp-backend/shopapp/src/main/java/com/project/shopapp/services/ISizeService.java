package com.project.shopapp.services;

import com.project.shopapp.dtos.request.SizeDTO;
import com.project.shopapp.entities.Size;

import java.util.List;

public interface ISizeService {
    List<Size> getAllSize();
    List<Size> getAllSizeOfProduct(Long productId);
    Size getSize(Long id);
    Size createSize(SizeDTO sizeDTO);
    void deleteSize(Long id);
    Size updateSize(SizeDTO sizeDTO) ;
}

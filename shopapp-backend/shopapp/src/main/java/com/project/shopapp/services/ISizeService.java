package com.project.shopapp.services;

import com.project.shopapp.dtos.SizeDTO;
import com.project.shopapp.entities.Size;

import java.util.List;

public interface ISizeService {
    List<Size> getAllSize();
    List<Size> getAllSizeOfProduct(Long productId);
    Size getSize(Long id) throws  Exception;
    Size createSize(SizeDTO sizeDTO) throws  Exception;
    void deleteSize(Long id);
    Size updateSize(SizeDTO sizeDTO) throws Exception ;
}

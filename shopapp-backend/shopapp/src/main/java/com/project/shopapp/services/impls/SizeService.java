package com.project.shopapp.services.impls;

import com.project.shopapp.entities.Size;
import com.project.shopapp.repositories.SizeRepository;
import com.project.shopapp.services.ISizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class SizeService implements ISizeService {
    private  final SizeRepository sizeRepository;
    @Override
    public List<Size> getAllSize() {
        return sizeRepository.findAll();
    }

    @Override
    public List<Size> getAllSizeOfProduct(Long productId) {
         return null;
    }
}

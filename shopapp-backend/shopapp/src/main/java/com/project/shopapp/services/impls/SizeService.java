package com.project.shopapp.services.impls;

import com.nimbusds.jose.proc.SecurityContext;
import com.project.shopapp.dtos.request.SizeDTO;
import com.project.shopapp.entities.Size;
import com.project.shopapp.exceptions.AppException;
import com.project.shopapp.exceptions.ErrorCode;
import com.project.shopapp.repositories.SizeRepository;
import com.project.shopapp.services.ISizeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SizeService implements ISizeService {
    private static final Logger log = LoggerFactory.getLogger(SizeService.class);
    private final SizeRepository sizeRepository;

    @Override
    public List<Size> getAllSize() {
        return sizeRepository.findAll();
    }

    @Override
    public List<Size> getAllSizeOfProduct(Long productId) {
        return null;
    }

    @Override
    public Size getSize(Long id) {
        return sizeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_EXISTED));
    }

    @Override
    public Size createSize(SizeDTO sizeDTO) {
        Size size = new Size().builder()
                .sizeCode(sizeDTO.getSizeCode())
                .sizeName(sizeDTO.getSizeName()).build();
        return sizeRepository.save(size);
    }

    @Override
    @Transactional
    public void deleteSize(Long id){
            sizeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Size updateSize(SizeDTO sizeDTO){
        Size size = sizeRepository.findById(sizeDTO.getSizeId()).orElseThrow(()-> new AppException(ErrorCode.SIZE_NOT_EXISTED));
        size.setSizeName(sizeDTO.getSizeName());
        size.setSizeCode(sizeDTO.getSizeCode());
        return  sizeRepository.save(size);
    }
}

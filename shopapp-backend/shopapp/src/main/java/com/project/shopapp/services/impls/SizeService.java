package com.project.shopapp.services.impls;

import com.project.shopapp.dtos.SizeDTO;
import com.project.shopapp.entities.Size;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.repositories.SizeRepository;
import com.project.shopapp.services.ISizeService;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintDeclarationException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.sqm.mutation.internal.temptable.AbstractDeleteExecutionDelegate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SizeService implements ISizeService {
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
    public Size getSize(Long id) throws Exception {
        Size size = sizeRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Không tìm thấy size"));
        return size;
    }

    @Override
    public Size createSize(SizeDTO sizeDTO) throws Exception {
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
    public Size updateSize(SizeDTO sizeDTO) throws Exception{
        Size size = sizeRepository.findById(sizeDTO.getSizeId()).orElseThrow(()-> new DataNotFoundException("Không tìm thấy size"));
        size.setSizeName(sizeDTO.getSizeName());
        size.setSizeCode(sizeDTO.getSizeCode());
        return  sizeRepository.save(size);
    }
}

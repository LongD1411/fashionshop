package com.project.shopapp.controllers;

import com.project.shopapp.dtos.request.SizeDTO;
import com.project.shopapp.dtos.respone.ApiResponse;
import com.project.shopapp.entities.Category;
import com.project.shopapp.entities.Size;
import com.project.shopapp.respone.Message;
import com.project.shopapp.services.ISizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "${api.prefix}")
@RequiredArgsConstructor
public class SizeController {
    private final ISizeService iSizeService;
    @GetMapping("/public/size")
    public ApiResponse<Size> getAllSize(@RequestParam(value = "id", required = false) Long id) {
        if (id != null) {
            Size sizes = iSizeService.getSize(id);
            return ApiResponse.<Size>builder().result(sizes).build();
        } else {
            List<Size> sizes = iSizeService.getAllSize();
            return ApiResponse.<Size>builder().results(sizes).build();
        }
    }

    @PostMapping("/size")
    public ApiResponse<Size> createSize(@RequestBody SizeDTO sizeDTO){

            Size size = iSizeService.createSize(sizeDTO);
            return ApiResponse.<Size>builder().result(size).build();
    }
    @DeleteMapping("/size")
    public ApiResponse<Void> deleteSize(@RequestParam(value = "id") Long id){
        iSizeService.deleteSize(id);
        return ApiResponse.<Void>builder().message("Xóa thành công").build();
    }
    @PutMapping("/size")
    public ResponseEntity<?> updateSize(@RequestBody SizeDTO sizeDTO){
           Size size = iSizeService.updateSize(sizeDTO);
           return ResponseEntity.ok(size);
       }
    }


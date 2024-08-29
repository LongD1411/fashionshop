package com.project.shopapp.controllers;

import com.project.shopapp.dtos.SizeDTO;
import com.project.shopapp.entities.Size;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.respone.Message;
import com.project.shopapp.services.ISizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "${api.prefix}/size")
@RequiredArgsConstructor
public class SizeController {
    private final ISizeService iSizeService;
    @GetMapping("")
    public ResponseEntity<?> getAllSize() {
        try {
            List<Size> sizes = iSizeService.getAllSize();
            return ResponseEntity.ok(sizes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.builder().message(e.getMessage()).build());
        }
    }
    @GetMapping("/one")
    public ResponseEntity<?> getSize(@RequestParam("id") Long id){
        try {
            Size size = iSizeService.getSize(id);
            return ResponseEntity.ok(size);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.builder().message(e.getMessage()).build());
        }
    }
    @PostMapping("")
    public ResponseEntity<?> createSize(@RequestBody SizeDTO sizeDTO){
        try {
            Size size = iSizeService.createSize(sizeDTO);
            return ResponseEntity.ok(size);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.builder().message(e.getMessage()).build());
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteSize(@RequestParam("id") Long id){
        try {
             iSizeService.deleteSize(id);
            return ResponseEntity.ok(Message.builder().message("Xóa thành công").build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.builder().message("Không thể xóa size").build());
        }
    }
    @PutMapping("")
    public ResponseEntity<?> updateSize(@RequestBody SizeDTO sizeDTO){
       try{
           Size size = iSizeService.updateSize(sizeDTO);
           return ResponseEntity.ok(size);
       }
       catch (DataNotFoundException e){
           return ResponseEntity.badRequest().body(Message.builder().message(e.getMessage()).build());
       }catch (Exception e){
           return ResponseEntity.badRequest().body(Message.builder().message("Không thể cập nhật").build());
       }
    }
//    @GetMapping
//    public ResponseEntity<?> getAllSizeOfProduct(){
//    }
}

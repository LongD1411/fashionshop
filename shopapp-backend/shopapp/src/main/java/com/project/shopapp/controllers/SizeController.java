package com.project.shopapp.controllers;

import com.project.shopapp.entities.Size;
import com.project.shopapp.respone.Message;
import com.project.shopapp.services.ISizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
//    @GetMapping
//    public ResponseEntity<?> getAllSizeOfProduct(){
//    }
}

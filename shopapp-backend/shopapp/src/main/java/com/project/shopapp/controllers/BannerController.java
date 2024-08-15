package com.project.shopapp.controllers;

import com.project.shopapp.dtos.BannerDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.entities.Banner;
import com.project.shopapp.entities.ProductImage;
import com.project.shopapp.respone.BannerResponse;
import com.project.shopapp.respone.Message;
import com.project.shopapp.respone.ProductResponse;
import com.project.shopapp.services.IBannerService;
import com.project.shopapp.statics.Image;
import com.project.shopapp.utils.ImageUtil;
import com.project.shopapp.utils.MessageKey;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.project.shopapp.statics.Image.MAXIMUM_IMAGES;

@RestController
@RequestMapping(value = "${api.prefix}/banner")
@RequiredArgsConstructor
public class BannerController {
    private  final IBannerService iBannerService;
    @GetMapping("")
    public ResponseEntity<?> getAllBanner(){
        try {
            List<Banner> bannerResponses = iBannerService.getAllBanner();
            return ResponseEntity.ok(bannerResponses);
        }catch (Exception e){
            return  ResponseEntity.badRequest().body(Message.builder().message(e.getMessage()).build());
        }
    }
    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createBanner(@RequestParam("file") MultipartFile file,
                                          @RequestParam("title") String title,
                                          @RequestParam("description") String description) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Message.builder().message("You must upload an image").build());
            }
            // Kiểm tra kích thước file và định dạng
            ResponseEntity<?> fileCheckResult = ImageUtil.checkImage(file);
            if (fileCheckResult != null) {
                return fileCheckResult;
            }
            // Lưu file và cập nhật thumbnail trong dto
            String fileName = ImageUtil.storeFile(file);

            BannerDTO bannerDTO = new BannerDTO();
            bannerDTO.setTitle(title);
            bannerDTO.setDescription(description);
            bannerDTO.setThumbnail(fileName);
            // Save the BannerDTO to the database
            Banner savedBanner = iBannerService.createBanner(bannerDTO);
            return ResponseEntity.ok(savedBanner);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.builder().message(e.getMessage()).build());
        }
    }
}

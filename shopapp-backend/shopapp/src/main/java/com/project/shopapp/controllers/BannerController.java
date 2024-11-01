package com.project.shopapp.controllers;

import com.project.shopapp.dtos.request.BannerDTO;
import com.project.shopapp.dtos.respone.ApiResponse;
import com.project.shopapp.dtos.respone.BannerResponse;
import com.project.shopapp.entities.Banner;
import com.project.shopapp.exceptions.AppException;
import com.project.shopapp.exceptions.ErrorCode;
import com.project.shopapp.respone.Message;
import com.project.shopapp.services.IBannerService;
import com.project.shopapp.utils.ImageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "${api.prefix}")
@RequiredArgsConstructor
public class BannerController {
    private  final IBannerService iBannerService;
    @GetMapping("/public/banner")
    public ApiResponse<Banner> getBanner(@RequestParam(value = "id", required = false) Long id){
            if(id!=null){
                Banner banner = iBannerService.getBanner(id);
                return ApiResponse.<Banner>builder().result(banner).build();
            }else {
                List<Banner> banners = iBannerService.getAllBanner();
                return ApiResponse.<Banner>builder().results(banners).build();
            }
    }

    @PostMapping(value = "/banner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Banner> createBanner(@RequestPart(value = "file",required = false) MultipartFile file,
                                                   @Valid @RequestPart("banner") BannerDTO bannerDTO) throws Exception {
            if (file.isEmpty()) {
                throw new AppException(ErrorCode.MISSING_REQUIRED_DATA);
            }
            // Kiểm tra kích thước file và định dạng
            ImageUtil.checkImage(file);

            // Lưu file và cập nhật thumbnail trong dto
            String fileName = ImageUtil.storeFile(file);

            bannerDTO.setThumbnail(fileName);

            // Save the BannerDTO to the database
            Banner savedBanner = iBannerService.createBanner(bannerDTO);
            return ApiResponse.<Banner>builder().result(savedBanner).build();
    }
    @DeleteMapping("/banner")
    public ResponseEntity<?> deleteBanner(@RequestParam("id") Long id) throws Exception {
            iBannerService.deleteBanner(id);
            return  ResponseEntity.ok(Message.builder().message("Xóa thành công").build());
    }
    @PutMapping("/banner")
        public ApiResponse<Banner> updateBanner(@Valid @RequestPart(value = "banner") BannerDTO bannerDTO,
                                          @RequestPart(value = "file",required = false) MultipartFile file) throws Exception {

                if(file != null) {
                    // Kiểm tra kích thước file và định dạng
                    ImageUtil.checkImage(file);
                    // Lưu file và cập nhật thumbnail trong dto
                    String fileName = ImageUtil.storeFile(file);
                    bannerDTO.setThumbnail(fileName);
                }
            // update  Banner to the database
            Banner savedBanner = iBannerService.updateBanner(bannerDTO);
            return ApiResponse.<Banner>builder().result(savedBanner).build();
    }
}

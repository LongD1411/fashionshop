package com.project.shopapp.controllers;

import com.project.shopapp.componens.LocalizationUtils;
import com.project.shopapp.dtos.request.CategoryDTO;
import com.project.shopapp.dtos.respone.ApiResponse;
import com.project.shopapp.entities.Category;
import com.project.shopapp.respone.Message;
import com.project.shopapp.services.impls.CategoryService;
import com.project.shopapp.utils.ImageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
//@Validated
@RequestMapping(value = "${api.prefix}")
@RequiredArgsConstructor
public class CategoryController {
    private final LocalizationUtils localizationUtils;
    private final CategoryService categoryService;

    @PostMapping("/categories")
    public ApiResponse<Category> createCategory(@Valid @RequestPart(value = "name") CategoryDTO categoryDTO,
                                      @RequestPart(value = "file") MultipartFile file) throws IOException {
                //kiểm tra image size và định dạng
                ImageUtil.checkImage(file);
                // Xử lý và lưu ảnh
                String imagePath = ImageUtil.storeFile(file);
                categoryDTO.setThumbnail(imagePath);
            Category category = categoryService.createCategory(categoryDTO);
            return ApiResponse.<Category>builder().result(category).build();
    }

    @GetMapping("/public/categories")// http://localhost:8088/api/v1/categories?page=1&limit=1
    public ApiResponse<Category> getAllCategories(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                              @RequestParam(value = "page", required = false, defaultValue = "10") int limit,
                                              @RequestParam(value = "id", required = false) Long id) {
            if (id != null) {
                Category category = categoryService.getCategoryById(id);
                return ApiResponse.<Category>builder().result(category).build();
            } else {
                List<Category> categories = categoryService.getAllCategories();// Lấy tất đ cần phân trang
                return ApiResponse.<Category>builder().results(categories).build();
            }
    }


    @PutMapping("/categories")
    public ApiResponse<Category> updateCategory(@RequestPart(value = "file",required = false) MultipartFile file,
                                                @Valid @RequestPart("category") CategoryDTO categoryDTO) throws Exception {
           if (file != null) {
               //kiểm tra image size và định dạng
               ImageUtil.checkImage(file);
           }
          Category category = categoryService.updateCategory(file,categoryDTO);
           return ApiResponse.<Category>builder().result(category).build();
    }

    @DeleteMapping("/categories")
    public ResponseEntity<?> deleteCategories(@RequestParam Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(Message.builder().message("Xóa thành công").build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.builder().message(e.getMessage()).build());
        }
    }
}

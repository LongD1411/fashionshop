package com.project.shopapp.controllers;

import com.project.shopapp.componens.LocalizationUtils;
import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.entities.Category;
import com.project.shopapp.respone.Message;
import com.project.shopapp.services.impls.CategoryService;
import com.project.shopapp.statics.Image;
import com.project.shopapp.utils.ImageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
//@Validated
@RequestMapping(value = "${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final LocalizationUtils localizationUtils;
    private final CategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<?> createCategory(@RequestPart(value = "name", required = false) CategoryDTO categoryDTO,
                                            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            if (categoryDTO.getName() == null) {
                return ResponseEntity.badRequest().body(Message.builder().message("Category name can be null").build());
            }
            if (file != null) {
                //kiểm tra image size và định dạng
                ResponseEntity<?> fileCheckResult = ImageUtil.checkImage(file);
                if (fileCheckResult != null) {
                    return fileCheckResult; // return về null nếu hợp lệ, return về error nếu vi phạm
                }
                if (!file.isEmpty()) {
                    // Xử lý và lưu ảnh
                    String imagePath = ImageUtil.storeFile(file);
                    categoryDTO.setThumbnail(imagePath);
                }
            }
            Category category = categoryService.createCategory(categoryDTO);
            return ResponseEntity.ok(category);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.builder().message("Error creating category: " + e.getMessage()).build());
        }
    }

    @GetMapping("")// http://localhost:8088/api/v1/categories?page=1&limit=1
    public ResponseEntity<?> getAllCategories(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                              @RequestParam(value = "page", required = false, defaultValue = "10") int limit,
                                              @RequestParam(value = "id", required = false) Long id) {
        try {
            if (id != null) {
                Category category = categoryService.getCategoryById(id);
                return ResponseEntity.ok(category);
            } else {
                List<Category> categories = categoryService.getAllCategories();// Lấy tất đ cần phân trang
                return ResponseEntity.ok(categories);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.builder().message(e.getMessage()).build());
        }
    }


    @PutMapping("")
    public ResponseEntity<?> updateCategory(@RequestPart(value = "file",required = false) MultipartFile file,
                                                 @RequestPart("category") CategoryDTO categoryDTO) {
       try{
           if (file != null) {
               //kiểm tra image size và định dạng
               ResponseEntity<?> fileCheckResult = ImageUtil.checkImage(file);
               if (fileCheckResult != null) {
                   return fileCheckResult; // return về null nếu hợp lệ, return về error nếu vi phạm
               }
           }
          Category category = categoryService.updateCategory(file,categoryDTO);
           return ResponseEntity.ok(category);
       }catch (Exception e) {
           return ResponseEntity.badRequest().body(Message.builder().message("Cập nhật không thành công").build());
       }
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteCategories(@RequestParam Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(Message.builder().message("Xóa thành công").build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.builder().message(e.getMessage()).build());
        }
    }
}

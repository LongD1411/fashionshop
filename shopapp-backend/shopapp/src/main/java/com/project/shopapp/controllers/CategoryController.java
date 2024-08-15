package com.project.shopapp.controllers;

import com.project.shopapp.componens.LocalizationUtils;
import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.entities.Category;
import com.project.shopapp.respone.Message;
import com.project.shopapp.services.impls.CategoryService;
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
    public ResponseEntity<?> createCategory(@RequestParam(value = "name", required = false) String name,
                                            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            if(name == null || name.isEmpty()){
                return ResponseEntity.badRequest().body(Message.builder().message("Error creating category: name is blank").build());
            }
            if( file == null ||file.isEmpty()){
                return ResponseEntity.badRequest().body(Message.builder().message("Error creating category: image is blank").build());
            }
            //kiểm tra image size và định dạng
            ResponseEntity<?> fileCheckResult = ImageUtil.checkImage(file);
            if (fileCheckResult != null) {
                return fileCheckResult; // return về null nếu hợp lệ, return về error nếu vi phạm
            }
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setName(name);
            if (!file.isEmpty()) {
                // Xử lý và lưu ảnh
                String imagePath = ImageUtil.storeFile(file);
                categoryDTO.setThumbnail(imagePath);
            }
            Category category = categoryService.createCategory(categoryDTO);
            return ResponseEntity.ok(category);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.builder().message("Error creating category: " + e.getMessage()).build());
        }
    }

    @GetMapping("")// http://localhost:8088/api/v1/categories?page=1&limit=1
    public ResponseEntity<List<Category>> getAllCategories(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                   @RequestParam(value = "page", required = false, defaultValue = "10") int limit) {
        List<Category> categories =  categoryService.getAllCategories();// Lấy tất đ cần phân trang
        return ResponseEntity.ok(categories);
    }


    @GetMapping("/{id}")
    public ResponseEntity<String> getCategory(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok("category");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@Valid @PathVariable Long id,
                                                 @RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(id,categoryDTO);
        return ResponseEntity.ok("Update category successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategories(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Delete category with id: " + id + " successfully");

    }
}

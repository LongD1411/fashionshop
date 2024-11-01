package com.project.shopapp.services;

import com.project.shopapp.dtos.request.CategoryDTO;
import com.project.shopapp.entities.Category;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Service
public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);
    Category getCategoryById(Long id);
    List<Category> getAllCategories();
    Category updateCategory(MultipartFile file, CategoryDTO categoryDTO) throws Exception;
    void deleteCategory(long id) throws  Exception;

}

package com.project.shopapp.services.impls;

import com.project.shopapp.componens.CategoryDeletedEvent;
import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.entities.Category;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.repositories.CategoryRepository;
import com.project.shopapp.services.ICategoryService;
import com.project.shopapp.statics.Image;
import com.project.shopapp.utils.ImageUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    @Override
    @Transactional
    public Category createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        if (categoryDTO.getThumbnail() != null) {
            category.setThumbnail(categoryDTO.getThumbnail());
        }
        category.setName(categoryDTO.getName());
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(MultipartFile file, CategoryDTO categoryDTO) throws Exception {
        Category existingCategory = getCategoryById(categoryDTO.getId());
        String fileImageOld = existingCategory.getThumbnail();
        existingCategory.setName(categoryDTO.getName());
        if (fileImageOld == null || fileImageOld.isEmpty()) {
            if (file != null && !file.isEmpty()) {
                String imageName = ImageUtil.storeFile(file);
                existingCategory.setThumbnail(imageName);
            }
        } else {
            if (file != null && !file.isEmpty()) {
                String imageName = ImageUtil.storeFile(file);
                existingCategory.setThumbnail(imageName);
                ImageUtil.deleteImage(fileImageOld);
            }
        }
        existingCategory = categoryRepository.save(existingCategory);
        return existingCategory;
    }

    @Override
    @Transactional
    public void deleteCategory(long id) throws Exception {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Category not found"));

        String thumbnail = category.getThumbnail();

        categoryRepository.deleteById(id);

        // Phát sự kiện sau khi xóa danh mục thành công
        applicationEventPublisher.publishEvent(new CategoryDeletedEvent(this, thumbnail));
    }
}

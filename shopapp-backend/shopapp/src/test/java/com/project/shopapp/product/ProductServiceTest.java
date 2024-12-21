package com.project.shopapp.product;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.project.shopapp.dtos.request.ProductDTO;
import com.project.shopapp.dtos.request.ProductSizeDTO;
import com.project.shopapp.entities.Category;
import com.project.shopapp.entities.Product;
import com.project.shopapp.entities.ProductImage;
import com.project.shopapp.entities.Size;
import com.project.shopapp.exceptions.AppException;
import com.project.shopapp.exceptions.ErrorCode;
import com.project.shopapp.repositories.CategoryRepository;
import com.project.shopapp.repositories.ProductImageRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.repositories.SizeRepository;
import com.project.shopapp.services.impls.ProductService;
import com.project.shopapp.utils.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    private MultipartFile thumbnail;
    private MultipartFile[] files;
    private static final Logger log = LoggerFactory.getLogger(ProductServiceTest.class);
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private SizeRepository sizeRepository;
    @Mock
    private ProductImageRepository productImageRepository;
    @InjectMocks
    private ProductService productService;
    private ProductDTO productDTO;
    private Category mockCategory;
    private Size mockSize;
    private  List<ProductSizeDTO> productSizeDTOS = new ArrayList<>();
    @BeforeEach
    void setUp() {
        productDTO = new ProductDTO();
        productDTO.setName("Product Name");
        productDTO.setPrice(100);
        thumbnail = new MockMultipartFile(
                "thumbnail",
                "thumbnail.jpg",
                "image/jpeg",
                "Fake image content".getBytes()
        );
        files = new MultipartFile[]{
                new MockMultipartFile(
                        "file1",
                        "detail1.jpg",
                        "image/jpeg",
                        "Fake image content 1".getBytes()
                ),
                new MockMultipartFile(
                        "file2",
                        "detail2.jpg",
                        "image/jpeg",
                        "Fake image content 2".getBytes()
                )
        };
        mockCategory = Category.builder().id(1L).name("Mock category").build();
        mockCategory.setId(1L);
        mockCategory.setName("Mock Category");
        mockSize = Size.builder().id(1L).sizeName("X").sizeCode("X").build();
    }
    @Test
    void testCreateProduct_SizeNotExisted() {
        Mockito.when(sizeRepository.findById(2L)).thenReturn(Optional.empty());
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(mockCategory));

        productDTO.setCategoryId(1L);
        productSizeDTOS.add(new ProductSizeDTO(2L, 30));
        productDTO.setProductSizes(productSizeDTOS);
        AppException exception = Assertions.assertThrows(AppException.class, () ->
                productService.createProduct(productDTO, thumbnail, files));
        Assertions.assertEquals(ErrorCode.SIZE_NOT_EXISTED, exception.getErrorCode());
    }
    @Test
    void testCreateProduct_CategoryNotExisted() {
        Mockito.when(categoryRepository.findById(2L)).thenReturn(Optional.empty());
        productDTO.setCategoryId(2L);
        AppException exception = Assertions.assertThrows(AppException.class, () ->
                productService.createProduct(productDTO, thumbnail, files));
        Assertions.assertEquals(ErrorCode.CATEGORY_NOT_EXISTED, exception.getErrorCode());
    }

    @Test
    void testCreateProduct_InvalidThumbnailFormat() {
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(mockCategory));
        Mockito.when(sizeRepository.findById(1L)).thenReturn(Optional.of(mockSize));
        productSizeDTOS.add(new ProductSizeDTO(1L,34));
        productDTO.setCategoryId(1L);
        productDTO.setProductSizes(productSizeDTOS);

        MultipartFile invalidThumbnail = new MockMultipartFile(
                "image", "test.txt", "text/plain", "invalid data".getBytes()
        );
        AppException exception = Assertions.assertThrows(AppException.class, () ->
                productService.createProduct(productDTO, invalidThumbnail, files));
        Assertions.assertEquals(ErrorCode.MISSING_FORMATTED_DATA, exception.getErrorCode());
    }

    @Test
    void testCreateProduct_DetailImageTooLarge() {
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(mockCategory));
        Mockito.when(sizeRepository.findById(1L)).thenReturn(Optional.of(mockSize));
        // When
        productSizeDTOS.add(new ProductSizeDTO(1L,34));
        productDTO.setCategoryId(1L);
        productDTO.setProductSizes(productSizeDTOS);

        MultipartFile largeFile = new MockMultipartFile(
                "file", "large.jpg", "image/jpeg", new byte[11 * 1024 * 1024]
        );
        AppException exception = Assertions.assertThrows(AppException.class, () ->
                productService.createProduct(productDTO, thumbnail, new MultipartFile[]{largeFile}));
        Assertions.assertEquals(ErrorCode.IMAGE_SIZE_OVERLOAD, exception.getErrorCode());
    }
}
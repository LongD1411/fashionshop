package com.project.shopapp.services;

import com.project.shopapp.dtos.request.ProductDTO;
import com.project.shopapp.dtos.request.ProductImageDTO;
import com.project.shopapp.dtos.respone.ProductResponse;
import com.project.shopapp.entities.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.project.shopapp.entities.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProductService {
    Product createProduct(ProductDTO productDTO, MultipartFile file,MultipartFile[] files) throws IOException;
    ProductResponse getProduct(Long id);
    Page<ProductResponse> getAllProducts(String keyword, Long categoryId,PageRequest pageRequest,Long minPrice,Long maxPrice);
    Product updateProduct(long id, ProductDTO productDTO,MultipartFile thumbnail,MultipartFile[] detail_images) throws IOException;
    void deleteProduct(long id) throws IOException;
    boolean existsByName(String name);
    ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception;
    List<ProductResponse> findAllProductsOrder(List<Long> ids);
    List<ProductResponse> getTop8ProductArrived();
    List<ProductResponse> getTop4ProductArrived();
}

package com.project.shopapp.controllers;

import com.project.shopapp.componens.LocalizationUtils;
import com.project.shopapp.dtos.request.ProductDTO;
import com.project.shopapp.dtos.respone.ApiResponse;
import com.project.shopapp.dtos.respone.ProductResponse;
import com.project.shopapp.entities.Product;
import com.project.shopapp.exceptions.AppException;
import com.project.shopapp.exceptions.ErrorCode;
import com.project.shopapp.respone.Message;
import com.project.shopapp.services.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.List;

@RestController
@RequestMapping(value = "${api.prefix}")
//@Validate
@RequiredArgsConstructor
public class ProductController {
    private final LocalizationUtils localizationUtils;
    private final IProductService productService;

    @PostMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Product> createProduct(@RequestPart("product") @Valid ProductDTO productDTO,
                                              @RequestPart("thumbnail") MultipartFile file,
                                              @RequestPart("detail_images") MultipartFile[] files) throws IOException {
        Product newProduct = productService.createProduct(productDTO, file, files);
        return ApiResponse.<Product>builder().result(newProduct).build();
    }

    @GetMapping("/public/products")
    public ApiResponse<ProductResponse> getProducts(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                    @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                                    @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                    @RequestParam(value = "categoryId", required = false, defaultValue = "0") Long categoryId,
                                                    @RequestParam(value = "size", required = false, defaultValue = "") String sizeId,
                                                    @RequestParam(value = "minPrice", required = false,defaultValue = "0") Long minPrice,
                                                    @RequestParam(value = "maxPrice", required = false, defaultValue = "1000000000") Long maxPrace) {
        PageRequest pageRequest = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());
        Page<ProductResponse> productPage = productService.getAllProducts(keyword, categoryId, pageRequest,minPrice,maxPrace);

        return ApiResponse.<ProductResponse>builder().results(productPage.getContent())
                .totalItem(productPage.getNumberOfElements())
                .totalPage(productPage.getTotalPages())
                .build();
    }

    @GetMapping("/public/top8-products")// http://localhost:8088/api/v1/products/top8
    public ApiResponse<ProductResponse> getTop8ProductsArrived() {
        List<ProductResponse> topProductArrived = productService.getTop8ProductArrived();
        return ApiResponse.<ProductResponse>builder().results(topProductArrived).build();
    }

    @GetMapping("/public/top4-products")// http://localhost:8088/api/v1/products/top4
    public ApiResponse<ProductResponse> getTop4ProductsArrived() {
        List<ProductResponse> topProductArrived = productService.getTop4ProductArrived();
        return ApiResponse.<ProductResponse>builder().results(topProductArrived).build();
    }

    @GetMapping("/public/product")
    public ApiResponse<ProductResponse> getProduct(@RequestParam("id") Long id) {
        ProductResponse existingProduct = productService.getProduct(id);
        return ApiResponse.<ProductResponse>builder().result(existingProduct).build();
    }

    @GetMapping("/public/product/orders")
    public ApiResponse<ProductResponse> getProductOrders(@RequestParam("ids") String ids) {
        List<Long> productIds = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        List<ProductResponse> productResponses = productService.findAllProductsOrder(productIds);
        return ApiResponse.<ProductResponse>builder().results(productResponses).build();
    }

    @DeleteMapping(value = "/product")
    public ApiResponse<Void> deleteProduct(@RequestParam("id") Long id) throws IOException {
        productService.deleteProduct(id);
        return ApiResponse.<Void>builder().message("Xóa thành công id " + id).build();
    }

    @PutMapping(value = "/product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Product> updateProduct(@Valid @RequestPart("product") ProductDTO productDTO,
                                              @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
                                              @RequestPart(value = "detail_images", required = false) MultipartFile[] files) throws IOException {

        if (productDTO.getId() == null) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTED);
        }
        Product updatedProduct = productService.updateProduct(productDTO.getId(), productDTO, thumbnail, files);
        return ApiResponse.<Product>builder().result(updatedProduct).build();
    }

}

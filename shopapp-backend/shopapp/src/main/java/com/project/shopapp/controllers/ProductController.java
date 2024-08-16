package com.project.shopapp.controllers;

import com.project.shopapp.componens.LocalizationUtils;
import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.entities.Product;
import com.project.shopapp.entities.ProductImage;
import com.project.shopapp.respone.ProductListRespone;
import com.project.shopapp.respone.ProductResponse;
import com.project.shopapp.services.IProductService;
import com.project.shopapp.utils.ImageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.List;

import static com.project.shopapp.statics.Image.MAXIMUM_IMAGES;

@RestController
@RequestMapping(value = "${api.prefix}/products")
//@Validate
@RequiredArgsConstructor
public class ProductController {
    private final LocalizationUtils localizationUtils;
    private final IProductService productService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(@RequestPart("product") ProductDTO productDTO,
                                           @RequestPart("thumbnail") MultipartFile file) {
        try {
            Product newProduct = productService.createProduct(productDTO, file);
            return ResponseEntity.ok("post request success: " + newProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@Valid @PathVariable("id") Long productId,
                                          @ModelAttribute("files") List<MultipartFile> files) {
        try {
            ProductResponse existingProduct = productService.getProduct(productId);
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if (files.size() > MAXIMUM_IMAGES) {
                return ResponseEntity.badRequest().body("You can only upload maximum 5 images");
            }
            List<ProductImage> listProductImages = new ArrayList<>();
            for (MultipartFile file : files) {

                if (file.getSize() == 0) {
                    continue;
                }
                // Kiểm tra kích thước file và định dạng
                if (file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File is  too large! Maximum size is 10mb");
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image");
                }
                // Lưu file và cập nhật  thumbnail trong dto
                String fileName = ImageUtil.storeFile(file);
                ProductImage productImage = productService.createProductImage(existingProduct.getId(), ProductImageDTO.builder()
                        .imageUrl(fileName)
                        .build());
                listProductImages.add(productImage);
            }
            return ResponseEntity.ok().body(listProductImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("")// http://localhost:8088/api/v1/categories?page=1&limit=1
        public ResponseEntity<ProductListRespone> getProducts(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                          @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                                          @RequestParam(value="keyword", required = false, defaultValue = "") String keyword,
                                                          @RequestParam(value="categoryId", required = false, defaultValue = "0") Long categoryId) {
        PageRequest pageRequest = PageRequest.of(page -1, limit, Sort.by("createdAt").descending());
        Page<ProductResponse> productPage = productService.getAllProducts(keyword,categoryId,pageRequest);
        ProductListRespone products = ProductListRespone.builder()
                .products(productPage.getContent())
                .totalPages(productPage.getTotalPages())
                .totalItems(productPage.getTotalElements())
                .build();
        return ResponseEntity.ok().body(products);
    }
    @GetMapping("/home")// http://localhost:8088/api/v1/products/home
    public ResponseEntity<?> getTopProductsArrived() {
        List<ProductResponse> topProductArrived = productService.getTopProductArrived();
        return ResponseEntity.ok().body(topProductArrived);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        try {
            ProductResponse existingProduct = productService.getProduct(id);
            return ResponseEntity.ok().body(existingProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/orders")
    public ResponseEntity<?> getProductOrders(@RequestParam("ids") String ids){
       try {
           if(ids== null || ids.isEmpty()){
               return ResponseEntity.ok("Ok");
           }
            List<Long> productIds = Arrays.stream(ids.split(","))
                    .map(Long::parseLong)
                    .toList();
           List<ProductResponse> productResponses = productService.findAllProductsOrder(productIds);
           return ResponseEntity.ok(productResponses);
       }catch(Exception e){
           return ResponseEntity.badRequest().body("aaa");
       }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Delete product " + id + " successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@Valid @PathVariable Long id,
                                           @RequestBody ProductDTO productDTO){
        try{
            Product updatedProduct = productService.updateProduct(id,productDTO);
            return ResponseEntity.ok().body(updatedProduct);
        }catch (Exception e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

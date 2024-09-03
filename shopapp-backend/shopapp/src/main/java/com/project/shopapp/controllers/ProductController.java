package com.project.shopapp.controllers;

import com.project.shopapp.componens.LocalizationUtils;
import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.entities.Product;
import com.project.shopapp.entities.ProductImage;
import com.project.shopapp.respone.Message;
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
                                           @RequestPart("thumbnail") MultipartFile file,
                                           @RequestPart("detail_images")MultipartFile[] files) {
        try {
            Product newProduct = productService.createProduct(productDTO, file,files);
            return ResponseEntity.ok(newProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("")// http://localhost:8088/api/v1/categories?page=1&limit=1
        public ResponseEntity<ProductListRespone> getProducts(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                              @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                                              @RequestParam(value="keyword", required = false, defaultValue = "") String keyword,
                                                              @RequestParam(value="categoryId", required = false, defaultValue = "0") Long categoryId,
                                                                 @RequestParam(value="size", required = false, defaultValue = "") String sizeId) {
        PageRequest pageRequest = PageRequest.of(page -1, limit, Sort.by("createdAt").descending());
        Page<ProductResponse> productPage = productService.getAllProducts(keyword,categoryId,pageRequest);
        ProductListRespone products = ProductListRespone.builder()
                .products(productPage.getContent())
                .totalPages(productPage.getTotalPages())
                .totalItems(productPage.getTotalElements())
                .build();
        return ResponseEntity.ok().body(products);
    }
    @GetMapping("/top8")// http://localhost:8088/api/v1/products/top8
    public ResponseEntity<?> getTop8ProductsArrived() {
        List<ProductResponse> topProductArrived = productService.getTop8ProductArrived();
        return ResponseEntity.ok().body(topProductArrived);
    }

    @GetMapping("/top4")// http://localhost:8088/api/v1/products/top4
    public ResponseEntity<?> getTop4ProductsArrived() {
        List<ProductResponse> topProductArrived = productService.getTop4ProductArrived();
        return ResponseEntity.ok().body(topProductArrived);
    }

    @GetMapping("/product")
    public ResponseEntity<?> getProduct(@RequestParam("id") Long id) {
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
    @DeleteMapping("")
    public ResponseEntity<?> deleteProduct(@RequestParam("id") Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(Message.builder().message("Xóa thành công sản phẩm có id: " + id).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(value = "",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(@RequestPart("product") ProductDTO productDTO,
                                           @RequestPart(value = "thumbnail" ,required = false) MultipartFile thumbnail,
                                           @RequestPart(value = "detail_images", required = false)MultipartFile[] files){
        try{
            if(productDTO.getId()==null){
                return  ResponseEntity.badRequest().body(Message.builder().message("Không tồn tại sản phẩm").build());
            }
            Product updatedProduct = productService.updateProduct(productDTO.getId(),productDTO,thumbnail,files);
            return ResponseEntity.ok().body(updatedProduct);
        }catch (Exception e){
            return  ResponseEntity.badRequest().body(Message.builder().message(e.getMessage()).build());
        }
    }

}

package com.project.shopapp.services.impls;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.dtos.ProductSizeDTO;
import com.project.shopapp.entities.*;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.repositories.*;
import com.project.shopapp.respone.ProductImageResponse;
import com.project.shopapp.respone.ProductResponse;
import com.project.shopapp.services.IProductService;
import com.project.shopapp.utils.ImageUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.project.shopapp.statics.Image.MAXIMUM_IMAGES;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private final SizeRepository sizeRepository;
    private final ProductSizeRepository productSizeRepository;

    @Override
    @Transactional
    public Product createProduct(ProductDTO productDTO, MultipartFile thumbnail, MultipartFile[] files) throws Exception {
        Category existsCategory = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(
                () -> new DataNotFoundException("Cannot find categoryID with id:" + productDTO.getCategoryId()));

        Product product = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .oldPrice(productDTO.getOldPrice())
                .description(productDTO.getDescription())
                .category(existsCategory)
                .sku(productDTO.getSku())
                .build();

        Set<ProductSize> productSizes = new HashSet<>();
        for (ProductSizeDTO sizeDTO : productDTO.getProductSizes()) {
            Size size = sizeRepository.findById(sizeDTO.getSizeId())
                    .orElseThrow(() -> new DataNotFoundException("Size not found"));
            ProductSize productSize = ProductSize.builder()
                    .product(product)
                    .size(size)
                    .quantity(sizeDTO.getQuantity())
                    .build();
            productSizes.add(productSize);
        }
        if (!thumbnail.isEmpty()) {
            // Kiểm tra kích thước file và định dạng
            ResponseEntity<?> fileCheckResult = ImageUtil.checkImage(thumbnail);
            if (fileCheckResult != null) {
               throw  new Exception("Ảnh quá lớn hoặc file không đúng định dạnh");
            }
            // Lưu file và cập nhật thumbnail trong dto
            String fileName = ImageUtil.storeFile(thumbnail);
            product.setThumbnail(fileName);
        }
        product.setProductSizes(productSizes);
        product = productRepository.save(product);
        //Luu detail_images
        if(files != null || files.length>0){
            for (MultipartFile file : files) {
                if (file.getSize() == 0) {
                    continue;
                }
                // Kiểm tra kích thước file và định dạng
                ResponseEntity<?> fileCheckResult = ImageUtil.checkImage(thumbnail);
                if (fileCheckResult != null) {
                    throw  new Exception("Ảnh quá lớn hoặc file không đúng định dạnh");
                }
                // Lưu file
                String fileName = ImageUtil.storeFile(file);
                ProductImage newProductImage = ProductImage.builder()
                        .product(product)
                        .imageUrl(fileName)
                        .build();
                productImageRepository.save(newProductImage);
            }
        }
        return  product;
    }

    @Override
    public ProductResponse getProduct(Long id) throws Exception {
            Product product = productRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Cannot find product with id: " + id));
        List<ProductSize>  productSizes= productSizeRepository.findByProductId(product.getId());
        List<Size> sizes = new ArrayList<>();
        for(ProductSize productSize: productSizes){
            sizes.add(productSize.getSize());
        }
        List<ProductImage> productImages = productImageRepository.findByProductId(product.getId());
        return ProductResponse.toProductRespone(product, ProductImageResponse.toProductImageResponse(productImages), sizes);
    }

    @Override
    public Page<ProductResponse> getAllProducts(String keyword,Long categoryId,PageRequest pageRequest) {
        return productRepository.searchProduct(pageRequest,categoryId,keyword).map(ProductResponse::toProductResponse2);
    }

    @Override
    public Product updateProduct(long id, ProductDTO productDTO) throws Exception {
        Product existsProduct = productRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Cannot find product with id:" + id));
        if (existsProduct != null) {
            Category existsCategory = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(
                    () -> new DataNotFoundException("Cannot find categoryID with id:" + productDTO.getCategoryId()));
           existsProduct = Product.toProduct(productDTO);
           existsProduct.setCategory(existsCategory);
            return productRepository.save(existsProduct);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteProduct(long id) throws Exception{
        Product existsProduct = productRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Cannot find product with id:" + id));
      List<ProductImage> productImageList = productImageRepository.findByProductId(existsProduct.getId());
      ImageUtil.deleteImage(existsProduct.getThumbnail());
      productRepository.delete(existsProduct);
      for(ProductImage productImage : productImageList){
          ImageUtil.deleteImage(productImage.getImageUrl());
      }
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception {
        Product existsProduct = productRepository.findById(productId).orElseThrow(() ->
                new DataNotFoundException("Cannot find product with id: " + productImageDTO.getProductId()));
        ProductImage newProductImage = ProductImage.builder()
                .product(existsProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        int size = productImageRepository.findByProductId(productId).size();
        if (size >= MAXIMUM_IMAGES) {
            throw new InvalidParamException("Number of  image must be <= " + MAXIMUM_IMAGES);
        }
        return productImageRepository.save(newProductImage);
    }

    @Override
    public List<ProductResponse> findAllProductsOrder(List<Long> ids) {
       List<Product> products =  productRepository.findAllOrderProduct(ids);
       List<ProductResponse> productResponses = products.stream().map(product -> ProductResponse.builder()
               .id(product.getId())
               .name(product.getName())
               .price(product.getPrice())
               .oldPrice(product.getOldPrice())
               .thumbnail(product.getThumbnail())
               .build()).toList();
        return  productResponses ;
    }

    @Override
    public List<ProductResponse> getTop8ProductArrived() {
       List<Product> products = productRepository.findTop8ProductOrderByUpdatedDate();
       List<ProductResponse> productResponses = new ArrayList<>();
       for(Product product : products){
           ProductResponse productResponse = ProductResponse.builder()
                   .thumbnail(product.getThumbnail())
                   .categoryId(product.getCategory().getId())
                   .description(product.getDescription())
                   .oldPrice(product.getOldPrice())
                   .price(product.getPrice())
                   .id(product.getId())
                   .name(product.getName())
                   .build();
           productResponses.add(productResponse);
       }
        return productResponses;
    }
    @Override
    public List<ProductResponse> getTop4ProductArrived() {
        List<Product> products = productRepository.findTop4ProductOrderByUpdatedDate();
        List<ProductResponse> productResponses = new ArrayList<>();
        for(Product product : products){
            ProductResponse productResponse = ProductResponse.builder()
                    .thumbnail(product.getThumbnail())
                    .categoryId(product.getCategory().getId())
                    .description(product.getDescription())
                    .oldPrice(product.getOldPrice())
                    .price(product.getPrice())
                    .id(product.getId())
                    .name(product.getName())
                    .build();
            productResponses.add(productResponse);
        }
        return productResponses;
    }
}

package com.project.shopapp.services.impls;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.dtos.ProductSizeDTO;
import com.project.shopapp.entities.*;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.repositories.CategoryRepository;
import com.project.shopapp.repositories.ProductImageRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.repositories.SizeRepository;
import com.project.shopapp.respone.ProductImageResponse;
import com.project.shopapp.respone.ProductResponse;
import com.project.shopapp.services.IProductService;
import com.project.shopapp.utils.ImageUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

//    @Override
//    public Product createProduct(ProductDTO productDTO) throws Exception {
//        Category existsCategory = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(
//                () -> new DataNotFoundException("Cannot find categoryID with id:" + productDTO.getCategoryId()));
//        Product newProduct = Product.toProduct(productDTO);
//        newProduct.setCategory(existsCategory);
//        return productRepository.save(newProduct);
//    }
    @Override
    @Transactional
    public Product createProduct(ProductDTO productDTO, MultipartFile file) throws Exception {
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
        if (!file.isEmpty()) {
            // Kiểm tra kích thước file và định dạng
            ResponseEntity<?> fileCheckResult = ImageUtil.checkImage(file);
            if (fileCheckResult != null) {
               throw  new Exception("Ảnh quá lớn hoặc file không đúng định dạnh");
            }
            // Lưu file và cập nhật thumbnail trong dto
            String fileName = ImageUtil.storeFile(file);
            product.setThumbnail(fileName);
        }
        product.setProductSizes(productSizes);
        return productRepository.save(product);
    }


    @Override
    public ProductResponse getProduct(Long id) throws Exception {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Cannot find product with id: " + id));
        List<ProductImage> productImages = productImageRepository.findByProductId(id);
        return ProductResponse.toProductRespone(product, ProductImageResponse.toProductImageResponse(productImages));
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
    public void deleteProduct(long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(product -> productRepository.delete(product));
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
    public List<ProductResponse> getTopProductArrived() {
       List<Product> products = productRepository.findTop6ProductOrderByUpdatedDate();
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

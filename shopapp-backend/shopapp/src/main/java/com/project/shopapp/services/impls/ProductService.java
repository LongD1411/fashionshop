package com.project.shopapp.services.impls;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.dtos.ProductSizeDTO;
import com.project.shopapp.dtos.SizeDTO;
import com.project.shopapp.entities.*;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.repositories.*;
import com.project.shopapp.respone.ProductImageResponse;
import com.project.shopapp.respone.ProductResponse;
import com.project.shopapp.respone.SizeResponse;
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
import java.util.stream.Collectors;

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
        List<SizeResponse> sizes = new ArrayList<>();
        for(ProductSize productSize: productSizes){
           SizeResponse sizeResponse = SizeResponse.builder().id(productSize.getSize().getId())
                   .sizeName(productSize.getSize().getSizeName())
                   .sizeCode(productSize.getSize().getSizeCode())
                   .quantity(productSize.getQuantity()).build();
           sizes.add(sizeResponse);
        }
        List<ProductImage> productImages = productImageRepository.findByProductId(product.getId());
        return ProductResponse.toProductRespone(product, ProductImageResponse.toProductImageResponse(productImages), sizes);
    }

    @Override
    public Page<ProductResponse> getAllProducts(String keyword,Long categoryId,PageRequest pageRequest) {
        return productRepository.searchProduct(pageRequest,categoryId,keyword).map(ProductResponse::toProductResponse2);
    }

    @Override
    @Transactional
    public Product updateProduct(long id, ProductDTO productDTO,MultipartFile thumbnail, MultipartFile[] detail_images) throws Exception {
        Product existsProduct = productRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Cannot find product with id:" + id));
        if (existsProduct != null) {
            Category existsCategory = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(
                    () -> new DataNotFoundException("Cannot find categoryID with id:" + productDTO.getCategoryId()));
           existsProduct = Product.toProduct(existsProduct,productDTO);
           existsProduct.setCategory(existsCategory);
            // Xóa các ProductSize không còn tồn tại trong DTO
            Iterator<ProductSize> iterator = existsProduct.getProductSizes().iterator();
            while (iterator.hasNext()) {
                ProductSize existingSize = iterator.next();
                boolean existsInDTO = productDTO.getProductSizes().stream()
                        .anyMatch(dtoSize -> dtoSize.getSizeId().equals(existingSize.getSize().getId()));
                if (!existsInDTO) {
                    iterator.remove();  // Xóa khỏi set và tự động xóa trong DB
                }
            }
            final Product product = existsProduct;
            for (ProductSizeDTO sizeDTO : productDTO.getProductSizes()) {
                ProductSize productSize = existsProduct.getProductSizes().stream()
                        .filter(ps -> ps.getSize().getId().equals(sizeDTO.getSizeId()))
                        .findFirst()
                        .orElseGet(() -> {
                            ProductSize newProductSize = new ProductSize();
                            newProductSize.setProduct(product);
                            try {
                                newProductSize.setSize(sizeRepository.findById(sizeDTO.getSizeId())
                                        .orElseThrow(() -> new DataNotFoundException("Size not found")));
                            } catch (DataNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            product.getProductSizes().add(newProductSize);
                            return newProductSize;
                        });

                productSize.setQuantity(sizeDTO.getQuantity());
            }

            if (thumbnail != null && !thumbnail.isEmpty()) {
                // Kiểm tra kích thước file và định dạng
                ResponseEntity<?> fileCheckResult = ImageUtil.checkImage(thumbnail);
                if (fileCheckResult != null) {
                    throw  new Exception("Ảnh quá lớn hoặc file không đúng định dạnh");
                }
                // Lưu file và cập nhật thumbnail trong dto
                String fileName = ImageUtil.storeFile(thumbnail);
                ImageUtil.deleteImage(existsProduct.getThumbnail());
                existsProduct.setThumbnail(fileName);
            }
            if(productDTO.getDetailProductImageIds() != null){
                List<ProductImage> productImages = productImageRepository.findByProductId(existsProduct.getId());
                for(ProductImage productImage: productImages){
                    if (!Arrays.asList(productDTO.getDetailProductImageIds()).contains(productImage.getId())) {
                        // Xóa ảnh từ hệ thống tệp
                        ImageUtil.deleteImage(productImage.getImageUrl());
                        // Xóa ảnh từ cơ sở dữ liệu
                        productImageRepository.delete(productImage);
                    }
                }
            }
            if(detail_images != null && detail_images.length>0){
                for (MultipartFile image : detail_images) {
                    if (image.getSize() == 0) {
                        continue;
                    }
                    // Kiểm tra kích thước file và định dạng
                    ResponseEntity<?> fileCheckResult = ImageUtil.checkImage(image);
                    if (fileCheckResult != null) {
                        throw  new Exception("Ảnh quá lớn hoặc file không đúng định dạnh");
                    }
                    // Lưu file
                    String fileName = ImageUtil.storeFile(image);
                    ProductImage newProductImage = ProductImage.builder()
                            .product(existsProduct)
                            .imageUrl(fileName)
                            .build();
                    productImageRepository.save(newProductImage);
                }
            }
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

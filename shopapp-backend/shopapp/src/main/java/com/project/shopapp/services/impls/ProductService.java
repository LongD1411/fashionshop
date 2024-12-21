package com.project.shopapp.services.impls;

import com.project.shopapp.dtos.request.ProductDTO;
import com.project.shopapp.dtos.request.ProductImageDTO;
import com.project.shopapp.dtos.request.ProductSizeDTO;
import com.project.shopapp.dtos.respone.ProductImageResponse;
import com.project.shopapp.dtos.respone.ProductResponse;
import com.project.shopapp.dtos.respone.SizeResponse;
import com.project.shopapp.entities.*;
import com.project.shopapp.exceptions.AppException;
import com.project.shopapp.exceptions.ErrorCode;
import com.project.shopapp.repositories.*;
import com.project.shopapp.services.IProductService;
import com.project.shopapp.utils.ImageUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final OrderDetailRepository orderDetailRepository;

    @Override
    @Transactional
    public Product createProduct(ProductDTO productDTO, MultipartFile image, MultipartFile[] files) throws IOException {
        Category existsCategory = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        Product product = Product.toProduct(productDTO);
        product.setCategory(existsCategory);

        Set<ProductSize> productSizes = new HashSet<>();
        for (ProductSizeDTO sizeDTO : productDTO.getProductSizes()) {
            Size size = sizeRepository.findById(sizeDTO.getSizeId()).orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));
            ProductSize productSize = ProductSize.builder().product(product).size(size).quantity(sizeDTO.getQuantity()).build();
            productSizes.add(productSize);
        }
        product.setProductSizes(productSizes);

        ImageUtil.checkImage(image);
        String thumbnail = ImageUtil.storeFile(image);
        product.setThumbnail(thumbnail);
        product = productRepository.save(product);
        //Luu detail_images
        if (files != null || files.length > 0) {
            for (MultipartFile file : files) {
                if (file.getSize() == 0) {
                    continue;
                }
                ImageUtil.checkImage(file);
                String fileName = ImageUtil.storeFile(file);
                ProductImage newProductImage = ProductImage.builder().product(product).imageUrl(fileName).build();
                productImageRepository.save(newProductImage);
            }
        }
        return product;
    }

    @Override
    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        List<ProductSize> productSizes = productSizeRepository.findByProductId(product.getId());
        List<SizeResponse> sizes = new ArrayList<>();
        for (ProductSize productSize : productSizes) {
            SizeResponse sizeResponse = SizeResponse.builder().id(productSize.getSize().getId()).sizeName(productSize.getSize().getSizeName()).sizeCode(productSize.getSize().getSizeCode()).quantity(productSize.getQuantity()).build();
            sizes.add(sizeResponse);
        }
        List<ProductImage> productImages = productImageRepository.findByProductId(product.getId());
        return ProductResponse.toProductRespone(product, ProductImageResponse.toProductImageResponse(productImages), sizes);
    }

    @Override
    public Page<ProductResponse> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest,Long minPrice, Long maxPrice) {
        return productRepository.searchProduct(categoryId, keyword,minPrice,maxPrice,pageRequest).map(ProductResponse::toProductResponse2);
    }

    @Override
    @Transactional
    public Product updateProduct(long id, ProductDTO productDTO, MultipartFile thumbnail, MultipartFile[] detail_images) throws IOException {
        Product existsProduct = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        Category existsCategory = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        Product.toProduct(existsProduct, productDTO);
        existsProduct.setCategory(existsCategory);
        // Xóa các ProductSize không còn tồn tại
        Iterator<ProductSize> iterator = existsProduct.getProductSizes().iterator();
        while (iterator.hasNext()) {
            ProductSize existingSize = iterator.next();
            boolean existsInDTO = productDTO.getProductSizes().stream().anyMatch(dtoSize -> dtoSize.getSizeId().equals(existingSize.getSize().getId()));
            if (!existsInDTO) {
                iterator.remove();  // Xóa khỏi set và update
            }
        }
        final Product product = existsProduct;
        for (ProductSizeDTO sizeDTO : productDTO.getProductSizes()) {
            ProductSize productSize = existsProduct.getProductSizes().stream().filter(ps -> ps.getSize().getId().equals(sizeDTO.getSizeId())).findFirst().orElseGet(() -> {
                ProductSize newProductSize = new ProductSize();
                newProductSize.setProduct(product);
                newProductSize.setSize(sizeRepository.findById(sizeDTO.getSizeId()).orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_EXISTED)));
                product.getProductSizes().add(newProductSize);
                return newProductSize;
            });

            productSize.setQuantity(sizeDTO.getQuantity());
        }
        if (thumbnail != null) {
            ImageUtil.checkImage(thumbnail);
            String fileName = ImageUtil.storeFile(thumbnail);
            if (existsProduct.getThumbnail() != null) {
                ImageUtil.deleteImage(existsProduct.getThumbnail());
            }
            existsProduct.setThumbnail(fileName);
        }
        if (productDTO.getDetailProductImageIds() != null) {
            List<ProductImage> productImages = productImageRepository.findByProductId(existsProduct.getId());
            for (ProductImage productImage : productImages) {
                if (!Arrays.asList(productDTO.getDetailProductImageIds()).contains(productImage.getId())) {
                    // Xóa ảnh từ hệ thống tệp
                    ImageUtil.deleteImage(productImage.getImageUrl());
                    // Xóa ảnh từ cơ sở dữ liệu
                    productImageRepository.delete(productImage);
                }
            }
        }
        if (detail_images != null) {
            for (MultipartFile image : detail_images) {
                if (image.getSize() == 0) {
                    continue;
                }
                // Kiểm tra kích thước file và định dạng
                ImageUtil.checkImage(image);
                // Lưu file
                String nameDetailImage = ImageUtil.storeFile(image);
                ProductImage newProductImage = ProductImage.builder().product(existsProduct).imageUrl(nameDetailImage).build();
                productImageRepository.save(newProductImage);
            }
        }
        return productRepository.save(existsProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(long id) throws IOException {
        Product existsProduct = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        List<ProductImage> productImageList = productImageRepository.findByProductId(existsProduct.getId());
        List<OrderDetail> orderDetails = orderDetailRepository.findByProductId(id);
        if (!orderDetails.isEmpty()) {
            orderDetailRepository.deleteAll(orderDetails);
        }
        ImageUtil.deleteImage(existsProduct.getThumbnail());
        productRepository.delete(existsProduct);
        for (ProductImage productImage : productImageList) {
            ImageUtil.deleteImage(productImage.getImageUrl());
        }
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception {
        Product existsProduct = productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        ProductImage newProductImage = ProductImage.builder().product(existsProduct).imageUrl(productImageDTO.getImageUrl()).build();
        int size = productImageRepository.findByProductId(productId).size();
        if (size >= MAXIMUM_IMAGES) {
            throw new AppException(ErrorCode.IMAGE_MAX);
        }
        return productImageRepository.save(newProductImage);
    }

    @Override
    public List<ProductResponse> findAllProductsOrder(List<Long> ids) {
        List<Product> products = productRepository.findAllOrderProduct(ids);
        List<ProductResponse> productResponses = products.stream().map(product -> ProductResponse.builder().id(product.getId()).name(product.getName()).price(product.getPrice()).oldPrice(product.getOldPrice()).thumbnail(product.getThumbnail()).build()).toList();
        return productResponses;
    }

    @Override
    public List<ProductResponse> getTop8ProductArrived() {
        List<Product> products = productRepository.findTop8ProductOrderByUpdatedDate();
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products) {
            ProductResponse productResponse = ProductResponse.builder().thumbnail(product.getThumbnail()).categoryId(product.getCategory().getId()).description(product.getDescription()).oldPrice(product.getOldPrice()).price(product.getPrice()).id(product.getId()).name(product.getName()).build();
            productResponses.add(productResponse);
        }
        return productResponses;
    }

    @Override
    public List<ProductResponse> getTop4ProductArrived() {
        List<Product> products = productRepository.findTop4ProductOrderByUpdatedDate();
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products) {
            ProductResponse productResponse = ProductResponse.builder().thumbnail(product.getThumbnail()).categoryId(product.getCategory().getId()).description(product.getDescription()).oldPrice(product.getOldPrice()).price(product.getPrice()).id(product.getId()).name(product.getName()).build();
            productResponses.add(productResponse);
        }
        return productResponses;
    }
}

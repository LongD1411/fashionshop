package com.project.shopapp.services.impls;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.entities.Category;
import com.project.shopapp.entities.Product;
import com.project.shopapp.entities.ProductImage;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.repositories.CategoryRepository;
import com.project.shopapp.repositories.ProductImageRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.respone.ProductImageResponse;
import com.project.shopapp.respone.ProductResponse;
import com.project.shopapp.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.project.shopapp.statics.Image.MAXIMUM_IMAGES;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductImageRepository productImageRepository;

    @Override
    public Product createProduct(ProductDTO productDTO) throws Exception {
        Category existsCategory = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(
                () -> new DataNotFoundException("Cannot find categoryID with id:" + productDTO.getCategoryId()));
        Product newProduct = Product.toProduct(productDTO);
        newProduct.setCategory(existsCategory);
        return productRepository.save(newProduct);
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
               .thumbnail(product.getThumbnail())
               .build()).toList();
        return  productResponses ;
    }
}

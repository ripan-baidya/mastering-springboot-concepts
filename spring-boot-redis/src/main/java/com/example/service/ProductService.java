package com.example.service;

import com.example.entity.Product;
import com.example.repository.ProductRepository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "products")
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Cacheable(key = "#id")
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @CachePut(key = "#result.id")
    public Product saveProduct(Product product) {
        Product createdProduct = Product.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
        return productRepository.save(createdProduct);
    }

    @CacheEvict(key = "#id")
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}

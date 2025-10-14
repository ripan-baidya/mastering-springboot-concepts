package com.springboot.graphql.service.impl;

import com.springboot.graphql.model.Product;
import com.springboot.graphql.repository.ProductRepository;
import com.springboot.graphql.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        log.info("fetching all products.");
        return productRepository.findAll();
    }

    @Override
    public List<Product> findProductByCategory(String category) {
        log.info("fetching products by category.");
        return productRepository.findByCategory(category);
    }

    @Override
    public Product updateStock(String id, Double stock) {
        log.info("Updating stock for product with id {} to {}.", id, stock);
        Product existingProduct = productRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        existingProduct.setStock(stock);
        return productRepository.save(existingProduct);
    }

    @Override
    public Product receiveNewShipment(String id, Double stock) {
        log.info("Updating stock for product with id {} by {} units.", id, stock);
        Product existingProduct = productRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        existingProduct.setStock(existingProduct.getStock() + stock);
        return productRepository.save(existingProduct);
    }
}

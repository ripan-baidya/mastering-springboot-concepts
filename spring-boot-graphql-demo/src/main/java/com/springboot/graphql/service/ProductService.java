package com.springboot.graphql.service;

import com.springboot.graphql.model.Product;

import java.util.List;

public interface ProductService {

    // get all products
    public List<Product> getAllProducts();

    // get product by category
    public List<Product> findProductByCategory(String category);

    // update the stock of a product
    public Product updateStock(String id, Double stock);

    // update the stock of a product by adding the stock to the existing stock
    public Product receiveNewShipment(String id, Double stock);
}

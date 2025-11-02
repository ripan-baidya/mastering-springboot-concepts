package com.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    /**
     * <p>
     * Current we are not working with any database, but in real time all the data will come from the
     * database. let's say we have list of products available, and we want to get the all the products
     * and want to add a product.
     * </p>
     * <p>
     *      Note: We are not working with db.
     * </p>
     */
    // Product POJO
    private record Product(Integer id, String name, Double price) { }

    // List of all products
    private List<Product> products = new ArrayList<>(
            List.of(
                    new Product(1, "iPhone11", 200.0),
                    new Product(2, "Mac M2", 999.0),
                    new Product(3, "iPad", 599.0),
                    new Product(4, "MacBook Pro", 1299.0)
            )
    );

    @GetMapping
    public List<Product> getAllProducts() {
        return products;
    }

    // With @PreAuthorize we can define the roles that are allowed to perform the operation
    // @PreAuthorize(value = "hasRole('ADMIN', 'USER')")
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        products.add(product);
        return product;
    }


    // *************** CSRF & IT'S USES ***************
    /**
     * Note::
     * In order to access the "/api/v1/products" for "GET" we need to pass the valid credentials.
     * But if we want to add a product (or any other operation other than "GET"), we need to pass the CSRF token
     * in the header(X-CSRF-TOKEN).
     * The CSRF token is hidden and we need to pass it in order to perform any other operation other than "GET".
     * eg: X-CSRF-TOKEN: <csrf-token>
     */
    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }
}

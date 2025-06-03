package com.springboot.graphql;

import com.springboot.graphql.model.Product;
import com.springboot.graphql.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@RequiredArgsConstructor
@SpringBootApplication
public class SpringBootGraphqlDemoApplication {

    private final ProductRepository productRepository;

    /**
     * Here, we are populating the database with some initial data.
     * this is just for demo purposes.
     * in a real-world scenario, this is not recommended
     */
    @PostConstruct
    public void initDb(){
        List<Product> products = List.of(
            new Product("Samsung Galaxy S22 Ultra", "Smartphones", "1000", 10.00),
            new Product("Apple iPhone 14 Pro", "Smartphones", "1200", 20.00),
            new Product("Sony WH-1000XM5", "Headphones", "350", 30.00),
            new Product("Microsoft Surface Laptop 4", "Laptops", "1000", 40.00),
            new Product("Canon EOS 5D Mark IV", "DSLR Cameras", "2500", 50.00),
            new Product("HP Pavilion Gaming 15", "Laptops", "700", 60.00),
            new Product("Samsung Galaxy Tab S8", "Tablets", "500", 70.00),
            new Product("Apple Watch Series 7", "Smartwatches", "500", 80.00),
            new Product("Sony WF-1000XM4", "Earbuds", "200", 90.00),
            new Product("Asus ROG Zephyrus G14", "Laptops", "1300", 100.00)
        );
        productRepository.saveAll(products);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootGraphqlDemoApplication.class, args);
    }

}

package com.springboot.graphqlclient.controller;

import com.springboot.graphqlclient.dto.Item;
import com.springboot.graphqlclient.dto.ItemRequest;
import com.springboot.graphqlclient.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/catalog")
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping("/products/all")
    public List<Item> getAllItems() {
        return catalogService.getItems();
    }

    @GetMapping("/products/category/{category}")
    public List<Item> getItemsByCategory(@PathVariable String category) {
        return catalogService.getItemsByCategory(category);
    }

    @PutMapping("/products/update-stock")
    public Item updateItemStock(@RequestBody ItemRequest request) {
        return catalogService.updateItemStock(request);
    }
}
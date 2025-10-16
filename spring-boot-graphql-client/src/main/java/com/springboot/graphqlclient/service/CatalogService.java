package com.springboot.graphqlclient.service;

import com.springboot.graphqlclient.client.InventoryClient;
import com.springboot.graphqlclient.dto.Item;
import com.springboot.graphqlclient.dto.ItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This is an catalog service that uses {@link InventoryClient} to make HTTP requests to a GraphQL server.
 *
 * The service exposes three operations that can be used to interact with the inventory:
 * 1. get all items
 * 2. get items by category
 * 3. update item stock
 *
 * The service is named "catalog-service" and it is the entry point for all the operations related to the inventory.
 */
@RequiredArgsConstructor
@Service
public class CatalogService {

    private final InventoryClient inventoryClient;

    public List<Item> getItems() {
        return inventoryClient.getItems();
    }

    public List<Item> getItemsByCategory(String category) {
        return inventoryClient.getItemsByCategory(category);
    }

    public Item updateItemStock(ItemRequest request) {
        return inventoryClient.updateItemStock(request);
    }
}

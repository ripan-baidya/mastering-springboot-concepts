package com.springboot.graphqlclient.client;

import com.springboot.graphqlclient.dto.Item;
import com.springboot.graphqlclient.dto.ItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.util.List;

/**
 * This class is a client that uses Spring GraphQL to make HTTP requests to a GraphQL server.
 *
 * The server is assumed to have queries and mutations for inventory management.
 *
 * The client is named "catalog-service" and it needs to access three endpoints:
 * 1. get all items
 * 2. get items by category
 * 3. update item stock
 *
 * For each endpoint, the fields we want to return are different.
 * We have created a DTO for each endpoint and used {@link JsonInclude(JsonInclude.Include.NON_DEFAULT)} annotation
 * to make sure that we will not add fields having default values in the response.
 *
 */

@RequiredArgsConstructor
@Component
public class InventoryClient {

    private final HttpGraphQlClient graphQlClient;

    /**
     * This method makes a HTTP request to the GraphQL server to get all the items.
     * It returns a list of product items including fields name, category and price.
     *
     * @return list of product items
     */
    public List<Item> getItems(){
        String query = """
            query GetAllProducts {
                getAllProducts {
                    name
                    category
                    price
                }
            }
            """;

        return graphQlClient.document(query).retrieve("getAllProducts")
                .toEntityList(Item.class).block();
    }

    /**
     * Makes a HTTP request to the GraphQL server to get items by category.
     * Returns a list of product items including fields: name, category, price, and stock.
     *
     * @param category the category to filter items by
     * @return list of items matching the category
     */
    public List<Item> getItemsByCategory(String category) {
        String query = """
            query FindProductByCategory {
                findProductByCategory(category: "%s") {
                    name
                    category
                    price
                    stock
                }
            }
            """.formatted(category);

        return graphQlClient.document(query)
                .retrieve("findProductByCategory")
                .toEntityList(Item.class)
                .block();
    }

    /**
     * This method makes a HTTP request to the GraphQL server to update the stock of an item.
     * It takes an {@link ItemRequest} object as input which has two fields:
     * 1. id: The id of the item
     * 2. stock: The new stock quantity of the item
     * It will update the items and return the updated item.
     */
    public Item updateItemStock(ItemRequest request){
        String query = String.format("mutation UpdateStock {\n" +
                "    updateStock(id: \"%s\", stock: %f) {\n" +
                "        name\n" +
                "        stock\n" +
                "    }\n" +
                "}\n", request.getId(), request.getStock());

        return graphQlClient.document(query).retrieve("updateStock")
                .toEntity(Item.class).block();
    }
}

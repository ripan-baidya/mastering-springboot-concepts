package com.springboot.graphqlclient.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// This annotation is used to specify that properties with default values should be excluded from serialization.
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Item {

    private String name;
    private String category;
    private String price;
    private Double stock;
}

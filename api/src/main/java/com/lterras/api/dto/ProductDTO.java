package com.lterras.api.dto;

import com.lterras.api.model.Product;
import lombok.Data;

@Data
public class ProductDTO {

    private Long id;

    private String name;

    private String description;

    private float price;

    private int quantity;

    public ProductDTO() {}

    public ProductDTO(Product product) {
        this.setId(product.getId());
        this.setName(product.getName());
        this.setDescription(product.getDescription());
        this.setPrice(product.getPrice());
        this.setQuantity(product.getQuantity());
    }

}

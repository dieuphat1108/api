package com.project.api.dto;

import com.project.api.entity.Product;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductDto {
    private Long id;

    private String name;

    private BigDecimal price;

    private String description;

    private String imageUrl;

    private int unitsInStock;

    private String category;

    public ProductDto() {
    }

    public ProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.imageUrl = product.getImageUrl();
        this.unitsInStock = product.getUnitsInStock();
        this.category = product.getCategory().getCategoryName();
    }
}
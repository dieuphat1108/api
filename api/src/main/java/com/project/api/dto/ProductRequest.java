package com.project.api.dto;

import lombok.Data;

import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductRequest {
    private String name;

    private BigDecimal price;

    private String description;

    private String imageUrl;

    private int unitsInStock;

    @CreationTimestamp
    private Date lastUpdated;

    private Long categoryId;
}

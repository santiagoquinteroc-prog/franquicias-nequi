package com.red.franquicias.nequi.model.product;

import lombok.*;



@Data
public class Product {

    private Long id;
    private Long branchId;
    private String name;
    private Integer stock;
}

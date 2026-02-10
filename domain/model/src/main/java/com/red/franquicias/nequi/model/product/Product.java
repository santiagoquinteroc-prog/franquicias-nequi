package com.red.franquicias.nequi.model.product;

import lombok.*;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private Long id;
    private Long branchId;
    private String name;
    private Integer stock;
}
